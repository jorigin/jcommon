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
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
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
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

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
	 * The transform that enable to pass from the image referential to the view referential.
	 * @see #viewScale
	 */
	private Affine viewTransform;

	/**
	 * The rotation that enable to pass from the image referential to the view referential.
	 * This rotation is a part of the {@link #viewTransform view transform}.
	 */
	private Rotate viewRotation;

	/**
	 * The scale that enable to pass from the image referential to the view referential.
	 * This scale is a part of the {@link #viewTransform view transform}.
	 */
	private Scale viewScale;

	/**
	 * The translation that enable to pass from the image referential to the view referential.
	 * This translation is a part of the {@link #viewTransform view transform}.
	 */
	private Translate viewTranslation;

	/**
	 * This property control if the clipping is active for translation. If the clipping is active,
	 * translating the view using {@link #viewTranslate(Point2D)} will ensure that the image cannot exit the view 
	 * according to the {@link #getViewInsets() view insets}.
	 */
	private BooleanProperty viewTranslationClipping;
	
	/**
	 * The margin to use when translating the image to avoid a edge of the image to be fixed to the edge of the canvas.
	 * This margin is expressed within the view referential.
	 */
	//private DoubleProperty imageMargin;

	/**
	 * The insets to use to avoid the displayed content to be aligned on the canvas edges. 
	 * This can be usefull if the edges of the displayed image has to be accessible for pointing, drawing, ...
	 */
	private Insets viewInsets;
	
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
		return this.resizableProperty.get();
	}

	/**
	 * Set if the image canvas can be resized.
	 * @param resizable <code>true</code> if the image canvas has to be resizable (default) or <code>false</code> otherwise.
	 * @see #isResizable()
	 */
	public void setResizable(boolean resizable) {
		this.resizableProperty.set(resizable);
	}
	// End of the overrides dedicated to the resize of the canvas.

	@Override
	public void onImageFeatureAdded(JImageFeatureLayer layer, JImageFeature feature) {
		if (layer != null) {
			if (feature != null) {
				setNeedRefresh(true);

				if (this.autoRepaint.get() == true) {
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

				if (this.autoRepaint.get() == true) {
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

				if (this.autoRepaint.get() == true) {
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

				if (this.autoRepaint.get() == true) {
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

				if (this.autoRepaint.get() == true) {
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

				if (this.autoRepaint.get() == true) {
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
				this.selectionShape = null;

				if (newValue.intValue() == MODE_SELECTION_POINT) {
					this.selectionShape = null;
				} else if (newValue.intValue() == MODE_SELECTION_RECTANGLE) {
					this.selectionShape = new Rectangle();
				} else if (newValue.intValue() == MODE_SELECTION_POLYGON) {
					this.selectionShape = new Polygon();
				}
			}
		});

		this.selectionShapeFill   = new Color(0.8d, 0.0d, 0.0d, 0.5d);

		this.selectionShapeStroke = new Color(0.8d, 0.8d, 0.0d, 1.0d);

		this.viewTransform = new Affine();

		this.viewRotation = new Rotate();

		this.viewScale = new Scale();

		this.viewTranslation = new Translate();
		
		this.viewTranslationClipping = new SimpleBooleanProperty(true);
		
		this.viewInsets = new Insets(20.0d, 20.0d, 20.0d, 20.0d);
		
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

		this.refreshQueue = new SynchronousQueue<Runnable>();

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
		return this.image;
	}

	/**
	 * Set the image to display within the canvas.
	 * @param image the image to display
	 * @see #getImage()
	 */
	public void setImage(Image image) {
		if (this.image != image) {
			this.image = image;

			if (this.autoFit.get()) {
				viewFit();
			}

			setNeedRefresh(true);

			if (this.autoRepaint.get()) {
				refresh();
			}
		}
	}

	/**
	 * Get the image fit method property.
	 * @return the image fit method property
	 */
	public IntegerProperty getImageFitMethodProperty() {
		return this.imageFitMethod;
	}
	
	/**
	 * Get the image fit method. Possible return values are {@link #FIT_NONE}, {@link #FIT_COMPLETE}, {@link #FIT_WIDTH}, {@link #FIT_HEIGHT} or {@link #FIT_AUTO}.
	 * @return the image fit method
	 * @see #setImageFitMethod(int)
	 * @see #getImageFitMethodProperty()
	 */
	public int getImageFitMethod() {
		return this.imageFitMethod.get();
	}

	/**
	 * Get the bounding box of the image within the view referential. The bounds can be greater / smaller than the view bounds.
	 * @return the bounding box of the image within the view referential
	 */
	public Bounds getImageBoundsInView() {
		
		if (this.image == null) {
			return new BoundingBox(0.0d, 0.0d, 0.0d, 0.0d);
		}
		
		// Compute image bounds according to the current rotation and scale
		Point2D imageUpperLeftInView  = this.viewTransform.transform(new Point2D(0, 0));
		Point2D imageLowerLeftInView  = this.viewTransform.transform(new Point2D(0, this.image.getHeight()));
		Point2D imageUpperRightInView = this.viewTransform.transform(new Point2D(this.image.getWidth(), 0));
		Point2D imageLowerRightInView = this.viewTransform.transform(new Point2D(this.image.getWidth(), this.image.getHeight()));

		Point2D boundsMin = new Point2D(Math.min(Math.min(imageUpperLeftInView.getX(), imageLowerLeftInView.getX()), 
													Math.min(imageUpperRightInView.getX(), imageLowerRightInView.getX())),
										Math.min(Math.min(imageUpperLeftInView.getY(), imageLowerLeftInView.getY()), 
													Math.min(imageUpperRightInView.getY(), imageLowerRightInView.getY())));

		Point2D boundsMax = new Point2D(Math.max(Math.max(imageUpperLeftInView.getX(), imageLowerLeftInView.getX()), 
													Math.max(imageUpperRightInView.getX(), imageLowerRightInView.getX())),
										Math.max(Math.max(imageUpperLeftInView.getY(), imageLowerLeftInView.getY()), 
													Math.max(imageUpperRightInView.getY(), imageLowerRightInView.getY())));
		
		return new BoundingBox(boundsMin.getX(), boundsMin.getY(), boundsMax.getX() - boundsMin.getX(), boundsMax.getY() - boundsMin.getY());
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

		this.imageFitMethod.set(method);

	}

	/**
	 * Get the cursor current position property. The cursor position is expressed within the canvas referential (scale, rotation and translation are taken in account).
	 * @return the cursor position property
	 */
	public ObjectProperty<Point2D> getCursorPositionProperty(){
		return this.cursorPosition;
	}

	/**
	 * Get the cursor current position. The cursor position is expressed within the canvas referential (scale, rotation and translation are taken in account).
	 * @return the cursor current position
	 * @see #getCursorPositionProperty()
	 */
	public Point2D getCursorPosition() {
		return this.cursorPosition.get();
	}
	
	/**
	 * Get the insets (in pixels) that the displayed image as to respect from the canvas borders. 
	 * Defining such insets is useful for avoiding the edge of the image to be on the edge of the canvas.
	 * This insets are expressed in pixels within view referential.
	 * @return the margin that the displayed image as to respect from the canvas borders
	 * @see #setViewInsets(Insets)
	 */
	public Insets getViewInsets() {
		return this.viewInsets;
	}
	
	/**
	 * Set the insets (in pixels) that the displayed image as to respect from the canvas borders. 
	 * Defining such insets is useful for avoiding the edge of the image to be on the edge of the canvas.
	 * These insets are expressed in pixels within view referential.
	 * @param insets the insets that the displayed image as to respect from the canvas borders
	 * @see #getViewInsets()
	 */
	public void setViewInsets(Insets insets) {
		this.viewInsets = insets;
	}
	
	/**
	 * Get the selection mode property.
	 * @return the selection mode property.
	 */
	public IntegerProperty getSelectionModeProperty() {
		return this.selectionMode;
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
	 * Get the current selection shape. All coordinates and length are expressed within canvas referential. 
	 * @return the current selection shape
	 */
	public Shape getSelectionShape() {
		return this.selectionShape;
	}

	/**
	 * Get the property that control the use of clipping during a view translation. If the clipping is active,
	 * translating the view using {@link #viewTranslate(Point2D)} will ensure that the image cannot exit the view 
	 * according to the {@link #getViewInsets() view insets}.
	 * @return the property that control the use of clipping during a view translation.
	 * @see #isViewTranslationClipping()
	 * @see #setViewTranslationClipping(boolean)
	 */
	public BooleanProperty getViewTranslationClippingProperty() {
		return this.viewTranslationClipping;
	}
	
	/**
	 * Get if the translation clipping is active. If the clipping is active,
	 * translating the view using {@link #viewTranslate(Point2D)} will ensure that the image cannot exit the view 
	 * according to the {@link #getViewInsets() view insets}.
	 * @return <code>true</code> if the clipping is active and <code>false</code> otherwise
	 * @see #setViewTranslationClipping(boolean)
	 * @see #getViewTranslationClippingProperty()
	 */
	public boolean isViewTranslationClipping() {
		return this.viewTranslationClipping.get();
	}
	
	/**
	 * Set if the translation clipping has to be active. If the clipping is active,
	 * translating the view using {@link #viewTranslate(Point2D)} will ensure that the image cannot exit the view 
	 * according to the {@link #getViewInsets() view insets}.
	 * @param clip <code>true</code> if the clipping has to be active and <code>false</code> otherwise
	 * @see #isViewTranslationClipping()
	 * @see #getViewTranslationClippingProperty()
	 */
	public void setViewTranslationClipping(boolean clip) {
		this.viewTranslationClipping.set(clip);
	}
	
	/**
	 * Get the rotation applied to the view as an angle expressed in degree (°).
	 * @return the rotation applied to the view as an angle expressed in degree (°).
	 * @see #setRotation(double)
	 * @see #getTranslation()
	 * @see #getScale()
	 */
	public double getRotation() {
		if (this.viewRotation != null) {
			return this.viewRotation.getAngle();
		}

		return 0.0d;
	}

	/**
	 * Set the rotation to apply to the view. For a cumulative rotation, use {@link #viewRotate(double)} method.
	 * @param angle the rotation angle, expressed in degree (°)
	 * @see #getRotation()
	 * @see #setTranslation(Point2D)
	 * @see #setScale(double)
	 * @see #viewRotate(double)
	 */
	public void setRotation(double angle) {
		
		// TODO Add rotation pivot setting
		
		if ((this.viewRotation != null) && (this.viewRotation.getAngle() != angle) && (Double.isFinite(angle) && (this.image != null))){
			
			// 1. Set the new rotation angle
			//    Ensure that the angle is between 180 / -180°
			double normalizedAngle = angle - (Math.ceil((angle + 180)/360)-1)*360;           // (-180;180]:
			
			//    Set the new angle of rotation to its normalized value
			//Point2D pivot = getViewCoordinate(getImage().getWidth()/2.0d, getImage().getHeight()/2.0d);
			Point2D pivot = new Point2D(getImage().getWidth()/2.0d, getImage().getHeight()/2.0d);
			this.viewRotation.setAngle(normalizedAngle);
			this.viewRotation.setPivotX(pivot.getX());
			this.viewRotation.setPivotY(pivot.getY());
			
			// 2. Update the global affine transform
			viewTransformUpdate();
			
/*			
			// Save the current translation component of the affine transform
			Point2D tmpTranslation = new Point2D(viewTransform.getTx(), viewTransform.getTy());
			
			// Translate image center to origin
			Affine tmpTransform = new Affine();
			tmpTransform.append(viewScale);
			tmpTransform.append(viewRotation);

			try {
				
				
				// 1. Set the new rotation angle
				//    Ensure that the angle is between 180 / -180°
				double normalizedAngle = angle - (Math.ceil((angle + 180)/360)-1)*360;           // (-180;180]:
				
				//    Set the new angle of rotation to its normalized value
				viewRotation.setAngle(normalizedAngle);
				
				// 2. Restore original translation
				tmpTransform.setToIdentity();       // temporary affine transform has to be recomputed has 
				tmpTransform.append(viewScale);     // the rotation component has changed
				tmpTransform.append(viewRotation);
				
				tmpTranslation = tmpTransform.inverseTransform(tmpTranslation);
				
				viewTranslation.setX(tmpTranslation.getX());
				viewTranslation.setY(tmpTranslation.getY());

				// 3. Update the global affine transform
				viewTransformUpdate();
				
			} catch (NonInvertibleTransformException e) {
				Common.logger.log(Level.SEVERE, "Cannot invert transformation: "+e.getMessage(), e);
			}						
*/			
		}
	}


	/**
	 * Get the current scale factor of the panel view.
	 * @return the current scale factor of the panel view.
	 * @see #setScale(double)
	 * @see #getTranslation()
	 */
	public double getScale(){
		return this.viewScale.getX();
	}

	/**
	 * Set the scale factor to apply to the panel view.
	 * @param scale the scale factor applied to the image.
	 * @see #getScale()
	 * @see #setTranslation(Point2D)
	 */
	public void setScale(double scale){

		// TODO Add scale pivot setting
		
		if ((this.viewScale.getX() != scale) || (this.viewScale.getY() != scale)) {
			
			this.viewScale.setX(scale);
			this.viewScale.setY(scale);

			viewTransformUpdate();
		}
	}

	/**
	 * Get the translation applied to the view as a {@link Point2D 2D point}.
	 * @return the translation applied to the view.
	 * @see #setTranslation(Point2D)
	 * @see #getRotation()
	 * @see #getScale()
	 */
	public Point2D getTranslation(){
		return new Point2D(this.viewTranslation.getTx(), this.viewTranslation.getTy());
	}

	/**
	 * Set the translation to apply to the view. The <code>translation</code> is expressed within view referential in pixels (px).
	 * For a cumulative translation, use {@link #viewTranslate(Point2D)} method.
	 * @param translation the translation to apply to the view as a {@link Point2D 2D point}.
	 * @see #getTranslation()
	 * @see #setRotation(double)
	 * @see #setScale(double)
	 * @see #viewTranslate(Point2D)
	 */
	public void setTranslation(Point2D translation){

		if ((translation != null) && ((this.viewTranslation.getX() != translation.getX()) || ((this.viewTranslation.getY() != translation.getY())))){

			try {

				// Express the input translation (given within view referential) into image referential
				Affine transform = new Affine();
				transform.append(this.viewScale);
				transform.append(this.viewRotation);

				Point2D translationVectorImage = transform.inverseTransform(translation.getX(), translation.getY());

				this.viewTranslation.setX(translationVectorImage.getX());
				this.viewTranslation.setY(translationVectorImage.getY());

				viewTransformUpdate();
				
			} catch (Exception e) {
				Common.logger.log(Level.SEVERE, "Cannot invert view rotation / scale: "+e.getMessage(), e);
			}	
		}
	}

	/**
	 * Rotate the current view by the given angle. The angle has to be expressed in degree (°).
	 * This method add the new rotation angle to the current one. If a reset of the rotation is needed, the method has to be used.
	 * @param angle the rotation angle, expressed in degree (°)
	 * @see #viewTranslate(Point2D)
	 */
	public void viewRotate(double angle) {

		if (angle != 0.0d) {
			setRotation(this.viewRotation.getAngle()+angle);
		}
	}

	/**
	 * Translate the current view by the given vector. The vector is expressed within the view referential.
	 * This method substract the given vector from the existing translation. The reset of the translation can be done by calling {@link #setTranslation(Point2D)}.
	 * @param vector the translation vector, expressed within the view referential
	 * @see #setTranslation(Point2D)
	 */
	public void viewTranslate(Point2D vector) {

		if ((this.image != null) && (this.viewTransform != null)) {

			Bounds bounds = this.getImageBoundsInView();

			double boundedTranslationX = 0.0d;
			
			double boundedTranslationY = 0.0d;
	
			boolean clipping = true;
			
			if (clipping) {
				// Handling X translation
				if (vector.getX() < 0.0d) {
					
					boundedTranslationX = Math.min(Math.max(getWidth() - bounds.getMaxX() - this.viewInsets.getRight(), vector.getX()),
	    										   Math.max(-bounds.getMinX() + this.viewInsets.getLeft(), vector.getX()));
					
				} else if (vector.getX() > 0.0d) {
					
					boundedTranslationX = Math.max(Math.min(getWidth() - bounds.getMaxX() - this.viewInsets.getRight(), vector.getX()),
	                        					   Math.min(-bounds.getMinX() + this.viewInsets.getLeft(), vector.getX()));
				}

				// Handling Y translation
				if (vector.getY() < 0.0d) {
					
					boundedTranslationY = Math.min(Math.max(getHeight() - bounds.getMaxY() - this.viewInsets.getBottom(), vector.getY()),
	    										   Math.max(-bounds.getMinY() + this.viewInsets.getTop(), vector.getY()));
					
				} else if (vector.getY() > 0.0d){
					
					boundedTranslationY = Math.max(Math.min(getHeight() - bounds.getMaxY() - this.viewInsets.getBottom(), vector.getY()),
	                        					   Math.min(-bounds.getMinY() + this.viewInsets.getTop(), vector.getY()));

				}
			} else {
				boundedTranslationX = vector.getX();
				boundedTranslationY = vector.getY();
			}

			// Update the translation only if the parameter have been changed.
			if ((boundedTranslationX != 0) || (boundedTranslationY != 0.0d)){	
				
				/// Express the current translation within the view referential
				Affine transform = new Affine();
				transform.append(this.viewScale);
				transform.append(this.viewRotation);
				
				Point2D currentTranslationInView = transform.transform(new Point2D(this.viewTranslation.getX(), this.viewTranslation.getY()));

				// Combine the current translation and the new one
				setTranslation(new Point2D(currentTranslationInView.getX()+boundedTranslationX, currentTranslationInView.getY()+boundedTranslationY));
			}
		}
	}


	/**
	 * Get if the canvas is auto fitting when dimension changes occur.
	 * @return <code>true</code> if the canvas is auto fitting when dimension changes occur and <code>false</code> otherwise
	 * @see #setAutoFit(boolean)
	 */
	public boolean isAutoFit() {
		return this.autoFit.get();
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
		return this.autoRepaint.get();
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
		return this.needRefresh.get();
	}

	/**
	 * Get the refresh needed property.
	 * @return the refresh needed property
	 * @see #isRefreshNeeded()
	 */
	public final BooleanProperty getRefreshNeededProperty() {
		return this.needRefresh;
	}

	/**
	 * Get the background paint.
	 * @return the background paint
	 * @see #setBackgroundPaint(Paint)
	 * @see #getBackgroundPaintProperty()
	 */
	public Paint getBackgroundPaint() {
		return this.backgroundPaint.get();
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
		if ((this.image != null) && (point != null)){
			if ((point.getX() >= 0) && (point.getX() < getWidth()) && (point.getY() >= 0) && (point.getY() < getHeight())){
				Point2D dest = new Point2D(0, 0);

				try {
					dest = this.viewTransform.inverseTransform(point);

					if ((dest.getX() < 0)||(dest.getX() >= this.image.getWidth() - 1) || (dest.getY() < 0)||(dest.getY() >= this.image.getHeight() - 1)){
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
		if ((this.image != null) && (point != null)){
			if (this.viewTransform != null) {

				Point2D dest = new Point2D(0, 0);

				dest = this.viewTransform.transform(point);

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
		for(JImageFeatureLayer l : this.layers) {
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
		for(JImageFeatureLayer l : this.layers) {
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

		if (this.layers.contains(layer)) {
			return false;
		}

		boolean b = this.layers.add(layer);

		if (b) {

			layer.addImageFeatureLayerListener(this);

			setNeedRefresh(true);

			if (this.autoRepaint.get()){
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

		boolean b = this.layers.remove(layer);

		if (b) {

			layer.removeImageFeatureLayerListener(this);

			setNeedRefresh(true);

			if (this.autoRepaint.get()){
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

				if (JImageCanvas.this.autoFit.get()) {
					viewFit();
				}

				if (JImageCanvas.this.autoRepaint.get()){
					refresh();
				}
			}

		});

		heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setNeedRefresh(true);

				if (JImageCanvas.this.autoFit.get()) {
					viewFit();
				}

				if (JImageCanvas.this.autoRepaint.get()){
					refresh();
				}
			}

		});

		// Mouse moved
		setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (JImageCanvas.this.listeningControls.get()) {
					JImageCanvas.this.cursorPosition.set(new Point2D(event.getX(), event.getY()));
				}
			}

		});

		setOnMousePressed((EventHandler<MouseEvent>)(event) ->{

			if (event.getButton() == MouseButton.PRIMARY) {
				this.controlPrimaryActive.set(true);

				// The canvas is within selection mode
				if ((this.selectionMode.get() | MODE_SELECTION_ACTIVE_MASK) != 0) {
					this.selectionOrigin = this.cursorPosition.get();

					if (this.selectionMode.get() == MODE_SELECTION_POINT) {
						this.selectionShape = null;
					} else if (this.selectionMode.get() == MODE_SELECTION_RECTANGLE) {
						this.selectionShape = new Rectangle();
					} else if (this.selectionMode.get() == MODE_SELECTION_POLYGON) {
						this.selectionShape = new Polygon();
					}
				}
			}

			if (event.getButton() == MouseButton.SECONDARY) {
				this.controlSecondaryActive.set(true);
			}
		});

		setOnMouseReleased((EventHandler<MouseEvent>)(event) ->{

			boolean refreshNeeded = false;

			if (event.getButton() == MouseButton.PRIMARY) {

				// Process to selection on underlying features
				if ((this.selectionMode.get() | MODE_SELECTION_ACTIVE_MASK) != 0) {
					if (this.selectionShape != null) {
						select(this.selectionShape);
					}
				}


				if (this.selectionShape != null) {
					this.selectionShape = null;
					refreshNeeded = true;
				}
				this.selectionOrigin = null;

				this.controlPrimaryActive.set(false);
			}

			if (event.getButton() == MouseButton.SECONDARY) {
				this.controlSecondaryActive.set(false);
			}

			setNeedRefresh(refreshNeeded);

			if (this.autoRepaint.get()){
				refresh();
			}
		});


		// Mouse dragged
		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (JImageCanvas.this.listeningControls.get()) {

					// Using temporary cursor position as it is needed by following code but 
					// we do not want cursorPosition property to be changed now.						
					Point2D tmpCursorPosition = new Point2D(event.getX(), event.getY());

					Point2D cursorMove = tmpCursorPosition.subtract(JImageCanvas.this.cursorPosition.get());

					if ((JImageCanvas.this.selectionMode.get() == MODE_SELECTION_NONE) || (JImageCanvas.this.selectionMode.get() == MODE_SELECTION_POINT)){

						if (JImageCanvas.this.controlPrimaryActive.get()){

							viewTranslate(cursorMove);

							if (JImageCanvas.this.autoRepaint.get()){
								refresh();
							}
						}

					} else if ((JImageCanvas.this.selectionMode.get() & MODE_SELECTION_RECTANGLE) == MODE_SELECTION_RECTANGLE){

						// Allow translation using secondary control
						if (JImageCanvas.this.controlSecondaryActive.get()){								
							viewTranslate(cursorMove);
						}

						if (JImageCanvas.this.controlPrimaryActive.get()) {

							if (JImageCanvas.this.selectionShape != null) {
								Point2D upperLeft  = new Point2D(Math.max(0, Math.min(JImageCanvas.this.selectionOrigin.getX(), tmpCursorPosition.getX())), Math.max(0, Math.min(JImageCanvas.this.selectionOrigin.getY(), tmpCursorPosition.getY())));

								Point2D lowerRight = new Point2D(Math.min(getWidth(), Math.max(JImageCanvas.this.selectionOrigin.getX(), tmpCursorPosition.getX())), Math.min(getHeight(), Math.max(JImageCanvas.this.selectionOrigin.getY(), tmpCursorPosition.getY())));

								Rectangle rectangle = ((Rectangle)JImageCanvas.this.selectionShape);

								rectangle.setX(upperLeft.getX());
								rectangle.setY(upperLeft.getY());
								rectangle.setWidth(lowerRight.getX() - upperLeft.getX());
								rectangle.setHeight(lowerRight.getY() - upperLeft.getY());

								setNeedRefresh(true);
							}
						}

						if (JImageCanvas.this.autoRepaint.get()){
							refresh();
						}

					} else if ((JImageCanvas.this.selectionMode.get() & MODE_SELECTION_POLYGON) == MODE_SELECTION_POLYGON){
						// TODO implements free shape selection
					} else {

					}

					// Update the cursor position and fire property changes 
					JImageCanvas.this.cursorPosition.set(tmpCursorPosition);
				}
			}

		});

		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (JImageCanvas.this.listeningControls.get()) {
					//controlPrimaryActive.set(false);
					//controlSecondaryActive.set(false);					
				}
			}

		});

		// Zoom / scaling
		setOnZoomFinished(new EventHandler<ZoomEvent>() {

			@Override
			public void handle(ZoomEvent event) {
				if (JImageCanvas.this.listeningControls.get()) {
					setScale(event.getTotalZoomFactor());
				}
			}});

		setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				if (JImageCanvas.this.listeningControls.get()) {

					double delta = event.getDeltaY();

					if (delta > 0) {
						setScale(getScale() + JImageCanvas.this.zoomFactor.get());
					} else {
						if ((getScale() - JImageCanvas.this.zoomFactor.get()) > 0.0d) {
							setScale(getScale() - JImageCanvas.this.zoomFactor.get());
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
	public void viewFit() {

		if ((this.image != null) && (this.getWidth() != 0.0d) && (this.getHeight() != 0.0d) && (this.image.getWidth() != 0) && (this.image.getHeight() != 0.0d)){
			
			// Compute image bounds according to the current rotation and scale
			Point2D imageUpperLeftInView  = this.viewRotation.transform(new Point2D(0, 0));
			Point2D imageLowerLeftInView  = this.viewRotation.transform(new Point2D(0, this.image.getHeight()));
			Point2D imageUpperRightInView = this.viewRotation.transform(new Point2D(this.image.getWidth(), 0));
			Point2D imageLowerRightInView = this.viewRotation.transform(new Point2D(this.image.getWidth(), this.image.getHeight()));

			Point2D boundsMin = new Point2D(Math.min(Math.min(imageUpperLeftInView.getX(), imageLowerLeftInView.getX()), 
														Math.min(imageUpperRightInView.getX(), imageLowerRightInView.getX())),
											Math.min(Math.min(imageUpperLeftInView.getY(), imageLowerLeftInView.getY()), 
														Math.min(imageUpperRightInView.getY(), imageLowerRightInView.getY())));

			Point2D boundsMax = new Point2D(Math.max(Math.max(imageUpperLeftInView.getX(), imageLowerLeftInView.getX()), 
														Math.max(imageUpperRightInView.getX(), imageLowerRightInView.getX())),
											Math.max(Math.max(imageUpperLeftInView.getY(), imageLowerLeftInView.getY()), 
														Math.max(imageUpperRightInView.getY(), imageLowerRightInView.getY())));
			
			Bounds bounds = new BoundingBox(boundsMin.getX(), boundsMin.getY(), boundsMax.getX() - boundsMin.getX(), boundsMax.getY() - boundsMin.getY());

			// Update image transform scale
			if (this.imageFitMethod.get() == FIT_COMPLETE) {
				double sx = (this.getWidth() - this.viewInsets.getLeft() - this.viewInsets.getRight()) / bounds.getWidth();
				double sy = (this.getHeight() - this.viewInsets.getTop() - this.viewInsets.getBottom()) / bounds.getHeight();

				this.viewScale.setX(sx);
				this.viewScale.setY(sy);

			} else if (this.imageFitMethod.get() == FIT_WIDTH) {
				double scale = (this.getWidth() - this.viewInsets.getLeft() - this.viewInsets.getRight()) / bounds.getWidth();
				this.viewScale.setX(scale);
				this.viewScale.setY(scale);

			} else if (this.imageFitMethod.get() == FIT_HEIGHT) {
				double scale = (this.getHeight() - this.viewInsets.getTop() - this.viewInsets.getBottom()) / bounds.getHeight();		
				this.viewScale.setX(scale);
				this.viewScale.setY(scale);

			} else if (this.imageFitMethod.get() == FIT_AUTO) {
				double sx = (this.getWidth() - this.viewInsets.getLeft() - this.viewInsets.getRight()) / bounds.getWidth();
				double sy = (this.getHeight() -this.viewInsets.getTop() - this.viewInsets.getBottom()) / bounds.getHeight();

				double scale = Math.min(sx, sy);

				this.viewScale.setX(scale);
				this.viewScale.setY(scale);
			}

			// Update translation
			this.viewTranslation.setX(0);
			this.viewTranslation.setY(0);
			this.viewTranslation.setZ(0);
			
			this.viewTransform.setToIdentity();
			this.viewTransform.append(this.viewScale);
			this.viewTransform.append(this.viewRotation);
			this.viewTransform.append(this.viewTranslation);
			
			
			// Compute new image bounds
			Point2D fittedBoundsMinV = new Point2D((-bounds.getMinX()*this.viewScale.getX()+this.viewInsets.getLeft()), (-bounds.getMinY()*this.viewScale.getY()+this.viewInsets.getTop()));

			viewTranslate(fittedBoundsMinV);
			
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
			this.refreshQueue.put(task);
		}
	}

	/**
	 * Refresh the GUI components
	 */
	public void refresh() {

		try{
			this.refreshLock.tryAcquire();

			if (this.needRefresh.get()) {

				//System.out.println("[JImageCanvas][refresh()] "+java.lang.Thread.currentThread());

				final Paint originalFillPaint   = getGraphicsContext2D().getFill();
				final Paint originalStrokePaint = getGraphicsContext2D().getStroke();

				final Affine originalTransform = getGraphicsContext2D().getTransform();

				if (this.backgroundPaint.get() != null) {
					getGraphicsContext2D().setFill(this.backgroundPaint.get());
					getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
					getGraphicsContext2D().setFill(originalFillPaint);
				} else {
					getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
				}

				getGraphicsContext2D().setTransform(this.viewTransform);

				// Draw the image
				if (this.image != null) {
					getGraphicsContext2D().drawImage(this.image, 0, 0);
				}

				// Draw the layers
				// Draw image features

				for (JImageFeatureLayer layer : this.layers) {

					if (layer.isStateDisplaying()) {

						List<JImageFeature> displayingFeatures = layer.getImageFeatures();

						if (displayingFeatures != null) {
							for(JImageFeature feature : displayingFeatures){

								if (feature.isStateDisplaying()) {
									feature.draw(getGraphicsContext2D(), this.viewTransform);
								}
							}
						}
					}
				}

				// Reset graphic context to the canvas referential
				getGraphicsContext2D().setTransform(originalTransform);

				// Draw the selection shape (shape is expressed within canvas referential)
				if (this.selectionShape != null) {

					if (this.selectionShape instanceof Rectangle) {

						Rectangle rectangle = (Rectangle)this.selectionShape;

						if (this.selectionShapeFill != null) {
							getGraphicsContext2D().setFill(this.selectionShapeFill);
							getGraphicsContext2D().fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
						}

						if (this.selectionShapeStroke != null) {
							getGraphicsContext2D().setStroke(this.selectionShapeStroke);
							getGraphicsContext2D().strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
						}

					}


					getGraphicsContext2D().setFill(originalFillPaint);
					getGraphicsContext2D().setStroke(originalStrokePaint);
				}

				// Process queued refresh task
				while(!this.refreshQueue.isEmpty()) {
					try {
						this.refreshQueue.take().run();
					} catch (InterruptedException e) {
						Common.logger.log(Level.WARNING, "Cannot run task: "+e.getMessage(), e);
					}
				}

				this.needRefresh.set(false);
			}


		} finally {
			this.refreshLock.release();
		}


	}

	/**
	 * Set the need refresh value.
	 * @param value <code>true</code> if the canvas has to be refreshed and <code>false</code> otherwise
	 */
	private void setNeedRefresh(boolean value) {
		try {
			this.refreshLock.acquire();
			this.needRefresh.set(value);
		} catch (InterruptedException e) {

		} finally {
			this.refreshLock.release();
		}
	}

	/**
	 * Select the features that are inside the given {@link Shape shape}. The shape is expressed within view coordinates.
	 * @param shape the selection {@link Shape shape}
	 */
	public void select(Shape shape) {

		// TODO implement selection
		
		try {
			shape.getTransforms().add(this.viewTransform.createInverse());

			for (JImageFeatureLayer layer : this.layers) {

				if (layer.isStateDisplaying()) {

					List<JImageFeature> features = layer.getImageFeatures();

					if (features != null) {
						for(JImageFeature feature : features){

							if ((feature.isStateSelectable())){
								if (feature.inside(shape)){
									feature.setStateSelected(true);
								} else {
									feature.setStateSelected(false);
								}
							}
						}
					}
				}
			}
		} catch (NonInvertibleTransformException e) {
			Common.logger.log(Level.SEVERE, "Cannot select: "+e.getMessage(), e);
		}
	}

	/**
	 * Update the view transform according to the transformation components.
	 */
	private void viewTransformUpdate() {
		this.viewTransform.setToIdentity();

		
		this.viewTransform.append(this.viewScale);
		this.viewTransform.append(this.viewRotation);
		this.viewTransform.append(this.viewTranslation);
/*
		System.out.println("Scale");
		System.out.println("  - x     : "+viewScale.getX());
		System.out.println("  - y     : "+viewScale.getY());
		System.out.println("  - Pivot : ("+viewScale.getPivotX()+", "+viewScale.getPivotY()+", "+viewScale.getPivotZ()+")");
		System.out.println("Rotation");
		System.out.println("  - Angle : "+viewRotation.getAngle());
		System.out.println("  - Axis  : "+viewRotation.getAxis());
		System.out.println("  - Pivot : ("+viewRotation.getPivotX()+", "+viewRotation.getPivotY()+", "+viewRotation.getPivotZ()+")");
		System.out.println("Translation");
		System.out.println("  - Vector : ("+viewTranslation.getX()+", "+viewTranslation.getY()+", "+viewTranslation.getZ()+")");
		System.out.println("");
		System.out.println("[ "+viewTransform.getMxx()+" "+viewTransform.getMxy()+" "+viewTransform.getMxz()+ " "+viewTransform.getTx()+" ]");
		System.out.println("[ "+viewTransform.getMyx()+" "+viewTransform.getMyy()+" "+viewTransform.getMyz()+ " "+viewTransform.getTy()+" ]");
		System.out.println("[ "+viewTransform.getMzx()+" "+viewTransform.getMzy()+" "+viewTransform.getMzz()+ " "+viewTransform.getTz()+" ]");
		System.out.println("[ "+0.0d+" "+0.0d+" "+0.0d+ " "+1.0d+" ]");
*/		
		
		setNeedRefresh(true);

		if (this.autoRepaint.get()){
			refresh();
		}
	}
}
