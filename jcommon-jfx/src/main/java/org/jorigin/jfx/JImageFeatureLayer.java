package org.jorigin.jfx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jorigin.identification.Named;
import org.jorigin.state.HandleDisplay;
import org.jorigin.state.HandleSelection;

/**
 * An layer that can reference {@link JImageFeature image feature} that can be displayed within a {@link JImageCanvas JImageCanvas}.
 * @author Julien Seinturier - IVM Technologies - http://www.seinturier.fr
 * @version 1.0.0
 */
public class JImageFeatureLayer implements HandleSelection, HandleDisplay, Named {

	/**
	 * Is the layer is selectable? If <code>true</code> the layer can be selected and <code>false</code> otherwise.
	 */
	private boolean selectable  = true;

	/**
	 * Is the layer is selected? If <code>true</code> the layer is currently selected and <code>false</code> otherwise.
	 */
	private boolean selected   = false;

	/**
	 * Is the layer is displayable? If <code>true</code> the layer can be displayed and <code>false</code> otherwise.
	 */
	private boolean displayable = true;

	/**
	 * Is the layer is displaying? If <code>true</code> the layer is currently displayed and <code>false</code> otherwise.
	 */
	private boolean displaying  = false;

	/**
	 * The name of the layer.
	 */
	private String name;

	/**
	 * The {@link JImageFeature image features} that compose this layer.
	 * <p>
	 * This list is never <code>null</code> but can be empty.
	 */
	private List<JImageFeature> features = null;

	/**
	 * The {@link JImageFeatureLayerListener listeners} that are attached to this layer.
	 * <p>
	 * This list is never <code>null</code> but can be empty.
	 */
	private List<JImageFeatureLayerListener> layerListeners;

	@Override
	public boolean isStateDisplaying() {
		return this.displaying;
	}

	@Override
	public void setStateDisplaying(boolean displaying) {
		this.displaying = displaying;
	}

	@Override
	public boolean isStateDisplayable() {
		return this.displayable;
	}

	@Override
	public void setStateDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

	@Override
	public boolean isStateSelected() {
		return this.selected;
	}

	@Override
	public void setStateSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isStateSelectable() {
		return this.selectable;
	}

	@Override
	public void setStateSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Create a new @link JImageFeature image feature} layer with the given name. By default the layer is displayable, displayed, selectable but not selected.
	 * @param name the name of the layer
	 */
	public JImageFeatureLayer(String name) {
		this(name, null, true, false, true, true);
	}

	/**
	 * Create a new {@link JImageFeature image feature} layer with the given parameters.
	 * @param name the name of the layer
	 * @param features the features that compose the layer
	 * @param selectable <code>true</code> if the layer can be selected and <code>false</code> otherwise
	 * @param selected <code>true</code> if the layer is currently selected and <code>false</code> otherwise
	 * @param displayable <code>true</code> if the layer can be displayed and <code>false</code> otherwise
	 * @param displaying <code>true</code> if the layer is currently displayed and <code>false</code> otherwise
	 */
	public JImageFeatureLayer(String name, Collection<JImageFeature> features, boolean selectable, boolean selected, boolean displayable, boolean displaying) {
		setName(name);
		setStateSelectable(selectable);
		setStateSelected(selectable);
		setStateDisplayable(displayable);
		setStateDisplaying(displaying);	

		if ((features != null) && (features.size() > 0)){
			this.features = new ArrayList<JImageFeature>(features);
		} else {
			this.features = new LinkedList<JImageFeature>();
		}

		this.layerListeners = new LinkedList<JImageFeatureLayerListener>();
	}

	/**
	 * Get the {@link JImageFeature image features} that are attached to this layer.
	 * @return the {@link JImageFeature image feature} that are attached to this layer
	 * @see #setImageFeatures(Collection)
	 */
	public List<JImageFeature> getImageFeatures(){
		return this.features;
	}

	/**
	 * Set the {@link JImageFeature image features} to attach to to this layer.
	 * @param features the {@link JImageFeature image features} to attach to to this layer
	 * @see #getImageFeatures()
	 */
	public void setImageFeatures(Collection<JImageFeature> features) {

		if (this.features.size() > 0) {

			for(JImageFeature feature : this.features) {
				feature.setImageFeatureLayer(null);
			}

			this.features.clear();
			fireOnImageFeaturesRemoved(this.features);
		}

		if (features != null) {
			boolean b = this.features.addAll(features);

			if (b) {
				for(JImageFeature feature : features) {
					feature.setImageFeatureLayer(this);
				}
				fireOnImageFeaturesAdded(features);
			}


		}
	}

	/**
	 * Add the given {@link JImageFeature image feature} to this layer.
	 * @param feature the feature to add
	 * @return <code>true</code> if the feature is successfully added and <code>false</code> otherwise
	 * @see #removeImageFeature(JImageFeature)
	 */
	public boolean addImageFeature(JImageFeature feature) {
		if (feature == null) {
			return false;
		}

		boolean b =  this.features.add(feature);

		if (b) {
			feature.setImageFeatureLayer(this);
			fireOnImageFeatureAdded(feature);
		}

		return b;
	}

	/**
	 * Add the given {@link JImageFeature image features} to this layer.
	 * @param features the features to add
	 * @return <code>true</code> if the features are successfully added and <code>false</code> otherwise
	 * @see #removeImageFeatures(Collection)
	 */
	public boolean addImageFeatures(Collection<JImageFeature> features) {
		if (features == null) {
			return false;
		}

		boolean b =  features.addAll(features);

		if (b) {
			for(JImageFeature feature : features) {
				feature.setImageFeatureLayer(this);
			}
			fireOnImageFeaturesAdded(features);
		}

		return b;
	}

	/**
	 * Remove the given {@link JImageFeature image feature} from this layer.
	 * @param feature the feature to remove
	 * @return <code>true</code> if the feature is successfully removed and <code>false</code> otherwise
	 * @see #addImageFeature(JImageFeature)
	 */
	public boolean removeImageFeature(JImageFeature feature) {
		if (feature == null) {
			return false;
		}

		boolean b =  this.features.remove(feature);

		if (b) {
			feature.setImageFeatureLayer(null);
			fireOnImageFeatureRemoved(feature);
		}

		return b;
	}

	/**
	 * Remove the given {@link JImageFeature image features} from this layer.
	 * @param features the features to remove
	 * @return <code>true</code> if the features are successfully removed and <code>false</code> otherwise
	 * @see #addImageFeatures(Collection)
	 */
	public boolean removeImageFeatures(Collection<JImageFeature> features) {
		if (features == null) {
			return false;
		}

		boolean b =  features.removeAll(features);

		if (b) {
			for(JImageFeature feature : features) {
				feature.setImageFeatureLayer(null);
			}
			fireOnImageFeaturesRemoved(features);
		}

		return b;
	}

	/**
	 * Add the given {@link JImageFeatureLayerListener layer listener} to this layer.
	 * @param listener the listener to add
	 * @return <code>true</code> if the listener is successfully added and <code>false</code> otherwise
	 * @see #removeImageFeatureLayerListener(JImageFeatureLayerListener)
	 */
	public boolean addImageFeatureLayerListener(JImageFeatureLayerListener listener) {

		if (listener == null) {
			return false;
		}

		if (this.layerListeners.contains(listener)) {
			return false;
		}

		return this.layerListeners.add(listener);
	}

	/**
	 * Remove the given {@link JImageFeatureLayerListener layer listener} from this layer.
	 * @param listener the listener to remove
	 * @return <code>true</code> if the listener is successfully removed and <code>false</code> otherwise
	 * @see #addImageFeatureLayerListener(JImageFeatureLayerListener)
	 */
	public boolean removeImageFeatureLayerListener(JImageFeatureLayerListener listener) {
		if (listener == null) {
			return false;
		}

		return this.layerListeners.remove(listener);
	}

	/**
	 * Fire an {@link JImageFeatureLayerListener#onImageFeatureAdded(JImageFeatureLayer, JImageFeature) onImageFeatureAdded} callback on all registered {@link JImageFeatureLayerListener listener}.
	 * @param feature the feature involved with the callback
	 */
	protected void fireOnImageFeatureAdded(JImageFeature feature) {
		for(JImageFeatureLayerListener listener : this.layerListeners) {
			listener.onImageFeatureAdded(this, feature);
		}
	}

	/**
	 * Fire an {@link JImageFeatureLayerListener#onImageFeatureRemoved(JImageFeatureLayer, JImageFeature) onImageFeatureRemoved} callback on all registered {@link JImageFeatureLayerListener listener}.
	 * @param feature the feature involved with the callback
	 */
	protected void fireOnImageFeatureRemoved(JImageFeature feature) {
		for(JImageFeatureLayerListener listener : this.layerListeners) {
			listener.onImageFeatureRemoved(this, feature);
		}
	}

	/**
	 * Fire an {@link JImageFeatureLayerListener#onImageFeaturesAdded(JImageFeatureLayer, Collection) onImageFeaturesAdded} callback on all registered {@link JImageFeatureLayerListener listener}.
	 * @param features the features involved with the callback
	 */
	protected void fireOnImageFeaturesAdded(Collection<JImageFeature> features) {
		for(JImageFeatureLayerListener listener : this.layerListeners) {
			listener.onImageFeaturesAdded(this, features);
		}
	}

	/**
	 * Fire an {@link JImageFeatureLayerListener#onImageFeaturesRemoved(JImageFeatureLayer, Collection) onImageFeaturesRemoved} callback on all registered {@link JImageFeatureLayerListener listener}.
	 * @param features the features involved with the callback
	 */
	protected void fireOnImageFeaturesRemoved(Collection<JImageFeature> features) {
		for(JImageFeatureLayerListener listener : this.layerListeners) {
			listener.onImageFeaturesRemoved(this, features);
		}
	}

	/**
	 * This method can be called by an attached {@link JImageFeature image feature} when its modified.
	 * @param feature the modified feature (the one that call this method)
	 */
	public void onImageFeatureModified(JImageFeature feature) {
		for(JImageFeatureLayerListener listener : this.layerListeners) {
			listener.onImageFeatureModified(this, this.features);
		}
	}
}
