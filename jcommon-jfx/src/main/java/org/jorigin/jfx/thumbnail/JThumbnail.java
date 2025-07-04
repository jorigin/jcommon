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
	
	/**
	 * The margin that is used to display the thumbnail image.
	 */
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
		
		this.imagePane = new StackPane();
		
		this.nameLB = new Label(this.name);
		
		setCenter(this.imagePane);
		
		if (style != null) {
			
		  setThumbnailStyle(style);
		
		  if (style.isThumbnailNameVisible()) {		  
		    this.nameLB.setBackground(style.getThumbnailNameBackground());
		    this.namePN = new BorderPane();
		    this.namePN.setCenter(this.nameLB);
		    setBottom(this.namePN);
		  }
		}
		
		this.imagePane.widthProperty().addListener((e) -> fitImage(this.imagePane.getWidth(), this.imagePane.getHeight()));		
		this.imagePane.heightProperty().addListener((e) -> fitImage(this.imagePane.getWidth(), this.imagePane.getHeight()));
		
		setThumbnailImage(image);

		this.selected.addListener((e) -> applyStyle());
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
		return this.content;
	}
	
	/**
	 * Set the image that has to be displayed within this thumbnail.
	 * @param image the image that has to be displayed within this thumbnail
	 */
	public void setThumbnailImage(Image image) {
		
		if (image == null) return;
		
		if ((this.view != null) && (this.view.getImage() == image)) return;
		
		ImageView newView = new ImageView(image);
		newView.setPreserveRatio(false);
		
		if (this.margin > 0) {
		  newView.setFitWidth(getPrefWidth() - 2.0d*this.margin);
		  newView.setFitHeight(getPrefHeight() - 2.0d*this.margin);
		}
		
        this.view = newView;
		this.view.setCache(true);
		this.view.setCacheHint(CacheHint.SPEED);
		
		Platform.runLater(() -> {this.imagePane.getChildren().clear();
		                         this.imagePane.getChildren().add(this.view);});
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
		
		if (this.style != null) {
			if (this.selected.get() == true) {
				this.imagePane.setBackground(this.style.getThumbnailSelectedImageBackground());
				this.nameLB.setBackground(this.style.getThumbnailSelectedNameBackground());
				backgroundProperty().set(this.style.thumbnailSelectedBackgroundProperty().get());
			} else {
				this.imagePane.setBackground(this.style.getThumbnailImageBackground());
				this.nameLB.setBackground(this.style.getThumbnailNameBackground());
				backgroundProperty().set(this.style.thumbnailBackgroundProperty().get());
			}
		}
	}
	
	/**
	 * Fit the thumbnail image to the specified value.
	 * @param width the desired width in pixels (px)
	 * @param height the desired height in pixels (px)
	 */
	private void fitImage(double width, double height) {
		
		if (this.view == null) return;
		
		if ((width <= 0) || (height <= 0)) return;
		
		this.view.setFitWidth(width - 2.0*this.margin);
		this.view.setFitHeight(height - 2.0*this.margin);
		
	}
}
