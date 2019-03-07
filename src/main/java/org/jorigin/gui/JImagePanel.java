package org.jorigin.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jorigin.Common;

/**
 * A {@link JPanel swing component} that enables to display {@link BufferedImage images}. <br>
 * This panel provides various operations on the view (zoom, moving, ...) and helper methods for correspondence between image space and display space.<br>
 * This image panel also manages {@link JImageFeature features} that enable to display interactive overlays on the panel.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.4
 */
public class JImagePanel extends JPanel {

  private static final long serialVersionUID = 201903071000L;
  
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
   * The selection with point mode. This mode enable to select features on the image using a point.
   * @see #MODE_SELECTION_RECT
   * @see #MODE_SELECTION_SHAPE
   */
  public static final int MODE_SELECTION_POINT = 1;
  
  /**
   * The selection with rectangle mode. This mode enable to select features on the image using a rectangle.
   * @see #MODE_SELECTION_POINT
   * @see #MODE_SELECTION_SHAPE
   */
  public static final int MODE_SELECTION_RECT  = 2;
  
  /**
   * The selection with shape mode. This mode enable to select features on the image using a custom shape.
   * @see #MODE_SELECTION_POINT
   * @see #MODE_SELECTION_RECT
   */
  public static final int MODE_SELECTION_SHAPE = 4;

  private BufferedImage image = null;
  
  private Map<String, List<JImageFeature>> layers;
  
  private Map<String, Boolean> layersVisibility;
  
  private double scale = 1.0f;

  private RenderingHints renderingHints = null;
  
  private AffineTransform transform = null;
  
  private final Shape selectionShape = null;
  
  private Stroke selectionShapeStroke = null;
  
  private int mode = MODE_SELECTION_POINT;
  
  private int mouseButtonActive = 0;
  
  int lastCursorPositionX = 0;
  int lastCursorPositionY = 0;
  
  private boolean autoRepaint = true;
  
  private boolean autoFit     = false;
  
  private boolean painting = false;
  
  /**
   * Create a new image panel.
   */
  public JImagePanel(){
    this(null);
  }
  
  /**
   * Create a new image panel that display the given {@link BufferedImage image}.
   * @param image the {@link BufferedImage image} to display.
   */
  public JImagePanel(BufferedImage image){
    super();
    
    this.image = image;
    
    renderingHints = null;
    
    transform = new AffineTransform();
    
    selectionShapeStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 10.0f, new float[]{1, 0, 1, 0}, 0.0f);

    addMouseListener(new MouseListener(){

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        
        mouseButtonActive |= InputEvent.getMaskForButton(e.getButton());

        lastCursorPositionX = e.getX();
        lastCursorPositionY = e.getY();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        
        mouseButtonActive &= ~InputEvent.getMaskForButton(e.getButton());
        
        lastCursorPositionX = e.getX();
        lastCursorPositionY = e.getY();
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        lastCursorPositionX = e.getX();
        lastCursorPositionY = e.getY();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        lastCursorPositionX = e.getX();
        lastCursorPositionY = e.getY();
      }
      
    });
    
    addMouseMotionListener(new MouseMotionListener(){

      @Override
      public void mouseDragged(MouseEvent e) {
        
        double deltaX = (lastCursorPositionX - e.getX());
        double deltaY = (lastCursorPositionY - e.getY());
        
        if ((mode & MODE_SELECTION_POINT) == MODE_SELECTION_POINT){
   
          double tx = transform.getTranslateX();
          double ty = transform.getTranslateY();

          if ((mouseButtonActive & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK){
            if ((tx - deltaX <= 0)&&((tx - deltaX) >= (-getImage().getWidth()*scale + getWidth()))){
              tx -= deltaX;
            } else if ((tx > 0)&&(deltaX > 0)){
              tx -= deltaX;
            } else if ((tx) < (-getImage().getWidth()*scale + getWidth()) && (deltaX < 0)){
              tx -= deltaX;
            }
          
            if ((ty - deltaY <= 0)&&((ty - deltaY) >= (-getImage().getHeight()*scale + getHeight()))){
              ty -= deltaY;
            } else if ((ty > 0)&&(deltaY > 0)){
              ty -= deltaY;
            } else if ((ty) < (-getImage().getHeight()*scale + getHeight()) && (deltaY < 0)){
              ty -= deltaY;
            }
            
            setTranslation(new Point2D.Double(tx, ty));
          
            lastCursorPositionX = e.getX();
            lastCursorPositionY = e.getY();
          
            if (autoRepaint){
              repaint();
            }
          }

        } else if ((mode & MODE_SELECTION_RECT) == MODE_SELECTION_RECT){
          
          
          
        } else if ((mode & MODE_SELECTION_SHAPE) == MODE_SELECTION_SHAPE){
          
        } else {
          
        }
      }

      @Override
      public void mouseMoved(MouseEvent e) {

        lastCursorPositionX = e.getX();
        lastCursorPositionY = e.getY();
      }
      
    });
    
    addComponentListener(new ComponentListener() {

      @Override
      public void componentResized(ComponentEvent e) {
        if (isAutoFit()) {
          fit();
        }
      }

      @Override
      public void componentMoved(ComponentEvent e) { 
      }

      @Override
      public void componentShown(ComponentEvent e) {
      }

      @Override
      public void componentHidden(ComponentEvent e) {
      }
      
    });
  }
  
  @Override
  public void paintComponent(Graphics g){
    
    painting = true;
    
    Graphics2D g2d = (Graphics2D)g;
    
    Color savedColor = g2d.getColor();
    
    RenderingHints savedHints = g2d.getRenderingHints();
    
    AffineTransform savedTransform = g2d.getTransform();
    
    if (renderingHints != null){
      g2d.setRenderingHints(renderingHints);
    }
    
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    g2d.setTransform(transform);
    
    // Transformed drawings
    
    // Draw image
    if (image != null){

      g2d.drawRenderedImage(image, null);

    }
    
    // Draw image features
    if (layers != null){
      
      for (String layer : layers.keySet()) {
        
        if (isLayerDisplayed(layer)) {
          
          List<JImageFeature> displayingFeatures = layers.get(layer);
          
          if (displayingFeatures != null) {
            for(JImageFeature feature : displayingFeatures){
              
              if (feature.isDisplaying()) {
                feature.draw(g2d, transform);
              }
            }
          }
        }
      }
    }
    
    // Restore original transform.
    g2d.setTransform(savedTransform);
    
    // Draw selection shape
    if (selectionShape != null){
      drawSelectionShape(g2d, selectionShape);
    }
    
    // Restore original parameters.
    g2d.setColor(savedColor);
    g2d.setRenderingHints(savedHints);
    
    painting = false;
  }
    
  /**
   * Draw the given selection {@link Shape shape} on the given {@link Graphics2D graphics context}.
   * @param g2d the the given {@link Graphics2D graphics context}.
   * @param shape the {@link Shape shape} to draw.
   */
  protected void drawSelectionShape(Graphics2D g2d, Shape shape){
    
    Color savedColor               = g2d.getColor();
    RenderingHints savedHints      = g2d.getRenderingHints();
    AffineTransform savedTransform = g2d.getTransform();
    
    if (selectionShapeStroke != null){
      g2d.setStroke(selectionShapeStroke);
    }
    
    g2d.draw(shape);
    
    g2d.setColor(savedColor);
    g2d.setRenderingHints(savedHints);
    g2d.setTransform(savedTransform);
    
  }
  
  /**
   * Check if the panel is actually refreshing image and features.
   * @return <code>true</code> if the panel is actually refreshing image and features and <code>false</code> otherwise.
   */
  public boolean isRendering() {
    return painting;
  }
  
  /**
   * Get the {@link RenderingHints rendering hints} that this panel use. If this method return <code>null</code>, 
   * default rendering hints are used.
   * @return the {@link RenderingHints rendering hints} that this panel use.
   * @see #setRenderingHints(RenderingHints)
   */
  public RenderingHints getRenderingHints() {
    return renderingHints;
  }
  
  /**
   * Set the {@link RenderingHints rendering hints} that this panel has to use. If <code>hints</code> parameter is set to <code>null</code>, 
   * then default rendering hints are used.
   * @param hints the {@link RenderingHints rendering hints} that this panel has to use.
   * @see #getRenderingHints()
   */
  public void setRenderingHints(RenderingHints hints) {
    this.renderingHints = hints;
  }
  
  /**
   * Get the {@link Stroke stroke} used when drawing selection shape.
   * @return the {@link Stroke stroke} used when drawing selection shape.
   * @see #setSelectionShapeStroke(Stroke)
   */
  public Stroke getSelectionShapeStroke(){
    return selectionShapeStroke;
  }
  
  /**
   * Set the {@link Stroke stroke} to use when drawing selection shape.
   * @param stroke the {@link Stroke stroke} to use.
   * @see #getSelectionShapeStroke()
   */
  public void setSelectionShapeStroke(Stroke stroke){
    this.selectionShapeStroke = stroke;
  }
  
  /**
   * Get the selection mode used by the panel. This can be one of:
   * <ul>
   * <li>{@link #MODE_SELECTION_POINT} for a point selection.
   * <li>{@link #MODE_SELECTION_RECT} for a rectangle based selection.
   * <li>{@link #MODE_SELECTION_SHAPE} for a custom shape based selection.
   * </ul>
   * @return the selection mode used by the panel.
   * @see #setSelectionMode(int)
   */
  public int getSelectionMode(){
    return mode;
  }
  
  /**
   * Set the selection mode that the panel has to use. This can be one of:
   * <ul>
   * <li>{@link #MODE_SELECTION_POINT} for a point selection.
   * <li>{@link #MODE_SELECTION_RECT} for a rectangle based selection.
   * <li>{@link #MODE_SELECTION_SHAPE} for a custom shape based selection.
   * </ul>
   * @param mode the selection mode that the panel has to use.
   * @see #getSelectionMode()
   */
  public void setSelectionMode(int mode){
    
    int oldValue = this.mode;
    
    this.mode = mode;
    
    firePropertyChange(MODE_SELECTION_PROPERTY, oldValue, mode);
  }
  
  /**
   * Get if the panel is using auto repaint. 
   * If the auto repaint mode is activated, the panel will repaint every time a modification is made to the displayed object (image, features) or to the view (scale, translation, ...). 
   * This mode is not convenient if many modification has to be made before a display refresh. 
   * In this case, it is recommended to set the auto repaint to <code>false</code> and to call {@link #repaint()} when all modifications are done.
   * @return <code>true</code> if the panel has to auto repaint and <code>false</code> otherwise.
   * @see #setAutoRepaint(boolean)
   */
  public boolean isAutoRepaint(){
    return autoRepaint;
  }
  
  /**
   * Set if the panel has to do auto repaint. 
   * If the auto repaint mode is activated, the panel will repaint every time a modification is made to the displayed object (image, features) or to the view (scale, translation, ...). 
   * This mode is not convenient if many modification has to be made before a display refresh. 
   * In this case, it is recommended to set the auto repaint to <code>false</code> and to call {@link #repaint()} when all modifications are done.
   * @param auto <code>true</code> if the panel has to auto repaint and <code>false</code> otherwise.
   * @see #isAutoRepaint()
   */
  public void setAutoRepaint(boolean auto){
    this.autoRepaint = auto;
  }
  
  /**
   * Get if the panel fits the view when its resized.
   * @return <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #setAutoFit(boolean)
   * @see #fit()
   */
  public boolean isAutoFit() {
    return autoFit;
  }
  
  /**
   * Set if the panel has to fit the view when its resized.
   * @param autoFit <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #isAutoFit()
   * @see #fit()
   */
  public void setAutoFit(boolean autoFit) {
    this.autoFit = autoFit;
  }
  
  /**
   * Get the {@link BufferedImage image} that is displayed within the panel.
   * @return the {@link BufferedImage image} that is displayed within the panel.
   * @see #setImage(BufferedImage)
   */
  public BufferedImage getImage(){
    return image;
  }
  
  /**
   * Set the {@link BufferedImage image} to display within the panel.
   * @param image the {@link BufferedImage image} to display within the panel.
   * @see #getImage()
   */
  public void setImage(BufferedImage image){
    this.image = image;
    
    this.transform = new AffineTransform();
    
    if (autoRepaint){
      repaint();
    }
  }
  
  /**
   * Get the current scale factor of the panel view.
   * @return the current scale factor of the panel view.
   * @see #setScale(double)
   * @see #getTranslation()
   */
  public double getScale(){
    return transform.getScaleX();
  }
  
  /**
   * Set the scale factor to apply to the panel view.
   * @param zoom the current scale factor of the panel view.
   * @see #getScale()
   * @see #setTranslation(Point2D)
   */
  public void setScale(double zoom){
    
    if (scale != zoom){
      
      double oldValue = this.scale;
      
      this.scale = zoom;
      //transform.scale((1.0d/transform.getScaleX())*scale, (1.0d/transform.getScaleY())*scale);
      transform.setTransform(scale,                      // The X coordinate scaling element of the 3x3 matrix
                             transform.getShearY(),      // the Y coordinate shearing element of the 3x3 matrix
                             transform.getShearX(),      // the X coordinate shearing element of the 3x3 matrix
                             scale,                      // the Y coordinate scaling element of the 3x3 matrix
                             transform.getTranslateX(),  // the X coordinate translation element of the 3x3 matrix
                             transform.getTranslateY()); // the Y coordinate translation element of the 3x3 matrix
      
      firePropertyChange(SCALE_PROPERTY, oldValue, this.scale);
      
      if (autoRepaint){
        repaint();
      }
    }
  }
  
  /**
   * Get the translation applied to the view as a {@link Point2D 2D point}.
   * @return the translation applied to the view.
   * @see #setTranslation(Point2D)
   * @see #getScale()
   */
  public Point2D getTranslation(){
    Point2D p = new Point2D.Double();
    p.setLocation(transform.getTranslateX(), transform.getTranslateY());
    return p;
  }
  
  /**
   * Set the translation to apply to the view.
   * @param translation the translation to apply to the view as a {@link Point2D 2D point}.
   * @see #getTranslation()
   * @see #setScale(double)
   */
  public void setTranslation(Point2D translation){
    if (translation != null){
      
      //transform.translate(-transform.getTranslateX(), -transform.getTranslateY());
      //transform.translate(translation.getX(), translation.getY());

      transform.setTransform(transform.getScaleX(), // The X coordinate scaling element of the 3x3 matrix
                             transform.getShearY(), // the Y coordinate shearing element of the 3x3 matrix
                             transform.getShearX(), // the X coordinate shearing element of the 3x3 matrix
                             transform.getScaleY(), // the Y coordinate scaling element of the 3x3 matrix
                             translation.getX(),    // the X coordinate translation element of the 3x3 matrix
                             translation.getY());   // the Y coordinate translation element of the 3x3 matrix
      
      if (autoRepaint){
        repaint();
      }
    }
  }
  
  /**
   * Fit the view in order to display the whole {@link #getImage() image}. 
   * This method update the view transformation (scale, translation) in order to make the whole image visible.
   */
  public void fit(){
    
    if (image != null){

      boolean b = autoRepaint;

      autoRepaint = false;
      setTranslation(new Point2D.Double(0.0d, 0.0d));
      setScale(Math.min(((float)getWidth()) / ((float)image.getWidth()), ((float)getHeight()) / ((float)image.getHeight())));

      repaint();
      autoRepaint = b;
    }

  }
  
  /**
   * Check if the given <code>layer</code> is displayed. 
   * If this method return <code>true</code>, then the features that are within the layer are visible on the image panel. 
   * @param layer the layer to check.
   * @return <code>true</code> if the layer is displayed and <code>false</code> otherwise.
   * @see #setLayerDisplayed(String, boolean)
   */
  public boolean isLayerDisplayed(String layer) {
    if (layersVisibility != null) {
      if (layersVisibility.get(layer) != null) {
        return layersVisibility.get(layer);
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
  
  /**
   * Set if the given <code>layer</code> has to be displayed. 
   * @param layer the layer to check.
   * @param displayed <code>true</code> if the layer has to be displayed and <code>false</code> otherwise.
   */
  public void setLayerDisplayed(String layer, boolean displayed) {
    if (layers != null) {
      if (layers.get(layer) != null) {
        
        if (layersVisibility == null) {
          layersVisibility = new HashMap<String, Boolean>();
        }
        
        layersVisibility.put(layer, displayed);
      }
    }
  }
  
  /**
   * Get the {@link JImageFeature image features} contained within the specified <code>layer</code> attached to this panel.
   * @param layer the layer of the features.
   * @return the image features attached to this panel and <code>null</code> if the layer is empty or does not exists.
   * @see #setImageFeatures(String, List)
   */
  public List<JImageFeature> getImageFeatures(String layer){
    
    if (layers != null) {
      return layers.get(layer);
    } else {
      return null;
    }
  }
  
  /**
   * Set the {@link JImageFeature image features} to attach to this panel within the specified <code>layer</code>. 
   * If the <code>layer</code> does not exist, it is created. In this case, the layer will not be displayed until a call to {@link #setLayerDisplayed(String, boolean) setLayerDisplayed(layer, true)}. 
   * If the layer exists, all previous existing features are deleted.
   * @param layer the layer of the features.
   * @param features the image features attached to attach to this panel.
   * @see #getImageFeatures(String)
   */
  public void setImageFeatures(String layer, List<JImageFeature> features){
    
    if (layers == null) {
      layers = new HashMap<String, List<JImageFeature>>();
    }
    
    layers.put(layer, features);
  }
  
  /**
   * Attach the given {@link JImageFeature image feature} to this panel within the specified <code>layer</code>.
   * If the <code>layer</code> does not exist, it is created. In this case, the layer will not be displayed until a call to {@link #setLayerDisplayed(String, boolean) setLayerDisplayed(layer, true)}. 
   * @param layer the layer of the feature.
   * @param feature the {@link JImageFeature image feature} to attach.
   * @return <code>true</code> if the feature is successfully attached and <code>false</code> otherwise.
   * @see #removeImageFeature(String, JImageFeature)
   */
  public boolean addImageFeature(String layer, JImageFeature feature){
    
    if (feature != null){
      if (layers == null) {
        layers = new HashMap<String, List<JImageFeature>>();
      }
      
      if (layers.get(layer) == null) {
        layers.put(layer, new ArrayList<JImageFeature>());
      }
      
      layers.get(layer).add(feature);
      
      return true;

    } else {
      return false;
    }
  }
  
  /**
   * Remove the given {@link JImageFeature image feature} from the specified <code>layer</code>.
   * @param layer the layer of the feature.
   * @param feature the {@link JImageFeature image feature} to remove.
   * @return <code>true</code> if the feature is successfully removed and <code>false</code> otherwise.
   * @see #addImageFeature(String, JImageFeature)
   */
  public boolean removeImageFeature(String layer, JImageFeature feature){
    if (feature == null){
      return false;
    } else {
      
      if (layers != null){

        if (layers.get(layer) != null) {
          return layers.get(layer).remove(feature);
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
  }
  
  /**
   * Get the coordinate within the image referential of the point located at (<code>x</code>, <code>y</code>) on the panel component.
   * @param x the x coordinate of the point on the panel component.
   * @param y the y coordinate of the point on the panel component.
   * @return the coordinate within the image of the point located at (x, y) on the panel component.
   * @see #getViewCoordinate(float, float)
   */
  public Point2D getImageCoordinate(int x, int y){
    if (image != null){
      if ((x >= 0) && (x < getWidth()) && (y >= 0) && (y < getHeight())){
        Point2D.Float dest = new Point2D.Float();
        Point2D.Float src = new Point2D.Float(x, y);
        
        try {
          transform.inverseTransform(src, dest);
          
          if ((dest.getX() < 0)||(dest.getX() >= image.getWidth() - 1) || (dest.getY() < 0)||(dest.getY() >= image.getHeight() - 1)){
            dest = null;
          }
          
        } catch (NoninvertibleTransformException e) {
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
   * Get the coordinate within the view referential of the point located at (<code>x</code>, <code>y</code>) on the image.
   * @param x the x coordinate of the point on the image.
   * @param y the y coordinate of the point on the image.
   * @return the coordinate within the view referential of the point located at (<code>x</code>, <code>y</code>) on the image.
   * @see #getImageCoordinate(int, int)
   */
  public Point2D getViewCoordinate(float x, float y){
    if (image != null) {
      if (transform != null) {
        
        Point2D.Float dest = new Point2D.Float();
        Point2D.Float src = new Point2D.Float(x, y);
        
        transform.transform(src, dest);
        
        return dest;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
