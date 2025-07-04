package org.jorigin.jfx.thumbnail;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;

/**
 * A set of parameters that enable to control {@link JThumbnailPane} and {@link JThumbnail} display and layout.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 */
// TODO Implements JTjumbnailStyle name font property
// TODO Implements JTjumbnailStyle selected name font property
public class JThumbnailStyle {

	// The property that set the {@link JThumbnail} width. By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailWidth = null;
	
	// The property that set the {@link JThumbnail} height. By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailHeight = null;
	
	// The property that set the horizontal spacing between {@link JThumbnail} within the {@link JThumbnailPane}.
	// By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailHGap = null;
	
	// The property that set the vertical spacing between {@link JThumbnail} within the {@link JThumbnailPane}. 
	// By default this property is set to {@link Double#NaN}. 
	private DoubleProperty thumbnailVGap = null;
	
	// The property that set if {@link JThumbnail} name has to be displayed.
	// By default this property is set to <code>false</code>.
	private BooleanProperty thumbnailNameVisibility = null;	

	// The property that set the {@link JThumbnail} image background.
	// By default this property holds an {@link Background#EMPTY} background and thus, attached thumbnail use default settings.
	private ObjectProperty<Background> thumbnailImageBackground = null;
	
	// The property that set the {@link JThumbnail} name background.
	// By default this property holds an {@link Background#EMPTY} background and thus, attached thumbnail use default settings.
	private ObjectProperty<Background> thumbnailNameBackground = null;
	
	// The property that set the {@link JThumbnail} global background. 
	private ObjectProperty<Background> thumbnailBackground = null;
	
	// The property that set the {@link JThumbnailPane} background. 
	private ObjectProperty<Background> thumbnailPaneBackground = null;
			 
	// The property that set a focused {@link JThumbnail} global background. 
	private ObjectProperty<Background> thumbnailFocusedBackground = null;

	
	// The property that set a selected {@link JThumbnail} global background.
	// By default, this property holds the {@link Background#EMPTY} value.
	private ObjectProperty<Background> thumbnailSelectedBackground = null;
	
	// The property that set the {@link JThumbnail} image background when it is selected.
	// By default, this property holds the {@link Background#EMPTY} value.
	private ObjectProperty<Background> thumbnailSelectedImageBackground = null;
	
	// The property that set the {@link JThumbnail} name background when it is selected.
	// By default, this property holds the {@link Background#EMPTY} value.
	private ObjectProperty<Background> thumbnailSelectedNameBackground = null;
	
	/**
	 * Create a new set of parameters that enable to control {@link JThumbnailPane} and {@link JThumbnail} display and layout.
	 */
	public JThumbnailStyle() {

		this.thumbnailWidth = new SimpleDoubleProperty(this, "thumbnail_width", Double.NaN);
		this.thumbnailHeight = new SimpleDoubleProperty(this, "thumbnail_height", Double.NaN);
		
		this.thumbnailNameVisibility = new SimpleBooleanProperty(this, "thumbnail_name_visibility", Boolean.TRUE);
		
		this.thumbnailImageBackground = new SimpleObjectProperty<Background>(this, "thumbnail_image_background", Background.EMPTY);
		this.thumbnailNameBackground = new SimpleObjectProperty<Background>(this, "thumbnail_name_background", Background.EMPTY);
		this.thumbnailBackground = new SimpleObjectProperty<Background>(this, "thumbnail_background", Background.EMPTY);

		this.thumbnailFocusedBackground = new SimpleObjectProperty<Background>(this, "thumbnail_focused", Background.EMPTY);
		
		this.thumbnailSelectedImageBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_image_background", Background.EMPTY);
		this.thumbnailSelectedNameBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_name_background", Background.EMPTY);
		this.thumbnailSelectedBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_background", Background.EMPTY);
		
		this.thumbnailHGap = new SimpleDoubleProperty(this, "thumbnail_hgap", Double.NaN);
		this.thumbnailVGap = new SimpleDoubleProperty(this, "thumbnail_vgap", Double.NaN);
		
		this.thumbnailPaneBackground = new SimpleObjectProperty<Background>(this, "thumbnail_pane_background", Background.EMPTY);
	}
	
	/**
	 * Get the thumbnail width property. This property describe the desired width of a {@link JThumbnail}.
	 * @return the thumbnail width property
	 * @see #getThumbnailWidth()
	 * @see #setThumbnailWidth(double)
	 */
	public DoubleProperty thumbnailWidthProperty() {
		return this.thumbnailWidth;
	}
	
	/**
	 * Get the desired width for the {@link JThumbnail} instances.
	 * @return the desired width for the {@link JThumbnail} instances
	 * @see #setThumbnailWidth(double)
	 * @see #thumbnailWidthProperty()
	 */
	public double getThumbnailWidth() {
		return this.thumbnailWidth.getValue().doubleValue();
	}
	
	/**
	 * Set the desired width for the {@link JThumbnail} instances.
	 * @param value the desired width for the {@link JThumbnail} instances
	 * @see #getThumbnailWidth()
	 * @see #thumbnailWidthProperty()
	 */
	public void setThumbnailWidth(double value) {
		this.thumbnailWidth.set(value);
	}
	
	/**
	 * Get the thumbnail height property. 
	 * This property describe the desired height of a {@link JThumbnail}.
	 * @return the thumbnail height property
	 * @see #getThumbnailHeight()
	 * @see #setThumbnailHeight(double)
	 */
	public DoubleProperty thumbnailHeightProperty() {
		return this.thumbnailHeight;
	}
	
	/**
	 * Get the desired height for the {@link JThumbnail} instances.
	 * @return the desired height for the {@link JThumbnail} instances
	 * @see #setThumbnailHeight(double)
	 * @see #thumbnailHeightProperty()
	 */
	public double getThumbnailHeight() {
		return this.thumbnailHeight.getValue().doubleValue();
	}
	
	/**
	 * Set the desired height for the {@link JThumbnail} instances.
	 * @param value the desired height for the {@link JThumbnail} instances
	 * @see #getThumbnailWidth()
	 * @see #thumbnailWidthProperty()
	 */
	public void setThumbnailHeight(double value) {
		this.thumbnailHeight.set(value);
	}
	
	/**
	 * Get the thumbnail image background property.
	 * @return the thumbnail image background property
	 * @see #getThumbnailImageBackground()
	 * @see #setThumbnailImageBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailImageBackgroundProperty(){
		return this.thumbnailImageBackground;
	}
	
	/**
	 * Get the thumbnail image background. 
	 * @return the thumbnail image background
	 * @see #setThumbnailImageBackground(Background)
	 * @see #thumbnailImageBackgroundProperty()
	 */
	public Background getThumbnailImageBackground() {
		return this.thumbnailImageBackground.get();
	}
	
	/**
	 * Set the thumbnail image background.
	 * @param background the thumbnail image background
	 * @see #getThumbnailImageBackground()
	 */
	public void setThumbnailImageBackground(Background background) {
		this.thumbnailImageBackground.set(background);
	}
	
	/**
	 * Get the thumbnail name background property.
	 * @return the thumbnail name background property
	 * @see #getThumbnailNameBackground()
	 * @see #setThumbnailNameBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailNameBackgroundProperty(){
		return this.thumbnailNameBackground;
	}

	/**
	 * Get the thumbnail name background. 
	 * @return the thumbnail name background
	 * @see #setThumbnailNameBackground(Background)
	 * @see #thumbnailNameBackgroundProperty()
	 */
	public Background getThumbnailNameBackground() {
		return this.thumbnailNameBackground.get();
	}
	
	/**
	 * Set the thumbnail name background.
	 * @param background the thumbnail name background
	 * @see #getThumbnailNameBackground()
	 */
	public void setThumbnailNameBackground(Background background) {
		this.thumbnailNameBackground.set(background);
	}
	
	/**
	 * Get the thumbnail global background property.
	 * @return the thumbnail global background property
	 * @see #getThumbnailBackground()
	 * @see #setThumbnailBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailBackgroundProperty(){
		return this.thumbnailBackground;
	}
	
	/**
	 * Get the thumbnail global background.
	 * @return the thumbnail global background
	 * @see #setThumbnailBackground(Background)
	 */
	public Background getThumbnailBackground() {
		return this.thumbnailBackground.get();
	}
	
	/**
	 * Set the thumbnail global background.
	 * @param background the thumbnail global background
	 * @see #getThumbnailBackground()
	 */
	public void setThumbnailBackground(Background background) {
		this.thumbnailBackground.set(background);
	}
	
	/**
	 * Get a focused thumbnail global background property.
	 * @return the focused thumbnail global background property
	 * @see #getThumbnailFocusedBackground()
	 * @see #setThumbnailFocusedBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailFocusedBackgroundProperty(){
		return this.thumbnailFocusedBackground;
	}
	
	/**
	 * Get a focused thumbnail global background.
	 * @return the focused thumbnail global background
	 * @see #setThumbnailFocusedBackground(Background)
	 */
	public Background getThumbnailFocusedBackground() {
		return this.thumbnailFocusedBackground.get();
	}
	
	/**
	 * Set a focused thumbnail global background.
	 * @param background the focused thumbnail global background
	 * @see #getThumbnailFocusedBackground()
	 */
	public void setThumbnailFocusedBackground(Background background) {
		this.thumbnailFocusedBackground.set(background);
	}
	
	/**
	 * Get the selected thumbnail image background property.
	 * @return the selected thumbnail image background property
	 * @see #getThumbnailSelectedImageBackground()
	 * @see #setThumbnailSelectedImageBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailSelectedImageBackgroundProperty(){
		return this.thumbnailSelectedImageBackground;
	}
	
	/**
	 * Get the selected thumbnail image background. 
	 * @return the selected thumbnail image background
	 * @see #setThumbnailSelectedImageBackground(Background)
	 * @see #thumbnailSelectedImageBackgroundProperty()
	 */
	public Background getThumbnailSelectedImageBackground() {
		return this.thumbnailSelectedImageBackground.get();
	}
	
	/**
	 * Set the selected thumbnail image background.
	 * @param background the selected thumbnail image background
	 * @see #getThumbnailSelectedImageBackground()
	 */
	public void setThumbnailSelectedImageBackground(Background background) {
		this.thumbnailSelectedImageBackground.set(background);
	}
	
	/**
	 * Get a selected thumbnail global background property.
	 * @return the selected thumbnail global background property
	 * @see #getThumbnailSelectedBackground()
	 * @see #setThumbnailSelectedBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailSelectedBackgroundProperty(){
		return this.thumbnailSelectedBackground;
	}
	
	/**
	 * Get a selected thumbnail global background.
	 * @return the selected thumbnail global background
	 * @see #setThumbnailSelectedBackground(Background)
	 */
	public Background getThumbnailSelectedBackground() {
		return this.thumbnailSelectedBackground.get();
	}
	
	/**
	 * Set a selected thumbnail global background.
	 * @param background the selected thumbnail global background
	 * @see #getThumbnailSelectedBackground()
	 */
	public void setThumbnailSelectedBackground(Background background) {
		this.thumbnailSelectedBackground.set(background);
	}
	
	/**
	 * Get the thumbnail horizontal gap property. 
	 * This property describe the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @return the thumbnail horizontal gap property
	 * @see #getThumbnailHGap()
	 * @see #setThumbnailHGap(double)
	 */
	public DoubleProperty thumbnailHGapProperty() {
		return this.thumbnailHGap;
	}
	
	/**
	 * Get the horizontal spacing between {@link JThumbnail thumbnails}.
	 * @return the horizontal spacing between {@link JThumbnail thumbnails}.
	 * @see #setThumbnailHGap(double)
	 * @see #thumbnailHGapProperty()
	 */
	public double getThumbnailHGap() {
		return this.thumbnailHGap.getValue().doubleValue();
	}
	
	/**
	 * Set the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @param value the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @see #getThumbnailHGap()
	 */
	public void setThumbnailHGap(double value) {
		this.thumbnailHGap.set(value);
	}

	/**
	 * Get the thumbnail vertical gap property. 
	 * This property describe the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @return the thumbnail vertical gap property
	 * @see #getThumbnailVGap()
	 */
	public DoubleProperty thumbnailVGapProperty() {
		return this.thumbnailVGap;
	}
	
	/**
	 * Get the vertical spacing between {@link JThumbnail thumbnails}.
	 * @return the vertical spacing between {@link JThumbnail thumbnails}.
	 * @see #setThumbnailVGap(double)
	 */
	public double getThumbnailVGap() {
		return this.thumbnailVGap.getValue().doubleValue();
	}
	
	/**
	 * Set the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @param value the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @see #getThumbnailVGap()
	 */
	public void setThumbnailVGap(double value) {
		this.thumbnailVGap.set(value);
	}
	
	/**
	 * Get the thumbnail pane background property.
	 * @return the thumbnail pane background property
	 * @see #getThumbnailPaneBackground()
	 * @see #setThumbnailPaneBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailPaneBackgroundProperty(){
		return this.thumbnailPaneBackground;
	}
	
	/**
	 * Get the thumbnail pane background.
	 * @return the thumbnail pane background
	 * @see #setThumbnailPaneBackground(Background)
	 */
	public Background getThumbnailPaneBackground() {
		return this.thumbnailPaneBackground.get();
	}
	
	/**
	 * Set the thumbnail pane background.
	 * @param background the thumbnail pane background
	 * @see #getThumbnailPaneBackground()
	 */
	public void setThumbnailPaneBackground(Background background) {
		this.thumbnailPaneBackground.set(background);
	}
	
	/**
	 * Get the name visibility property. This property that set if {@link JThumbnail} name has to be visible.
	 * @return the show name property
	 * @see #isThumbnailNameVisible()
	 * @see #setThumbnailNameVisible(boolean)
	 */
	public BooleanProperty thumbnailNameVisibilityProperty() {
		return this.thumbnailNameVisibility;
	}

	/**
	 * Get if {@link JThumbnail} has to display its name. 
	 * @return <code>true</code> if a {@link JThumbnail} has to display its name and <code>false</code> otherwise
	 * @see #thumbnailNameVisibilityProperty()
	 * @see #setThumbnailNameVisible(boolean)
	 */
	public boolean isThumbnailNameVisible() {
		return this.thumbnailNameVisibility.get();
	}
	
	/**
	 * Set if a {@link JThumbnail} has to display its name. 
	 * @param show  <code>true</code> if a {@link JThumbnail} has to display its name and <code>false</code> otherwise
	 * @see #thumbnailNameVisibilityProperty()
	 * @see #isThumbnailNameVisible()
	 */
	public void setThumbnailNameVisible(boolean show) {
		this.thumbnailNameVisibility.set(show);
	}
	
	/**
	 * Get the thumbnail name background property when it is selected.
	 * @return the thumbnail name background property when it is selected
	 */
	public ObjectProperty<Background> thumbnailSelectedNameBackgroundProperty(){
		return this.thumbnailSelectedNameBackground;
	}
	
	/**
	 * Get the thumbnail name background when it is selected.
	 * @return the thumbnail name background when it is selected
	 * @see #setThumbnailSelectedNameBackground(Background)
	 * @see #thumbnailSelectedNameBackgroundProperty()
	 */
	public Background getThumbnailSelectedNameBackground() {
		return this.thumbnailSelectedNameBackground.get();
	}
	
	/**
	 * Set the thumbnail name background when it is selected.
	 * @param background the thumbnail name background when it is selected
	 * @see #getThumbnailSelectedNameBackground()
	 */
	public void setThumbnailSelectedNameBackground(Background background) {
		this.thumbnailSelectedNameBackground.set(background);
	}
}
