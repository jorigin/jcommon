package org.jorigin.gui;

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
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @version 1.0.0
 */
public class JImageFrame extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private JImagePanel imagePanel = null;
  
  private JImagePanelToolBar imageToolbar = null;
  
  private JLabel panelCoordLB = null;
  private JLabel panelCoordTF = null;
  
  private JLabel imageCoordLB = null;
  private JLabel imageCoordTF = null;
  
  private JPanel northPanel = null;
  private JPanel southPanel = null;
  
  private NumberFormat imagePixelFormat = null;
  
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
    
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
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
    
    imagePanel = new JImagePanel(image);
    
    imageToolbar = new JImagePanelToolBar(imagePanel);
    
    imagePixelFormat = NumberFormat.getNumberInstance(Locale.US);
    imagePixelFormat.setMaximumFractionDigits(3);
    
    panelPixelFormat = NumberFormat.getNumberInstance(Locale.US);
    panelPixelFormat.setMaximumFractionDigits(0);
    
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
    
    imagePanel = new JImagePanel(image);
    
    imageToolbar = new JImagePanelToolBar(imagePanel);
    
    imagePixelFormat = NumberFormat.getNumberInstance(Locale.US);
    imagePixelFormat.setMaximumFractionDigits(3);
    
    panelPixelFormat = NumberFormat.getNumberInstance(Locale.US);
    panelPixelFormat.setMaximumFractionDigits(0);
    
    initGUI();
    
    refreshGUI();
  }
  
  /**
   * Initialization of the Graphical User Interface components.
   */
  protected void initGUI(){
    
    imagePanel.addMouseMotionListener(new MouseMotionListener(){

      @Override
      public void mouseDragged(MouseEvent e) {
        panelCoordTF.setText("("+panelPixelFormat.format(e.getX())+", "+panelPixelFormat.format(e.getY())+")");
        
        Point2D imageCoord = imagePanel.getImageCoordinate(e.getX(), e.getY());
        
        if (imageCoord != null){
          imageCoordTF.setText("("+imagePixelFormat.format(imageCoord.getX())+", "+imagePixelFormat.format(imageCoord.getY())+")");
        } else {
          imageCoordTF.setText("(Out of bounds)");
        }
        
        
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        panelCoordTF.setText("("+panelPixelFormat.format(e.getX())+", "+panelPixelFormat.format(e.getY())+")");
        
        Point2D imageCoord = imagePanel.getImageCoordinate(e.getX(), e.getY());
        
        if (imageCoord != null){
          imageCoordTF.setText("("+imagePixelFormat.format(imageCoord.getX())+", "+imagePixelFormat.format(imageCoord.getY())+")");
        } else {
          imageCoordTF.setText("(Out of bounds)");
        }
      }
      
    });
    
    northPanel = new JPanel();
    northPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
    
    southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
    
    panelCoordLB = new JLabel("Panel coords: ");
    panelCoordTF = new JLabel("");
    
    imageCoordLB = new JLabel("Image coords: ");
    imageCoordTF = new JLabel("");
    
    
    
    getContentPane().setLayout(new BorderLayout());
    
    northPanel.add(imageToolbar);

    southPanel.add(panelCoordLB);
    southPanel.add(panelCoordTF);
    southPanel.add(imageCoordLB);
    southPanel.add(imageCoordTF);
    
    getContentPane().add(imagePanel, BorderLayout.CENTER);
    getContentPane().add(northPanel, BorderLayout.NORTH);
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    
    
  }
  
  /**
   * Refresh of the Graphical User Interface components.
   */
  protected void refreshGUI(){
    imageToolbar.refreshGUI();
  }
  
  /**
   * Get the {@link BufferedImage image} that is displayed within the frame.
   * @return the image that is displayed within the frame.
   * @see #setImage(BufferedImage)
   */
  public BufferedImage getImage(){
    return imagePanel.getImage();
  }
  
  /**
   * Set the {@link BufferedImage image} to display within the frame.
   * @param image the image to display within the frame.
   * @see #getImage()
   */
  public void setImage(BufferedImage image){
    imagePanel.setImage(image);
  }
  
  /**
   * Get the {@link JImageFeature image features} that are attached to the underlying {@link JImagePanel image panel}.
   * @return the {@link JImageFeature image features} that are attached to the underlying image panel.
   * @see #setImageFeatures(List)
   */
  public List<JImageFeature> getImageFeatures(){
    return imagePanel.getImageFeatures();
  }
  
  /**
   * Set the {@link JImageFeature image features} to attach to the underlying {@link JImagePanel image panel}.
   * @param features the {@link JImageFeature image features} to attach to the underlying {@link JImagePanel image panel}.
   * @see #getImageFeatures()
   */
  public void setImageFeatures(List<JImageFeature> features){
    imagePanel.setImageFeatures(features);
  }
  
  /**
   * Get if the underlying image panel fits the view when its resized.
   * @return <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #setAutoFit(boolean)
   * @see #fit()
   */
  public boolean isAutoFit() {
    return imagePanel.isAutoFit();
  }
  
  /**
   * Set if the underlying image panel has to fit the view when its resized.
   * @param autoFit <code>true</code> if the panel fits the view when its resized and <code>false</code> otherwise.
   * @see #isAutoFit()
   * @see #fit()
   */
  public void setAutoFit(boolean autoFit) {
    imagePanel.setAutoFit(autoFit);
  }
  
  /**
   * Fit the actual view to display the whole image.
   */
  public void fit(){
    imagePanel.fit();
  }
}

