package org.jorigin.jfx;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;

import org.jorigin.Common;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;

/**
 * A JavaFX canvas that can display images.
 * @author Julien Seinturier - IVM Technologies - http://www.seinturier.fr
 */
public class JImageCanvas extends Canvas implements JImageFeatureLayerListener{

	/**
	 * The image feature selection mode property name. 
	 * This name enable to identify the property when a {@link java.beans.PropertyChangeEvent property change event} is fired from this object.
	 */
	public static final String MODE_SELECTION_PROPERTY = "MODE_SELECTION";

	/**
	 * The image feature scale property name.
	 * This name enable to identify the property when a {@link java.beans.PropertyChangeEvent property change event} is fired from this object.
	 */
	public static final String SCALE_PROPERTY          = "SCALE";

	/**
	 * No selection mode activated.
	 * @see #MODE_SELECTION_POINT
	 * @see #MODE_SELECTION_RECTANGLE
	 * @see #MODE_SELECTION_POLYGON
	 */
	public static final int MODE_SELECTION_NONE = 0;
	
	/**
	 * The selection with point mode. This mode enable to select features on the image using a point.
	 * @see #MODE_SELECTION_NONE
	 * @see #MODE_SELECTION_RECTANGLE
	 * @see #MODE_SELECTION_POLYGON
	 */
	public static final int MODE_SELECTION_POINT = 1;

	/**
	 * The selection with rectangle mode. This mode enable to select features on the image using a rectangle.
	 * @see #MODE_SELECTION_NONE
	 * @see #MODE_SELECTION_POINT
	 * @see #MODE_SELECTION_POLYGON
	 */
	public static final int MODE_SELECTION_RECTANGLE  = 2;

	/**
	 * The selection with polygon mode. This mode enable to select features on the image using a closed polygon.
	 * @see #MODE_SELECTION_NONE
	 * @see #MODE_SELECTION_POINT
	 * @see #MODE_SELECTION_RECTANGLE
	 */
	public static final int MODE_SELECTION_POLYGON = 4;

	/**
	 * A mask that enable to check if a selection is active.
	 * @see #MODE_SELECTION_POINT
	 * @see #MODE_SELECTION_RECTANGLE
	 * @see #MODE_SELECTION_POLYGON
	 */
	public static final int MODE_SELECTION_ACTIVE_MASK = MODE_SELECTION_POINT | MODE_SELECTION_RECTANGLE | MODE_SELECTION_POLYGON;
	
	/**
	 * No fit is applied to the image when displayed.
	 * @see #FIT_COMPLETE
	 * @see #FIT_WIDTH
	 * @see #FIT_HEIGHT
	 * @see #FIT_AUTO
	 */
	public static final int FIT_NONE     = 0;

	/**
	 * Image is scaled in x and Y in order to fit the canvas. Original ratio is modified.
	 * @see #FIT_NONE
	 * @see #FIT_WIDTH
	 * @see #FIT_HEIGHT
	 * @see #FIT_AUTO
	 */
	public static final int FIT_COMPLETE = 1;

	/**
	 * Image is scaled in order to the image width is fitting the canvas width. Original ratio is preserved.
	 * @see #FIT_NONE
	 * @see #FIT_COMPLETE
	 * @see #FIT_HEIGHT
	 * @see #FIT_AUTO
	 */
	public static final int FIT_WIDTH    = 2;

	/**
	 * Image is scaled in order to the image height is fitting the canvas height. Original ratio is preserved.
	 * @see #FIT_NONE
	 * @see #FIT_COMPLETE
	 * @see #FIT_WIDTH
	 * @see #FIT_AUTO
	 */
	public static final int FIT_HEIGHT   = 3;

	/**
	 * Image is scaled in order to fit longest dimension. Original ratio is preserved.
	 * @see #FIT_NONE
	 * @see #FIT_COMPLETE
	 * @see #FIT_WIDTH
	 * @see #FIT_HEIGHT
	 */
	public static final int FIT_AUTO     = 4;

	/**
	 * The displayed image.
	 */
	private Image image = null;

	/**
	 * The image fit method. Can be {@link #FIT_NONE}, {@link #FIT_COMPLETE}, {@link #FIT_WIDTH}, {@link #FIT_HEIGHT} or {@link #FIT_AUTO} (default).
	 */
	private IntegerProperty imageFitMethod = null;

	/**
	 * The selection mode that is used. Can be {@link #MODE_SELECTION_POINT}, {@link #MODE_SELECTION_RECTANGLE} or {@link #MODE_SELECTION_POLYGON}.
	 */
	private IntegerProperty selectionMode = null;

	/**
	 * The transform applied to the canvas.
	 */
	private Affine transform;
	
	/**
	 * The scale factor applied to the image.
	 */
	private DoubleProperty scale = null;

	/**
	 * The layers that are displayed within the canvas.
	 */
	private List<JImageFeatureLayer> layers;

	private Shape selectionShape = null;
	
	private Point2D selectionOrigin = null;
	
	private Paint selectionShapeStroke = null;

	private Paint selectionShapeFill = null;
	
	private ObjectProperty<Point2D> cursorPosition;

	private BooleanProperty autoRepaint;

	private BooleanProperty autoFit;

	private BooleanProperty needRefresh;

	private BooleanProperty listeningControls;

	private DoubleProperty zoomFactor;

	private ObjectProperty<Paint> backgroundPaint;

	private BooleanProperty controlPrimaryActive;
	
	private BooleanProperty controlSecondaryActive;

	private BooleanProperty resizableProperty = new SimpleBooleanProperty(true);
	
	private SynchronousQueue<Runnable> refreshQueue;
	
	private Semaphore refreshLock = new Semaphore(1);
	
	// The following overrides enable the canvas to be resizable when contained
	// by a dynamic size container.
	@Override
	public double minHeight(double width){
	    return 0;
	}

	@Override
	public double maxHeight(double width){
	    return Double.MAX_VALUE;
	}

	@Override
	public double prefHeight(double width){
	    return minHeight(width);
	}

	@Override
	public double minWidth(double height){
	    return 0;
	}

	@Override
	public double maxWidth(double height){
	    return Double.MAX_VALUE;
	}

	@Override
	public void resize(double width, double height){
	    super.setWidth(width);
	    super.setHeight(height);
	}
	
	@Override
	public boolean isResizable() {
		return resizableProperty.get();
	}
	
	/**
	 * Set if the image canvas can be resized.
	 * @param resizable <code>true</code> if the image canvas has to be resizable (default) or <code>false</code> otherwise.
	 * @see #isResizable()
	 */
	public void setResizable(boolean resizable) {
		resizableProperty.set(resizable);
	}
	// End of the overrides dedicated to the resize of the canvas.
	
	@Override
	public void onImageFeatureAdded(JImageFeatureLayer layer, JImageFeature feature) {
		if (layer != null) {
			if (feature != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onImageFeatureRemoved(JImageFeatureLayer layer, JImageFeature feature) {
		if (layer != null) {
			if (feature != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onImageFeatureModified(JImageFeatureLayer layer, JImageFeature feature) {
		if (layer != null) {
			if (feature != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onImageFeaturesAdded(JImageFeatureLayer layer, Collection<JImageFeature> features) {
		if (layer != null) {
			if (features != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onImageFeaturesRemoved(JImageFeatureLayer layer, Collection<JImageFeature> features) {
		if (layer != null) {
			if (features != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	@Override
	public void onImageFeatureModified(JImageFeatureLayer layer, Collection<JImageFeature> features) {
		if (layer != null) {
			if (features != null) {
				setNeedRefresh(true);

				if (autoRepaint.get() == true) {
					refresh();
				}
			}
		}
	}

	/**
	 * Create a new image canvas with no attached image.
	 */
	public JImageCanvas() {
		this(null, 0, 0, FIT_AUTO, MODE_SELECTION_NONE, true, true, true);
	}

	/**
	 * Create a new image canvas with an attached image.
	 * @param image the image to display.
	 */
	public JImageCanvas(Image image) {
		this(image, 0, 0, FIT_AUTO, MODE_SELECTION_NONE, true, true, true);
	}

	/**
	 * Create a new image canvas that display the given image.
	 * @param image the image to display.
	 * @param width the width in pixels of the canvas
	 * @param height  the height in pixels of the canvas
	 * @param fit The fit method for the input image. Can be {@link #FIT_NONE}, {@link #FIT_COMPLETE}, {@link #FIT_WIDTH}, {@link #FIT_HEIGHT} or {@link #FIT_AUTO}
	 * @param selection The selection mode to use. Can be {@link #MODE_SELECTION_NONE}, {@link #MODE_SELECTION_POINT}, {@link #MODE_SELECTION_RECTANGLE} or {@link #MODE_SELECTION_POLYGON}
	 * @param autoRepaint <code>true</code> if the canvas GUI has to update automatically when modifications occurs (default) or <code>false</code> otherwise
	 * @param autoFit <code>true</code> if the canvas GUI has to fit automatically when modifications occurs (default) or <code>false</code> otherwise
	 * @param listeningControls <code>true</code> if the canvas has to listen to controls (like mouse, keyboard, ...) (default) or <code>false</code> otherwise
	 */
	public JImageCanvas(Image image, double width, double height, int fit, int selection, boolean autoRepaint, boolean autoFit, boolean listeningControls) {
		super(width, height);

		this.imageFitMethod = new SimpleIntegerProperty(fit);

		this.selectionMode = new SimpleIntegerProperty(selection);

		this.selectionMode.addListener((ChangeListener<Number>)(observable, oldValue, newValue) ->{
			
			// Only perform update if the change is effective
			if (!oldValue.equals(newValue)) {
				selectionShape = null;
				
				if (newValue.intValue() == MODE_SELECTION_POINT) {
					selectionShape = null;
				} else if (newValue.intValue() == MODE_SELECTION_RECTANGLE) {
					selectionShape = new Rectangle();
				} else if (newValue.intValue() == MODE_SELECTION_POLYGON) {
					selectionShape = new Polygon();
				}
			}
		});
		
		this.selectionShapeFill   = new Color(0.8d, 0.0d, 0.0d, 0.5d);
		
		this.selectionShapeStroke = new Color(0.8d, 0.8d, 0.0d, 1.0d);
		
		this.transform = new Affine();

		this.scale = new SimpleDoubleProperty(1.0d);

		this.cursorPosition = new SimpleObjectProperty<Point2D>();
		
		this.autoRepaint = new SimpleBooleanProperty(autoRepaint);

		this.autoFit = new SimpleBooleanProperty(autoFit);

		this.listeningControls = new SimpleBooleanProperty(autoFit);

		this.zoomFactor = new SimpleDoubleProperty(0.01d);

		this.backgroundPaint = new SimpleObjectProperty<Paint>(null);

		this.needRefresh = new SimpleBooleanProperty(true);

		this.controlPrimaryActive = new SimpleBooleanProperty(false);
		
		this.controlSecondaryActive = new SimpleBooleanProperty(false);
		
		this.layers = new LinkedList<JImageFeatureLayer>();

		refreshQueue = new SynchronousQueue<Runnable>();
		
		if (image != null) {
			setImage(image);
		}

		initEventHandling();

		refresh();
	}

	/**
	 * Get the image that is displayed within the canvas.
	 * @return the image that is displayed within the canvas
	 * @see #setImage(Image)
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Set the image to display within the canvas.
	 * @param image the image to display
	 * @see #getImage()
	 */
	public void setImage(Image image) {
		if (this.image != image) {
			this.image = image;

			if (autoFit.get()) {
				fit();
			}

			setNeedRefresh(true);

			if (autoRepaint.get()) {
				refresh();
			}
		}
	}

	/**
	 * Get the image fit method. Possible return values are {@link #FIT_NONE}, {@link #FIT_COMPLETE}, {@link #FIT_WIDTH}, {@link #FIT_HEIGHT} or {@link #FIT_AUTO}.
	 * @return the image fit method
	 * @see #setImageFitMethod(int)
	 */
	public int getImageFitMethod() {
		return imageFitMethod.get();
	}

	/**
	 * Set the image fit method. Possible return values are {@link #FIT_NONE}, {@link #FIT_COMPLETE}, {@link #FIT_WIDTH}, {@link #FIT_HEIGHT} or {@link #FIT_AUTO}.
	 * @param method the image fit method
	 * @see #getImageFitMethod()
	 * @throws IllegalArgumentException if the given method is invalid
	 */
	public void setImageFitMethod(int method) {
		if ((method < 0) || (method > 4)) {
			throw new IllegalArgumentException("Invalid fit method "+method+", expected values are FIT_NONE, FIT_COMPLETE, FIT_WIDTH, FIT_HEIGHT or FIT_AUTO.");
		}

		imageFitMethod.set(method);

	}

	/**
	 * Get the cursor current position property. The cursor position is expressed within the canvas referential (scale, rotation and translation are taken in account).
	 * @return the cursor position property
	 */
	public ObjectProperty<Point2D> getCursorPositionProperty(){
		return cursorPosition;
	}
	
	/**
	 * Get the cursor current position. The cursor position is expressed within the canvas referential (scale, rotation and translation are taken in account).
	 * @return the cursor current position
	 */
	public Point2D getCursorPosition() {
		return cursorPosition.get();
	}
	
	/**
	 * Get the image fit method property.
	 * @return the image fit method property
	 */
	public final IntegerProperty getImageFitMethodProperty() {
		return imageFitMethod;
	}

	/**
	 * Get the selection mode used by the panel. This can be one of:
	 * <ul>
	 * <li>{@link #MODE_SELECTION_POINT} for a point selection.
	 * <li>{@link #MODE_SELECTION_RECTANGLE} for a rectangle based selection.
	 * <li>{@link #MODE_SELECTION_POLYGON} for a custom shape based selection.
	 * </ul>
	 * @return the selection mode used by the panel.
	 * @see #setSelectionMode(int)
	 */
	public int getSelectionMode(){
		return this.selectionMode.get();
	}

	/**
	 * Set the selection mode that the panel has to use. This can be one of:
	 * <ul>
	 * <li>{@link #MODE_SELECTION_POINT} for a point selection.
	 * <li>{@link #MODE_SELECTION_RECTANGLE} for a rectangle based selection.
	 * <li>{@link #MODE_SELECTION_POLYGON} for a custom shape based selection.
	 * </ul>
	 * @param mode the selection mode that the panel has to use.
	 * @see #getSelectionMode()
	 */
	public void setSelectionMode(int mode){
		this.selectionMode.set(mode);
	}

	/**
	 * Get the selection mode property.
	 * @return the selection mode property.
	 */
	public final IntegerProperty getSelectionModeProperty() {
		return this.selectionMode;
	}

	/**
	 * Get the current selection shape. All coordinates and length are expressed within canvas referential. 
	 * @return the current selection shape
	 */
	public Shape getSelectionShape() {
		return selectionShape;
	}
	
	/**
	 * Get the translation applied to the view as a {@link Point2D 2D point}.
	 * @return the translation applied to the view.
	 * @see #setTranslation(Point2D)
	 * @see #getScale()
	 */
	public Point2D getTranslation(){
		return new Point2D(transform.getTx(), transform.getTy());
	}

	/**
	 * Set the translation to apply to the view. The translation is set to the given one. 
	 * For a cumulative translation, use {@link #translate(Point2D)} method.
	 * @param translation the translation to apply to the view as a {@link Point2D 2D point}.
	 * @see #getTranslation()
	 * @see #setScale(double)
	 * @see #translate(Point2D)
	 */
	public void setTranslation(Point2D translation){
		if (translation != null){

			transform.setTx(translation.getX());
			transform.setTy(translation.getY());

			setNeedRefresh(true);

			if (autoRepaint.get()){
				refresh();
			}
		}
	}

	/**
	 * Get the current scale factor of the panel view.
	 * @return the current scale factor of the panel view.
	 * @see #setScale(double)
	 * @see #getTranslation()
	 */
	public double getScale(){
		return transform.getMxx();
	}

	/**
	 * Set the scale factor to apply to the panel view.
	 * @param zoom the scale factor applied to the image.
	 * @see #getScale()
	 * @see #setTranslation(Point2D)
	 */
	public void setScale(double zoom){

		if (scale.get() != zoom){

			transform.setMxx(zoom);
			transform.setMyy(zoom);

			scale.set(zoom);

			setNeedRefresh(true);

			if (autoRepaint.get()){
				refresh();
			}
		}
	}

	/**
	 * Get if the canvas is auto fitting when dimension changes occur.
	 * @return <code>true</code> if the canvas is auto fitting when dimension changes occur and <code>false</code> otherwise
	 * @see #setAutoFit(boolean)
	 */
	public boolean isAutoFit() {
		return autoFit.get();
	}

	/**
	 * Set if the canvas has to auto fit when dimension changes occur.
	 * @param autoFit  <code>true</code> if the canvas is auto fitting when dimension changes occur and <code>false</code> otherwise
	 * @see #isAutoFit()
	 */
	public void setAutoFit(boolean autoFit) {
		this.autoFit.set(autoFit);
	}

	/**
	 * Get the auto fit property.
	 * @return the auto fit property
	 * @see #isAutoFit()
	 * @see #setAutoFit(boolean)
	 */
	public final BooleanProperty getAutoFitProperty() {
		return this.autoFit;
	}

	/**
	 * Get if the canvas is auto refreshing when changes occur.
	 * @return <code>true</code> if the canvas is auto refreshing when changes occur and <code>false</code> otherwise
	 * @see #setAutoRefresh(boolean)
	 */
	public boolean isAutoRefresh() {
		return autoRepaint.get();
	}

	/**
	 * Get if the canvas has to auto refresh when changes occur.
	 * @param autoRefresh <code>true</code> if the canvas has to auto refresh when changes occur and <code>false</code> otherwise
	 * @see #isAutoRefresh()
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRepaint.set(autoRefresh);
	}

	/**
	 * Get the auto refresh property.
	 * @return the auto refresh property
	 * @see #isAutoRefresh()
	 * @see #setAutoRefresh(boolean)
	 */
	public BooleanProperty getAutoRefreshProperty() {
		return this.autoRepaint;
	}

	/**
	 * Get if the canvas is listening to controls (Mouse, Keyboard, ...).
	 * @return <code>true</code> if the canvas is listening to controls (Mouse, Keyboard, ...) and <code>false</code> otherwise
	 * @see #setListeningControls(boolean)
	 */
	public boolean isListeningControls() {
		return this.listeningControls.get();
	}

	/**
	 * Set if the canvas has to listen to controls (Mouse, Keyboard, ...).
	 * @param listening <code>true</code> if the canvas has to listen to controls (Mouse, Keyboard, ...) and <code>false</code> otherwise
	 * @see #isListeningControls()
	 */
	public void setListeningControls(boolean listening) {
		this.listeningControls.set(listening);
	}

	/**
	 * Get the listening controls property.
	 * @return the listening controls property
	 * @see #isListeningControls()
	 * @see #setListeningControls(boolean)
	 */
	public BooleanProperty getListeningControlsProperty() {
		return this.listeningControls;
	}

	/**
	 * Get if the canvas has to be refreshed (changes have occured).
	 * @return <code>true</code> if the canvas has to be refreshed and <code>false</code> otherwise
	 * @see #refresh()
	 * @see #getRefreshNeededProperty()
	 */
	public boolean isRefreshNeeded() {
		return needRefresh.get();
	}

	/**
	 * Get the refresh needed property.
	 * @return the refresh needed property
	 * @see #isRefreshNeeded()
	 */
	public final BooleanProperty getRefreshNeededProperty() {
		return needRefresh;
	}

	/**
	 * Get the background paint.
	 * @return the background paint
	 * @see #setBackgroundPaint(Paint)
	 * @see #getBackgroundPaintProperty()
	 */
	public Paint getBackgroundPaint() {
		return backgroundPaint.get();
	}

	/**
	 * Set the background paint.
	 * @param paint the background paint
	 * @see #getBackgroundPaint()
	 * @see #getBackgroundPaintProperty()
	 */
	public void setBackgroundPaint(Paint paint) {
		this.backgroundPaint.set(paint);
	}

	/**
	 * Get the background paint property.
	 * @return the background paint property
	 * @see #getBackgroundPaint()
	 * @see #setBackgroundPaint(Paint)
	 */
	public final ObjectProperty<Paint> getBackgroundPaintProperty(){
		return this.backgroundPaint;
	}

	/**
	 * Get the coordinate within the image referential of the point located at (<code>x</code>, <code>y</code>) on the panel component.
	 * @param x the x coordinate of the point on the panel component.
	 * @param y the y coordinate of the point on the panel component.
	 * @return the coordinate within the image of the point located at (x, y) on the panel component.
	 * @see #getImageCoordinate(Point2D)
	 * @see #getViewCoordinate(double, double)
	 */
	public Point2D getImageCoordinate(double x, double y){
		return getImageCoordinate(new Point2D(x, y));
	}

	/**
	 * Get the coordinate within the image referential of the given point expressed within the canvas component referential.
	 * @param point the point expressed within the canvas component referential.
	 * @return the point expressed within the image referential.
	 * @see #getImageCoordinate(double, double)
	 * @see #getViewCoordinate(Point2D)
	 */
	public Point2D getImageCoordinate(Point2D point){
		if ((image != null) && (point != null)){
			if ((point.getX() >= 0) && (point.getX() < getWidth()) && (point.getY() >= 0) && (point.getY() < getHeight())){
				Point2D dest = new Point2D(0, 0);

				try {
					dest = transform.inverseTransform(point);

					if ((dest.getX() < 0)||(dest.getX() >= image.getWidth() - 1) || (dest.getY() < 0)||(dest.getY() >= image.getHeight() - 1)){
						dest = null;
					}

				} catch (Exception e) {
					return null;
				}

				return dest;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Get the coordinate within the canvas referential of the given point expressed within the image referential.
	 * @param point the point expressed within the image referential.
	 * @return the coordinate within the canvas referential of the given point expressed within the image referential.
	 * @see #getViewCoordinate(double, double)
	 * @see #getImageCoordinate(double, double)
	 */
	public Point2D getViewCoordinate(Point2D point){
		if ((image != null) && (point != null)){
			if (transform != null) {

				Point2D dest = new Point2D(0, 0);

				dest = transform.transform(point);

				return dest;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the coordinate within the canvas referential of the given point located at (<code>x</code>, <code>y</code>) within the image referential.
	 * @param x the x coordinate of the point expressed within the image referential.
	 * @param y the y coordinate of the point expressed within the image referential.
	 * @return the coordinate within the canvas referential of the point located at (<code>x</code>, <code>y</code>) within the image referential.
	 * @see #getViewCoordinate(Point2D)
	 * @see #getImageCoordinate(double, double)
	 */
	public Point2D getViewCoordinate(double x, double y){
		return getViewCoordinate(new Point2D(x, y));
	}
	
	/**
	 * Check if the given <code>layer</code> is displayed. 
	 * If this method return <code>true</code>, then the features that are within the layer are visible on the image panel. 
	 * @param layer the layer to check.
	 * @return <code>true</code> if the layer is displayed and <code>false</code> otherwise
	 * @see #setLayerDisplayed(String, boolean)
	 */
	public boolean isLayerDisplayed(String layer) {
		for(JImageFeatureLayer l : layers) {
			if (l.getName().equals(layer)) {
				return l.isStateDisplaying();
			}
		}

		return false;
	}

	/**
	 * Set if the given <code>layer</code> has to be displayed. 
	 * @param layer the layer to check.
	 * @param displayed <code>true</code> if the layer has to be displayed and <code>false</code> otherwise.
	 */
	public void setLayerDisplayed(String layer, boolean displayed) {
		for(JImageFeatureLayer l : layers) {
			if (l.getName().equals(layer)) {
				l.setStateDisplaying(displayed);
			}
		}
	}

	/**
	 * Add the given {@link JImageFeatureLayer layer} to this canvas.
	 * @param layer the layer to add
	 * @return <code>true</code> if the layer is successfully added and <code>false</code> otherwise
	 * @see #removeImageFeatureLayer(JImageFeatureLayer)
	 */
	public boolean addImageFeatureLayer(JImageFeatureLayer layer) {
		if (layer == null) {
			return false;
		}

		if (layers.contains(layer)) {
			return false;
		}

		boolean b = layers.add(layer);

		if (b) {

			layer.addImageFeatureLayerListener(this);

			setNeedRefresh(true);

			if (autoRepaint.get()){
				refresh();
			}
		}

		return b;
	}

	/**
	 * Remove the given {@link JImageFeatureLayer layer} from this canvas.
	 * @param layer the layer to remove
	 * @return <code>true</code> if the layer is successfully removed and <code>false</code> otherwise
	 * @see #addImageFeatureLayer(JImageFeatureLayer)
	 */
	public boolean removeImageFeatureLayer(JImageFeatureLayer layer) {
		if (layer == null) {
			return false;
		}

		boolean b = layers.remove(layer);

		if (b) {

			layer.removeImageFeatureLayerListener(this);

			setNeedRefresh(true);

			if (autoRepaint.get()){
				refresh();
			}
		}

		return b;
	}

	/**
	 * Initialize event handling
	 */
	private void initEventHandling() {

		widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setNeedRefresh(true);

				if (autoFit.get()) {
					fit();
				}

				if (autoRepaint.get()){
					refresh();
				}
			}

		});

		heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setNeedRefresh(true);

				if (autoFit.get()) {
					fit();
				}

				if (autoRepaint.get()){
					refresh();
				}
			}

		});

		// Mouse moved
		setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (listeningControls.get()) {
					cursorPosition.set(new Point2D(event.getX(), event.getY()));
				}
			}

		});

		setOnMousePressed((EventHandler<MouseEvent>)(event) ->{
			
			if (event.getButton() == MouseButton.PRIMARY) {
				controlPrimaryActive.set(true);
				
				// The canvas is within selection mode
				if ((selectionMode.get() | MODE_SELECTION_ACTIVE_MASK) != 0) {
	              selectionOrigin = cursorPosition.get();
	              
	              if (selectionMode.get() == MODE_SELECTION_POINT) {
						selectionShape = null;
					} else if (selectionMode.get() == MODE_SELECTION_RECTANGLE) {
						selectionShape = new Rectangle();
					} else if (selectionMode.get() == MODE_SELECTION_POLYGON) {
						selectionShape = new Polygon();
					}
				}
			}
			
			if (event.getButton() == MouseButton.SECONDARY) {
				controlSecondaryActive.set(true);
			}
		});
		
		setOnMouseReleased((EventHandler<MouseEvent>)(event) ->{
			
			boolean refreshNeeded = false;
			
			if (event.getButton() == MouseButton.PRIMARY) {

				// TODO proceed to auto-selection

				if (selectionShape != null) {
					selectionShape = null;
					refreshNeeded = true;
				}
				selectionOrigin = null;

				controlPrimaryActive.set(false);
			}
			
			if (event.getButton() == MouseButton.SECONDARY) {
				controlSecondaryActive.set(false);
			}
			
			setNeedRefresh(refreshNeeded);

			if (autoRepaint.get()){
				refresh();
			}
		});
		
		
		// Mouse dragged
		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (listeningControls.get()) {

					if (getImage() != null) {

						double deltaX = (cursorPosition.get().getX() - event.getX());
						double deltaY = (cursorPosition.get().getY() - event.getY());

						// Using temporary cursor position as it is needed by following code but 
						// we do not want cursorPosition property to be changed now.						
						Point2D tmpCursorPosition = new Point2D(event.getX(), event.getY());
						
						if ((selectionMode.get() == MODE_SELECTION_NONE) || (selectionMode.get() == MODE_SELECTION_POINT)){

							if (event.isPrimaryButtonDown()){
						
								translate(new Point2D(deltaX, deltaY));
							
								if (autoRepaint.get()){
									refresh();
								}
							}

						} else if ((selectionMode.get() & MODE_SELECTION_RECTANGLE) == MODE_SELECTION_RECTANGLE){
							
							// Allow translation using secondary control
							if (controlSecondaryActive.get()){								
								translate(new Point2D(deltaX, deltaY));
							}
							
							if (controlPrimaryActive.get()) {
								
								if (selectionShape != null) {
									Point2D upperLeft  = new Point2D(Math.max(0, Math.min(selectionOrigin.getX(), tmpCursorPosition.getX())), Math.max(0, Math.min(selectionOrigin.getY(), tmpCursorPosition.getY())));
									
									Point2D lowerRight = new Point2D(Math.min(getWidth(), Math.max(selectionOrigin.getX(), tmpCursorPosition.getX())), Math.min(getHeight(), Math.max(selectionOrigin.getY(), tmpCursorPosition.getY())));
									
									Rectangle rectangle = ((Rectangle)selectionShape);
									
									rectangle.setX(upperLeft.getX());
									rectangle.setY(upperLeft.getY());
									rectangle.setWidth(lowerRight.getX() - upperLeft.getX());
									rectangle.setHeight(lowerRight.getY() - upperLeft.getY());
																	
									setNeedRefresh(true);
								}
							}

							if (autoRepaint.get()){
								refresh();
							}
							
						} else if ((selectionMode.get() & MODE_SELECTION_POLYGON) == MODE_SELECTION_POLYGON){
							// TODO implements free shape selection
						} else {

						}
						
						// Update the cursor position and fire property changes 
						cursorPosition.set(tmpCursorPosition);
					}
				}
			}

		});

		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (listeningControls.get()) {
					//controlPrimaryActive.set(false);
					//controlSecondaryActive.set(false);					
				}
			}

		});
		
		// Zoom / scaling
		setOnZoomFinished(new EventHandler<ZoomEvent>() {

			@Override
			public void handle(ZoomEvent event) {
				if (listeningControls.get()) {
					setScale(event.getTotalZoomFactor());
				}
			}});

		setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				if (listeningControls.get()) {

					double delta = event.getDeltaY();

					if (delta > 0) {
						setScale(getScale() + zoomFactor.get());
					} else {
						if ((getScale() - zoomFactor.get()) > 0.0d) {
							setScale(getScale() - zoomFactor.get());
						}
					}
				}
			}

		});
	}

	/**
	 * Fit the image view according to the active {@link #getImageFitMethod() fit method}.
	 * @see #getImageFitMethod()
	 */
	public void fit() {
		if (image != null) {

			// Update image transform scale
			if (imageFitMethod.get() == FIT_COMPLETE) {
				double sx = this.getWidth() / image.getWidth() ;
				double sy = this.getHeight() / image.getHeight();

				transform.setMxx(sx);
				transform.setMyy(sy);

				this.scale.set(sx);

				setNeedRefresh(true);

			} else if (imageFitMethod.get() == FIT_WIDTH) {
				double scale = this.getWidth() / image.getWidth() ;			
				transform.setMxx(scale);
				transform.setMyy(scale);

				this.scale.set(scale);

				setNeedRefresh(true);

			} else if (imageFitMethod.get() == FIT_HEIGHT) {
				double scale = this.getHeight() / image.getHeight() ;			
				transform.setMxx(scale);
				transform.setMyy(scale);

				this.scale.set(scale);

			} else if (imageFitMethod.get() == FIT_AUTO) {
				double sx = this.getWidth() / image.getWidth();
				double sy = this.getHeight() / image.getHeight();

				double scale = Math.min(sx, sy);

				transform.setMxx(scale);
				transform.setMyy(scale);

				this.scale.set(scale);
				setNeedRefresh(true);
			}

			if (autoRepaint.get()) {
				refresh();
			}
		}
	}

	/**
	 * Add the given task to add to the refresh queue. 
	 * The refresh queue is processed at the end of the refresh of the canvas.
	 * @param task the task to add to the refresh queue
	 * @throws InterruptedException if a error occurs during queuing
	 * @see #refresh()
	 */
	public void refreshEnqueue(Runnable task) throws InterruptedException{
		if (task != null) {
			refreshQueue.put(task);
		}
	}
	
	/**
	 * Refresh the GUI components
	 */
	public void refresh() {

		try{
			refreshLock.tryAcquire();

			if (needRefresh.get()) {

				//System.out.println("[JImageCanvas][refresh()] "+java.lang.Thread.currentThread());

				final Paint originalFillPaint   = getGraphicsContext2D().getFill();
				final Paint originalStrokePaint = getGraphicsContext2D().getStroke();

				final Affine originalTransform = getGraphicsContext2D().getTransform();

				if (backgroundPaint.get() != null) {
					getGraphicsContext2D().setFill(backgroundPaint.get());
					getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
					getGraphicsContext2D().setFill(originalFillPaint);
				} else {
					getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
				}

				getGraphicsContext2D().setTransform(transform);

				// Draw the image
				if (image != null) {
					getGraphicsContext2D().drawImage(image, 0, 0);
				}

				// Draw the layers
				// Draw image features

				for (JImageFeatureLayer layer : layers) {

					if (layer.isStateDisplaying()) {

						List<JImageFeature> displayingFeatures = layer.getImageFeatures();

						if (displayingFeatures != null) {
							for(JImageFeature feature : displayingFeatures){

								if (feature.isStateDisplaying()) {
									feature.draw(getGraphicsContext2D(), transform);
								}
							}
						}
					}
				}

				// Reset graphic context to the canvas referential
				getGraphicsContext2D().setTransform(originalTransform);

				// Draw the selection shape (shape is expressed within canvas referential)
				if (selectionShape != null) {
					
					if (selectionShape instanceof Rectangle) {

						Rectangle rectangle = (Rectangle)selectionShape;
						
						if (selectionShapeFill != null) {
							getGraphicsContext2D().setFill(selectionShapeFill);
							getGraphicsContext2D().fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
						}
						
						if (selectionShapeStroke != null) {
							getGraphicsContext2D().setStroke(selectionShapeStroke);
							getGraphicsContext2D().strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
						}
						
					}
					
					
					getGraphicsContext2D().setFill(originalFillPaint);
					getGraphicsContext2D().setStroke(originalStrokePaint);
				}
				
				// Process queued refresh task
				while(!refreshQueue.isEmpty()) {
					try {
						refreshQueue.take().run();
					} catch (InterruptedException e) {
						Common.logger.log(Level.WARNING, "Cannot run task: "+e.getMessage(), e);
					}
				}
				
				needRefresh.set(false);
			}


		} finally {
			refreshLock.release();
		}


	}

	/**
	 * Set the need refresh value.
	 * @param value <code>true</code> if the canvas has to be refreshed and <code>false</code> otherwise
	 */
	private void setNeedRefresh(boolean value) {
		try {
			refreshLock.acquire();
			needRefresh.set(value);
		} catch (InterruptedException e) {

		} finally {
			refreshLock.release();
		}
	}

	/**
	 * Translate the current view from the given vector. The vector is expressed within the canvas referential.
	 * This method add the given vector to the existing translation. The reset of the translation can be done by calling {@link #setTranslation(Point2D)}.
	 * @param vector the translation vector, expressed within the canvas referential
	 * @see #setTranslation(Point2D)
	 * @see #setTranslation(Point2D)
	 */
	public void translate(Point2D vector) {
		
		double tx = transform.getTx();
		double ty = transform.getTy();
		
		if ((tx - vector.getX() <= 0)&&((tx - vector.getX()) >= (-getImage().getWidth()*scale.get() + getWidth()))){
			tx -= vector.getX();
		} else if ((tx > 0)&&(vector.getX() > 0)){
			tx -= vector.getX();
		} else if ((tx) < (-getImage().getWidth()*scale.get() + getWidth()) && (vector.getY() < 0)){
			tx -= vector.getX();
		}

		if ((ty - vector.getY() <= 0)&&((ty - vector.getY()) >= (-getImage().getHeight()*scale.get() + getHeight()))){
			ty -= vector.getY();
		} else if ((ty > 0)&&(vector.getY() > 0)){
			ty -= vector.getY();
		} else if ((ty) < (-getImage().getHeight()*scale.get() + getHeight()) && (vector.getY() < 0)){
			ty -= vector.getY();
		}

		transform.setTx(tx);
		transform.setTy(ty);
		
		setNeedRefresh(true);
	}
	
	public void select(Shape shape) {
		// TODO implement select(Shape)
		shape.getTransforms().add(transform);
		
		shape.intersect(shape, shape);
	}
}
