package org.jorigin.jfx;

import java.util.Collection;

/**
 * A listener that enables to monitor {@link JImageFeatureLayer image feature layer} that can be displayed within a {@link JImageCanvas JImageCanvas}.
 * @author Julien Seinturier - IVM Technologies - http://www.seinturier.fr
 * @version 1.0.0
 */
public interface JImageFeatureLayerListener {

	/**
	 * A callback method that is triggered when a {@link JImageFeature feature} has been added to the listened {@link JImageFeatureLayer layer}.
	 * @param layer the layer that holds the event
	 * @param feature the feature that has been added
	 */
	public void onImageFeatureAdded(JImageFeatureLayer layer, JImageFeature feature);
	
	/**
	 * A callback method that is triggered when a {@link JImageFeature feature} has been removed from the listened {@link JImageFeatureLayer layer}.
	 * @param layer the layer that holds the event
	 * @param feature the feature that has been removed
	 */
	public void onImageFeatureRemoved(JImageFeatureLayer layer, JImageFeature feature);
	
	/**
	 * A callback method that is triggered when a {@link JImageFeature feature} has been modified from the listened {@link JImageFeatureLayer layer}.
	 * @param layer  the layer that holds the event
	 * @param feature the feature that has been removed
	 */
	public void onImageFeatureModified(JImageFeatureLayer layer, JImageFeature feature);
	
	/**
	 * A callback method that is triggered when a collection of {@link JImageFeature feature} has been added to the listened {@link JImageFeatureLayer layer}.
	 * @param layer the layer that holds the event
	 * @param features the collection of feature that have been added
	 */
    public void onImageFeaturesAdded(JImageFeatureLayer layer, Collection<JImageFeature> features);
	
    /**
     * A callback method that is triggered when a collection of {@link JImageFeature feature} has been removed from the listened {@link JImageFeatureLayer layer}.
     * @param layer the layer that holds the event
     * @param features  the collection of feature that have been removed
     */
	public void onImageFeaturesRemoved(JImageFeatureLayer layer, Collection<JImageFeature> features);
	
	/**
	 * A callback method that is triggered when a collection of {@link JImageFeature feature} has been modified from the listened {@link JImageFeatureLayer layer}.
	 * @param layer  the layer that holds the event
	 * @param features the collection of feature that has been removed
	 */
	public void onImageFeatureModified(JImageFeatureLayer layer, Collection<JImageFeature> features);
}
