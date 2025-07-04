package org.jorigin.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jorigin.Common;

import java.awt.Dimension;
import java.awt.Point;


/**
 * A {@link JFrame swing component} that enables to display {@link BufferedImage images} by embedding a {@link JImagePanel JImagePanel}. 
 * <p>
 * This frame provides various operations on the view (zoom, moving, ...) and helper methods for correspondence between image space and display space.
 * </p>
 * <p>
 * This underlying image panel also manages {@link JImageFeature features} that enable to display interactive overlays on the panel.
 * </p>
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class JImageFrame extends JFrame {

  /**
   * Serial version UID.
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 201903071000L;

  /**
   * The underlying {@link JImagePanel image panel} that displays the image.
   * @see #getImage()
   * @see #setImage(BufferedImage)
   */
  private JImagePanel imagePanel = null;
  
  /**
   * The {@link JImagePanelToolBar image panel toolbar} that provides various operations on the view.
   * @see #getImageFeatures(String)
   * @see #setImageFeatures(String, List)
   */
  private JImagePanelToolBar imageToolbar = null;
  
  /**
   * The label and text field that display the coordinates of the mouse pointer in the panel space.
   */
  private JLabel panelCoordLB = null;
  
  /**
   * The label and text field that display the coordinates of the mouse pointer in the image space.
   */
  private JLabel panelCoordTF = null;
  
  /**
   * The label and text field that display the coordinates of the mouse pointer in the image space.
   * <p>
   * The coordinates are expressed in pixels.
   * </p>
   */
  private JLabel imageCoordLB = null;
  
  /**
   * The label and text field that display the coordinates of the mouse pointer in the image space.
   * <p>
   * The coordinates are expressed in pixels.
   * </p>
   */
  private JLabel imageCoordTF = null;
  
  /**
   * The panel that holds the north components of the frame.
   * <p>
   * This panel contains the {@link JImagePanelToolBar image panel toolbar}.
   * </p>
   */
  private JPanel northPanel = null;
  
  /**	
   * The panel that holds the south components of the frame.
   * <p>
   * This panel contains the labels and text fields that display the coordinates of the mouse pointer in the panel and image spaces.
   * </p>
   */
  private JPanel southPanel = null;
  
  /**
   * The number format used to display the coordinates of the mouse pointer in the image space.
   * <p>
   * The coordinates are expressed in pixels.
   * </p>
   */
  private NumberFormat imagePixelFormat = null;
  
  /**
   * The number format used to display the coordinates of the mouse pointer in the panel space.
   * <p>
   * The coordinates are expressed in pixels.
   * </p>
   */
  private NumberFormat panelPixelFormat = null;
  
  /**
   * Create and display an image frame.
   * @param title the title of the frame.
   * @param size the size (width &times; height) of the frame in pixels.
   * @param location the location (x, y) of the frame on the display.
   * @param image the image to display.
   * @param autoFit <code>true</code> if the frame has to fit the view when resized and <code>false</code> otherwise.
   */
  public static void showImageFrame(String title, Dimension size, Point location, BufferedImage image, boolean autoFit) {
    JImageFrame frame = new JImageFrame(title, size, image);
    
    frame.setAutoFit(autoFit);
    
    frame.setLocation(location);
    
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    frame.setVisible(true);
  }
  
  /**
   * Create a new frame dedicated to image display.
   */
  public JImageFrame(){
    this(null);
  }
  
  /**
   * Create a new frame that displays the given <code>image</code>.
   * @param image the image to display.
   */
  public JImageFrame(BufferedImage image){
    super();
    
    this.imagePanel = new JImagePanel(image);
    
    this.imageToolbar = new JImagePanelToolBar(this.imagePanel);
    
    this.imagePixelFormat = NumberFormat.getNumberInstance(Locale.US);
    this.imagePixelFormat.setMaximumFractionDigits(3);
    
    this.panelPixelFormat = NumberFormat.getNumberInstance(Locale.US);
    this.panelPixelFormat.setMaximumFractionDigits(0);
    
    initGUI();
    
    refreshGUI();
  }
  
  /**
   * Create a new frame that displays the given <code>image</code>.
   * @param title the title of the frame.
   * @param size the size of the frame in pixels.
   * @param image the image to display.
   */
  public JImageFrame(String title, Dimension size, BufferedImage image) {
    super();
    
    setTitle(title);
    setSize(size);
    
    this.imagePanel = new JImagePanel(image);
    
    this.imageToolbar = new JImagePanelToolBar(this.imagePanel);
    
    this.imagePixelFormat = NumberFormat.getNumberInstance(Locale.US);
    this.imagePixelFormat.setMaximumFractionDigits(3);
    
    this.panelPixelFormat = NumberFormat.getNumberInstance(Locale.US);
    this.panelPixelFormat.setMaximumFractionDigits(0);
    
    initGUI();
    
    refreshGUI();
  }
  
  /**
   * Initialization of the Graphical User Interface components.
   */
  protected void initGUI(){
    
    this.imagePanel.addMouseMotionListener(new MouseMotionListener(){

      @Override
      public void mouseDragged(MouseEvent e) {
        JImageFrame.this.panelCoordTF.setText("("+JImageFrame.this.panelPixelFormat.format(e.getX())+", "+JImageFrame.this.panelPixelFormat.format(e.getY())+")");
        
        Point2D imageCoord = JImageFrame.this.imagePanel.getImageCoordinate(e.getX(), e.getY());
        
        if (imageCoord != null){
          JImageFrame.this.imageCoordTF.setText("("+JImageFrame.this.imagePixelFormat.format(imageCoord.getX())+", "+JImageFrame.this.imagePixelFormat.format(imageCoord.getY())+")");
        } else {
          JImageFrame.this.imageCoordTF.setText("(Out of bounds)");
        }
        
        
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        JImageFrame.this.panelCoordTF.setText("("+JImageFrame.this.panelPixelFormat.format(e.getX())+", "+JImageFrame.this.panelPixelFormat.format(e.getY())+")");
        
        Point2D imageCoord = JImageFrame.this.imagePanel.getImageCoordinate(e.getX(), e.getY());
        
        if (imageCoord != null){
          JImageFrame.this.imageCoordTF.setText("("+JImageFrame.this.imagePixelFormat.format(imageCoord.getX())+", "+JImageFrame.this.imagePixelFormat.format(imageCoord.getY())+")");
        } else {
          JImageFrame.this.imageCoordTF.setText("(Out of bounds)");
        }
      }
      
    });
    
    this.northPanel = new JPanel();
    this.northPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
    
    this.southPanel = new JPanel();
    this.southPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
    
    this.panelCoordLB = new JLabel("Panel coords: ");
    this.panelCoordTF = new JLabel("");
    
    this.imageCoordLB = new JLabel("Image coords: ");
    this.imageCoordTF = new JLabel("");
    
    
    
    getContentPane().setLayout(new BorderLayout());
    
    this.northPanel.add(this.imageToolbar);

    this.southPanel.add(this.panelCoordLB);
    this.southPanel.add(this.panelCoordTF);
    this.southPanel.add(this.imageCoordLB);
    this.southPanel.add(this.imageCoordTF);
    
    getContentPane().add(this.imagePanel, BorderLayout.CENTER);
    getContentPane().add(this.northPanel, BorderLayout.NORTH);
    getContentPane().add(this.southPanel, BorderLayout.SOUTH);
    
    
  }
  
  /**
   * Refresh of the Graphical User Interface components.
   */
  protected void refreshGUI(){
    this.imageToolbar.refreshGUI();
  }
  
  /**
   * Get the {@link BufferedImage image} that is displayed within the frame.
   * @return the image that is displayed within the frame.
   * @see #setImage(BufferedImage)
   */
  public BufferedImage getImage(){
    return this.imagePanel.getImage();
  }
  
  /**
   * Set the {@link BufferedImage image} to display within the frame.
   * @param image the image to display within the frame.
   * @see #getImage()
   */
  public void setImage(BufferedImage image){
    this.imagePanel.setImage(image);
  }
  
  /**
   * Get the {@link JImageFeature image features} that are attached to the underlying {@link JImagePanel image panel}.
   * If the layer does not exist, it is created. 
   * In this case, the layer will not be displayed until a call to {@link #setLayerDisplayed(String, boolean) setLayerDisplayed(layer, true)}.
   * If the layer exists, all previous existing features are deleted.
   * @param layer the layer that holds the features.
   * @return the {@link JImageFeature image features} that are attached to the underlying image panel.
   * @see #setImageFeatures(String, List)
   */
  public List<JImageFeature> getImageFeatures(String layer){
    return this.imagePanel.getImageFeatures(layer);
  }
  
  /**
   * Set the {@link JImageFeature image features} to attach to the underlying {@link JImagePanel image panel}.
   * @param layer the layer that holds the features.
   * @param features the {@link JImageFeature image features} to attach to the underlying {@link JImagePanel image panel}.
   * @see #getImageFeatures(String)
   */
  public void setImageFeatures(String layer, List<JImageFeature> features){
    this.imagePanel.setImageFeatures(layer, features);
  }
  
  /**
   * Get if the <code>layer</code> is actually displaying within the underlying {@link JImagePanel image panel}.
   * @param layer the layer to check.
   * @return <code>true</code> if the <code>layer</code> is actually displaying and <code>false</code> otherwise.
   * @see #setLayerDisplayed(String, boolean)
   */
  public boolean isLayerDisplayed(String layer) {
	  return this.imagePanel.isLayerDisplayed(layer);
  }
  
  /**
   * Set if the <code>layer</code> has to be displayed within the underlying {@link JImagePanel image panel}.
   * @param layer the layer to set.
   * @param displayed <code>true</code> if the <code>layer</code> has to be displayed and <code>false</code> otherwise.
   * @see #isLayerDisplayed(String)
   */
  public void setLayerDisplayed(String layer, boolean displayed) {
	  this.imagePanel.setLayerDisplayed(layer, displayed);
  }
  
  /**
   * Get if the underlying image panel fits the view when its resized.
   * @return <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #setAutoFit(boolean)
   * @see #fit()
   */
  public boolean isAutoFit() {
    return this.imagePanel.isAutoFit();
  }
  
  /**
   * Set if the underlying image panel has to fit the view when its resized.
   * @param autoFit <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #isAutoFit()
   * @see #fit()
   */
  public void setAutoFit(boolean autoFit) {
    this.imagePanel.setAutoFit(autoFit);
  }
  
  /**
   * Fit the actual view to display the whole image.
   */
  public void fit(){
    this.imagePanel.fit();
  }
}

