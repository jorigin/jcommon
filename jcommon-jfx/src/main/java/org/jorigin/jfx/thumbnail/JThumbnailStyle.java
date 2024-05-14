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
//TODO Implements JTjumbnailStyle selected name font property
public class JThumbnailStyle {

	// The property that set the {@link JThumbnail} width. By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailWidth = null;
	
	// The property that set the {@link JThumbnail} height. By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailHeight = null;
	
	// The property that set the horizontal spacing between {@link JThumbnail} within the {@link JThumbnailPane}.
	// By default this property is set to {@link Double#NaN}.
	private DoubleProperty thumbnailHGap = null;
	
	 // The property that set the vertical spacing between {@link JThumbnail} within the {@link JThumbnailPane}.
	 // By default this property is set to {@link Double#NaN}
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
	// @see #thumbnailFocusedBackground
	// @see #thumbnailSelectedBackground
	private ObjectProperty<Background> thumbnailBackground = null;
	
	// The property that set the {@link JThumbnailPane} background. 
	private ObjectProperty<Background> thumbnailPaneBackground = null;
		
//////////////////////////////////////////////////////////////////
// FOCUSED STATE PROPERTIES
	 
	// The property that set a focused {@link JThumbnail} global background. 
	// @see #thumbnailBackground
	// @see #thumbnailSelectedBackground
	private ObjectProperty<Background> thumbnailFocusedBackground = null;
//////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////
// SELECTED STATE PROPERTIES
	
	// The property that set a selected {@link JThumbnail} global background.
	// By default, this property holds the {@link Background#EMPTY} value.
	// @see #thumbnailBackground
	// @see #thumbnailFocusedBackground
	private ObjectProperty<Background> thumbnailSelectedBackground = null;
	
	// The property that set the {@link JThumbnail} image background when it is selected.
	// By default, this property holds the {@link Background#EMPTY} value.
	private ObjectProperty<Background> thumbnailSelectedImageBackground = null;
	
	// The property that set the {@link JThumbnail} name background when it is selected.
	//  By default, this property holds the {@link Background#EMPTY} value.
	private ObjectProperty<Background> thumbnailSelectedNameBackground = null;
//////////////////////////////////////////////////////////////////
	
	/**
	 * Create a new set of parameters that enable to control {@link JThumbnailPane} and {@link JThumbnail} display and layout.
	 */
	public JThumbnailStyle() {

		thumbnailWidth = new SimpleDoubleProperty(this, "thumbnail_width", Double.NaN);
		thumbnailHeight = new SimpleDoubleProperty(this, "thumbnail_height", Double.NaN);
		
		thumbnailNameVisibility = new SimpleBooleanProperty(this, "thumbnail_name_visibility", Boolean.TRUE);
		
		thumbnailImageBackground = new SimpleObjectProperty<Background>(this, "thumbnail_image_background", Background.EMPTY);
		thumbnailNameBackground = new SimpleObjectProperty<Background>(this, "thumbnail_name_background", Background.EMPTY);
		thumbnailBackground = new SimpleObjectProperty<Background>(this, "thumbnail_background", Background.EMPTY);

		thumbnailFocusedBackground = new SimpleObjectProperty<Background>(this, "thumbnail_focused", Background.EMPTY);
		
		thumbnailSelectedImageBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_image_background", Background.EMPTY);
		thumbnailSelectedNameBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_name_background", Background.EMPTY);
		thumbnailSelectedBackground = new SimpleObjectProperty<Background>(this, "thumbnail_selected_background", Background.EMPTY);
		
		thumbnailHGap = new SimpleDoubleProperty(this, "thumbnail_hgap", Double.NaN);
		thumbnailVGap = new SimpleDoubleProperty(this, "thumbnail_vgap", Double.NaN);
		
		thumbnailPaneBackground = new SimpleObjectProperty<Background>(this, "thumbnail_pane_background", Background.EMPTY);
	}
	
	/**
	 * Get the thumbnail width property. This property describe the desired width of a {@link JThumbnail}.
	 * @return the thumbnail width property
	 * @see #getThumbnailWidth()
	 * @see #setThumbnailWidth(double)
	 */
	public DoubleProperty thumbnailWidthProperty() {
		return thumbnailWidth;
	}
	
	/**
	 * Get the desired width for the {@link JThumbnail} instances.
	 * @return the desired width for the {@link JThumbnail} instances
	 * @see #setThumbnailWidth(double)
	 * @see #thumbnailWidthProperty()
	 */
	public double getThumbnailWidth() {
		return thumbnailWidth.getValue().doubleValue();
	}
	
	/**
	 * Set the desired width for the {@link JThumbnail} instances.
	 * @param value the desired width for the {@link JThumbnail} instances
	 * @see #getThumbnailWidth()
	 * @see #thumbnailWidthProperty()
	 */
	public void setThumbnailWidth(double value) {
		thumbnailWidth.set(value);
	}
	
	/**
	 * Get the thumbnail height property. 
	 * This property describe the desired height of a {@link JThumbnail}.
	 * @return the thumbnail height property
	 * @see #getThumbnailHeight()
	 * @see #setThumbnailHeight(double)
	 */
	public DoubleProperty thumbnailHeightProperty() {
		return thumbnailHeight;
	}
	
	/**
	 * Get the desired height for the {@link JThumbnail} instances.
	 * @return the desired height for the {@link JThumbnail} instances
	 * @see #setThumbnailHeight(double)
	 * @see #thumbnailHeightProperty()
	 */
	public double getThumbnailHeight() {
		return thumbnailHeight.getValue().doubleValue();
	}
	
	/**
	 * Set the desired height for the {@link JThumbnail} instances.
	 * @param value the desired height for the {@link JThumbnail} instances
	 * @see #getThumbnailWidth()
	 * @see #thumbnailWidthProperty()
	 */
	public void setThumbnailHeight(double value) {
		thumbnailHeight.set(value);
	}
	
	/**
	 * Get the thumbnail image background property.
	 * @return the thumbnail image background property
	 * @see #getThumbnailImageBackground()
	 * @see #setThumbnailImageBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailImageBackgroundProperty(){
		return thumbnailImageBackground;
	}
	
	/**
	 * Get the thumbnail image background. 
	 * @return the thumbnail image background
	 * @see #setThumbnailImageBackground(Background)
	 * @see #thumbnailImageBackgroundProperty()
	 */
	public Background getThumbnailImageBackground() {
		return thumbnailImageBackground.get();
	}
	
	/**
	 * Set the thumbnail image background.
	 * @param background the thumbnail image background
	 * @see #getThumbnailImageBackground()
	 * @see #thumbnailImageBackground
	 */
	public void setThumbnailImageBackground(Background background) {
		thumbnailImageBackground.set(background);
	}
	
	/**
	 * Get the thumbnail name background property.
	 * @return the thumbnail name background property
	 * @see #getThumbnailNameBackground()
	 * @see #setThumbnailNameBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailNameBackgroundProperty(){
		return thumbnailNameBackground;
	}

	/**
	 * Get the thumbnail name background. 
	 * @return the thumbnail name background
	 * @see #setThumbnailNameBackground(Background)
	 * @see #thumbnailNameBackgroundProperty()
	 */
	public Background getThumbnailNameBackground() {
		return thumbnailNameBackground.get();
	}
	
	/**
	 * Set the thumbnail name background.
	 * @param background the thumbnail name background
	 * @see #getThumbnailNameBackground()
	 * @see #thumbnailNameBackground
	 */
	public void setThumbnailNameBackground(Background background) {
		thumbnailNameBackground.set(background);
	}
	
	/**
	 * Get the thumbnail global background property.
	 * @return the thumbnail global background property
	 * @see #getThumbnailBackground()
	 * @see #setThumbnailBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailBackgroundProperty(){
		return thumbnailBackground;
	}
	
	/**
	 * Get the thumbnail global background.
	 * @return the thumbnail global background
	 * @see #setThumbnailBackground(Background)
	 * @see #thumbnailBackground
	 */
	public Background getThumbnailBackground() {
		return thumbnailBackground.get();
	}
	
	/**
	 * Set the thumbnail global background.
	 * @param background the thumbnail global background
	 * @see #getThumbnailBackground()
	 * @see #thumbnailBackground
	 */
	public void setThumbnailBackground(Background background) {
		thumbnailBackground.set(background);
	}
	
	/**
	 * Get a focused thumbnail global background property.
	 * @return the focused thumbnail global background property
	 * @see #getThumbnailFocusedBackground()
	 * @see #setThumbnailFocusedBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailFocusedBackgroundProperty(){
		return thumbnailFocusedBackground;
	}
	
	/**
	 * Get a focused thumbnail global background.
	 * @return the focused thumbnail global background
	 * @see #setThumbnailFocusedBackground(Background)
	 * @see #thumbnailFocusedBackground
	 */
	public Background getThumbnailFocusedBackground() {
		return thumbnailFocusedBackground.get();
	}
	
	/**
	 * Set a focused thumbnail global background.
	 * @param background the focused thumbnail global background
	 * @see #getThumbnailFocusedBackground()
	 * @see #thumbnailFocusedBackground
	 */
	public void setThumbnailFocusedBackground(Background background) {
		thumbnailFocusedBackground.set(background);
	}
	
	/**
	 * Get the selected thumbnail image background property.
	 * @return the selected thumbnail image background property
	 * @see #getThumbnailSelectedImageBackground()
	 * @see #setThumbnailSelectedImageBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailSelectedImageBackgroundProperty(){
		return thumbnailSelectedImageBackground;
	}
	
	/**
	 * Get the selected thumbnail image background. 
	 * @return the selected thumbnail image background
	 * @see #setThumbnailSelectedImageBackground(Background)
	 * @see #thumbnailSelectedImageBackgroundProperty()
	 */
	public Background getThumbnailSelectedImageBackground() {
		return thumbnailSelectedImageBackground.get();
	}
	
	/**
	 * Set the selected thumbnail image background.
	 * @param background the selected thumbnail image background
	 * @see #getThumbnailSelectedImageBackground()
	 * @see #thumbnailSelectedImageBackground
	 */
	public void setThumbnailSelectedImageBackground(Background background) {
		thumbnailSelectedImageBackground.set(background);
	}
	
	/**
	 * Get a selected thumbnail global background property.
	 * @return the selected thumbnail global background property
	 * @see #getThumbnailSelectedBackground()
	 * @see #setThumbnailSelectedBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailSelectedBackgroundProperty(){
		return thumbnailSelectedBackground;
	}
	
	/**
	 * Get a selected thumbnail global background.
	 * @return the selected thumbnail global background
	 * @see #setThumbnailSelectedBackground(Background)
	 * @see #thumbnailSelectedBackground
	 */
	public Background getThumbnailSelectedBackground() {
		return thumbnailSelectedBackground.get();
	}
	
	/**
	 * Set a selected thumbnail global background.
	 * @param background the selected thumbnail global background
	 * @see #getThumbnailSelectedBackground()
	 * @see #thumbnailSelectedBackground
	 */
	public void setThumbnailSelectedBackground(Background background) {
		thumbnailSelectedBackground.set(background);
	}
	
	/**
	 * Get the thumbnail horizontal gap property. 
	 * This property describe the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @return the thumbnail horizontal gap property
	 * @see #getThumbnailHGap()
	 * @see #setThumbnailHGap(double)
	 */
	public DoubleProperty thumbnailHGapProperty() {
		return thumbnailHGap;
	}
	
	/**
	 * Get the horizontal spacing between {@link JThumbnail thumbnails}.
	 * @return the horizontal spacing between {@link JThumbnail thumbnails}.
	 * @see #setThumbnailHGap(double)
	 * @see #thumbnailHGapProperty()
	 */
	public double getThumbnailHGap() {
		return thumbnailHGap.getValue().doubleValue();
	}
	
	/**
	 * Set the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @param value the desired horizontal spacing between {@link JThumbnail thumbnails}.
	 * @see #getThumbnailHGap()
	 * @see #thumbnailHGap
	 */
	public void setThumbnailHGap(double value) {
		thumbnailHGap.set(value);
	}

	/**
	 * Get the thumbnail vertical gap property. 
	 * This property describe the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @return the thumbnail vertical gap property
	 * @see #getThumbnailVGap()
	 * @see #thumbnailVGap
	 */
	public DoubleProperty thumbnailVGapProperty() {
		return thumbnailVGap;
	}
	
	/**
	 * Get the vertical spacing between {@link JThumbnail thumbnails}.
	 * @return the vertical spacing between {@link JThumbnail thumbnails}.
	 * @see #setThumbnailVGap(double)
	 * @see #thumbnailVGap
	 */
	public double getThumbnailVGap() {
		return thumbnailVGap.getValue().doubleValue();
	}
	
	/**
	 * Set the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @param value the desired vertical spacing between {@link JThumbnail thumbnails}.
	 * @see #getThumbnailVGap()
	 * @see #thumbnailVGap
	 */
	public void setThumbnailVGap(double value) {
		thumbnailVGap.set(value);
	}
	
	/**
	 * Get the thumbnail pane background property.
	 * @return the thumbnail pane background property
	 * @see #getThumbnailPaneBackground()
	 * @see #setThumbnailPaneBackground(Background)
	 */
	public ObjectProperty<Background> thumbnailPaneBackgroundProperty(){
		return thumbnailPaneBackground;
	}
	
	/**
	 * Get the thumbnail pane background.
	 * @return the thumbnail pane background
	 * @see #setThumbnailPaneBackground(Background)
	 * @see #thumbnailPaneBackground
	 */
	public Background getThumbnailPaneBackground() {
		return thumbnailPaneBackground.get();
	}
	
	/**
	 * Set the thumbnail pane background.
	 * @param background the thumbnail pane background
	 * @see #getThumbnailPaneBackground()
	 * @see #thumbnailPaneBackground
	 */
	public void setThumbnailPaneBackground(Background background) {
		thumbnailPaneBackground.set(background);
	}
	
	/**
	 * Get the name visibility property. This property that set if {@link JThumbnail} name has to be visible.
	 * @return the show name property
	 * @see #isThumbnailNameVisible()
	 * @see #setThumbnailNameVisible(boolean)
	 */
	public BooleanProperty thumbnailNameVisibilityProperty() {
		return thumbnailNameVisibility;
	}

	/**
	 * Get if {@link JThumbnail} has to display its name. 
	 * @return <code>true</code> if a {@link JThumbnail} has to display its name and <code>false</code> otherwise
	 * @see #thumbnailNameVisibilityProperty()
	 * @see #setThumbnailNameVisible(boolean)
	 */
	public boolean isThumbnailNameVisible() {
		return thumbnailNameVisibility.get();
	}
	
	/**
	 * Set if a {@link JThumbnail} has to display its name. 
	 * @param show  <code>true</code> if a {@link JThumbnail} has to display its name and <code>false</code> otherwise
	 * @see #thumbnailNameVisibilityProperty()
	 * @see #isThumbnailNameVisible()
	 */
	public void setThumbnailNameVisible(boolean show) {
		thumbnailNameVisibility.set(show);
	}
	
	/**
	 * Get the thumbnail name background property when it is selected.
	 * @return the thumbnail name background property when it is selected
	 */
	public ObjectProperty<Background> thumbnailSelectedNameBackgroundProperty(){
		return thumbnailSelectedNameBackground;
	}
	
	/**
	 * Get the thumbnail name background when it is selected.
	 * @return the thumbnail name background when it is selected
	 * @see #setThumbnailSelectedNameBackground(Background)
	 * @see #thumbnailSelectedNameBackgroundProperty()
	 */
	public Background getThumbnailSelectedNameBackground() {
		return thumbnailSelectedNameBackground.get();
	}
	
	/**
	 * Set the thumbnail name background when it is selected.
	 * @param background the thumbnail name background when it is selected
	 * @see #getThumbnailSelectedNameBackground()
	 * @see #thumbnailSelectedNameBackground
	 */
	public void setThumbnailSelectedNameBackground(Background background) {
		thumbnailSelectedNameBackground.set(background);
	}
}
