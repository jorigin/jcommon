package org.jorigin.jfx.thumbnail;

import org.jorigin.identification.Named;
import org.jorigin.state.HandleSelection;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * A class that represents a thumbnail. A Thumbnail enable to display a name and an image that represent an underlying content. 
 * A thumbnail can be {@link JThumbnailStyle styled} and placed inside a {@link JThumbnailPane thumbnail pane} to visually manage a collection of content.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 *
 * @param <T> the type of the underlying content
 */
public class JThumbnail<T> extends BorderPane implements Named, HandleSelection {

	/** The thumbnail name */
	private String name = null;
	
	/** The thumbnail content */
	private T content = null;
	
	/** The thumbnail image view */
	private ImageView view;
	
	/** The thumbnaii limage pane */
	private StackPane imagePane = null;
	
	/** The thumbnail name label */
	private Label nameLB = null;
	
	/** The thumbnail name pane */
	private BorderPane namePN = null;
	
	/** The thumbnail style */
	private JThumbnailStyle style = null;
	
	/** The property that describe if the thumbnail is selected */
	private BooleanProperty selected = new SimpleBooleanProperty(false);
	
	/** The property that describe if the thumbnail is selectable */
	private BooleanProperty selectable = new SimpleBooleanProperty(true);
	
	//TODO JThumbnail - replace margin by border or insets
	private double margin = 5;
	
	/**
	 * Create a new thumbnail that represents the given <code>content</code> by displaying the given <code>image</code> and the given <code>name</code>. 
	 * The thumbnail style is defined by the given <code>style</code>
	 * @param name the name of the thumbnail
	 * @param content the underlying content that is represented by this thumbnail
	 * @param image the image to display
	 * @param style the style of the thumbnail
	 */
	public JThumbnail(String name, T content, Image image, JThumbnailStyle style) {
		
		super();

		getStyleClass().setAll("list-cell");

		this.content = content;
		
		this.name = name;
		
		imagePane = new StackPane();
		
		nameLB = new Label(this.name);
		
		setCenter(imagePane);
		
		if (style != null) {
			
		  setThumbnailStyle(style);
		
		  if (style.isThumbnailNameVisible()) {		  
		    nameLB.setBackground(style.getThumbnailNameBackground());
		    namePN = new BorderPane();
		    namePN.setCenter(nameLB);
		    setBottom(namePN);
		  }
		}
		
		imagePane.widthProperty().addListener((e) -> fitImage(imagePane.getWidth(), imagePane.getHeight()));		
		imagePane.heightProperty().addListener((e) -> fitImage(imagePane.getWidth(), imagePane.getHeight()));
		
		setThumbnailImage(image);

		selected.addListener((e) -> applyStyle());
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isStateSelected() {
		return this.selected.get();
	}

	@Override
	public void setStateSelected(boolean selected) {
		this.selected.set(selected);
	}

	@Override
	public boolean isStateSelectable() {
		return this.selectable.get();
	}

	@Override
	public void setStateSelectable(boolean selectable) {
		this.selectable.set(selectable);		
	}
	
	/**
	 * Get the content that is associated to this thumbnail.
	 * @return the content that is associated to this thumbnail
	 */
	public T getThumbnailContent() {
		return content;
	}
	
	/**
	 * Set the image that has to be displayed within this thumbnail.
	 * @param image the image that has to be displayed within this thumbnail
	 */
	public void setThumbnailImage(Image image) {
		
		if (image == null) return;
		
		if ((view != null) && (view.getImage() == image)) return;
		
		ImageView newView = new ImageView(image);
		newView.setPreserveRatio(false);
		
		if (margin > 0) {
		  newView.setFitWidth(getPrefWidth() - 2.0d*margin);
		  newView.setFitHeight(getPrefHeight() - 2.0d*margin);
		}
		
        this.view = newView;
		this.view.setCache(true);
		this.view.setCacheHint(CacheHint.SPEED);
		
		Platform.runLater(() -> {imagePane.getChildren().clear();
		                         imagePane.getChildren().add(view);});
	}
	
	/**
	 * Set the parameters that this thumbnail has to use for display and layout.
	 * @param style the parameters that this thumbnail has to use for display and layout
	 */
	public void setThumbnailStyle(JThumbnailStyle style) {
		
		if (this.style == style) return;
		
		// Clear current parameters bindings
		if (this.style != null) {
			minWidthProperty().unbind();
			maxWidthProperty().unbind();
			prefWidthProperty().unbind();
			
			minHeightProperty().unbind();
			maxHeightProperty().unbind();
			prefHeightProperty().unbind();
		}
		
		this.style = style;

		// Set style bindings
		if (this.style != null) {
			
			if (style.thumbnailWidthProperty() != null){
				minWidthProperty().bind(style.thumbnailWidthProperty());			
				maxWidthProperty().bind(style.thumbnailWidthProperty());				
				prefWidthProperty().bind(style.thumbnailWidthProperty());
			}
			
			if (style.thumbnailHeightProperty() != null) {
				minHeightProperty().bind(style.thumbnailHeightProperty());
				maxHeightProperty().bind(style.thumbnailHeightProperty());
				prefHeightProperty().bind(style.thumbnailHeightProperty());
			}
			
			applyStyle();
		}
	}
	
	/**
	 * Apply the style to the thumbnail. This method is called when the thumbnail style is modified or when the thumbnail state is modified.
	 */
	private void applyStyle() {		
		
		if (style != null) {
			if (selected.get() == true) {
				imagePane.setBackground(style.getThumbnailSelectedImageBackground());
				nameLB.setBackground(style.getThumbnailSelectedNameBackground());
				backgroundProperty().set(style.thumbnailSelectedBackgroundProperty().get());
			} else {
				imagePane.setBackground(style.getThumbnailImageBackground());
				nameLB.setBackground(style.getThumbnailNameBackground());
				backgroundProperty().set(style.thumbnailBackgroundProperty().get());
			}
		}
	}
	
	/**
	 * Fit the thumbnail image to the specified value.
	 * @param width the desired width in pixels (px)
	 * @param height the desired height in pixels (px)
	 */
	private void fitImage(double width, double height) {
		
		if (view == null) return;
		
		if ((width <= 0) || (height <= 0)) return;
		
		view.setFitWidth(width - 2.0*margin);
		view.setFitHeight(height - 2.0*margin);
		
	}
}
