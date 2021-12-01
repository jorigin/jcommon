package org.jorigin.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jorigin.Common;
import org.jorigin.lang.LangResourceBundle;

/**
 * A panel that display a {@link java.awt.GraphicsEnvironment GraphicsEnvironment}. The graphics environment is an object that represents 
 * the system graphics capabilities.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.2
 */
public class GraphicsEnvironmentPanel extends JPanel{

  private static final long serialVersionUID = Common.BUILD;
  
  private LangResourceBundle lr               = (LangResourceBundle) LangResourceBundle.getBundle(new Locale(System.getProperty("user.language"), System.getProperty("user.country"))); 

  private float displayedFontSize             = 14.0f;
  
  private GraphicsEnvironment ge              = null;
  
  private Point centerPoint                   = null;
  
  private Rectangle maximumWindowBounds       = null;
  
  private Font[] fonts                        = null;
  
  private GraphicsDevice[] devices            = null;
  
  private JLabel centerPointLB                = null;
  
  private JTextField centerPointTF            = null;
  
  private JLabel maximumWindowBoundsLB        = null;
  
  private JTextField maximumWindowBoundsTF    = null;
  
  private JPanel geometryPN                   = null;
  
  private JList<Font> fontsList               = null;
  
  private JScrollPane fontsListSP             = null;
  
  private JPanel fontsPN                      = null;
  
  private JLabel displayLB                    = null;
  
  private JComboBox<GraphicsDevice> displayCB = null;
  
  private JPanel displayPN                    = null;
  
//  private JCheckBox isFullScreenCH           = null;
  
//  private JCheckBox isDisplayChangeCH        = null;
  
  /**
   * Create a new panel displaying the default {@link java.awt.GraphicsEnvironment GraphicsEnvironment}. 
   * The <code>GraphicsEnvironment</code> is obtained by <code>GraphicsEnvironment.getLocalGraphicsEnvironment()</code>
   */
  public GraphicsEnvironmentPanel(){
    this(null);
  }
  
  /**
   * Create a new panel displaying the given {@link java.awt.GraphicsEnvironment GraphicsEnvironment}. 
   * If the given environment is <code>null</code>, the default <code>GraphicsEnvironment</code> is 
   * obtained by <code>GraphicsEnvironment.getLocalGraphicsEnvironment()</code>
   * @param ge the GraphicsEnvironment to display.
   */
  public GraphicsEnvironmentPanel(GraphicsEnvironment ge){
    if (ge != null){
      this.setGraphicsEnvironment(ge);
    } else {
      this.setGraphicsEnvironment(GraphicsEnvironment.getLocalGraphicsEnvironment());
    }
    
    centerPoint         = this.ge.getCenterPoint();
    maximumWindowBounds = this.ge.getMaximumWindowBounds();
    
    fonts = this.ge.getAllFonts();
    
    devices = this.ge.getScreenDevices();
    
    for(int i = 0; i < devices.length; i++){
      Common.logger.log(Level.INFO, ""+devices[i].getIDstring());
      Common.logger.log(Level.INFO, ""+devices[i].getType());
      Common.logger.log(Level.INFO, ""+devices[i].getDisplayMode());
      Common.logger.log(Level.INFO, ""+devices[i].getAvailableAcceleratedMemory());
      
      devices[i].isDisplayChangeSupported();
      devices[i].isFullScreenSupported();
      
      Common.logger.log(Level.INFO, ""+devices[i].getDisplayMode().getBitDepth());
      Common.logger.log(Level.INFO, ""+devices[i].getDisplayMode().getHeight());
      Common.logger.log(Level.INFO, ""+devices[i].getDisplayMode().getWidth());
      Common.logger.log(Level.INFO, ""+devices[i].getDisplayMode().getRefreshRate());
      
      devices[i].isDisplayChangeSupported();
      devices[i].isFullScreenSupported();
    }
    
    initGUI();
    refreshGUI();
  }
  
  /**
   * Initialize the panel GUI components.
   */
  protected void initGUI(){
    
    GridBagConstraints c = null;
    
    Insets labelInsets = new Insets(3, 6, 3, 0);
    Insets fieldInsets = new Insets(3, 0, 3, 6);
    
    centerPointLB = new JLabel(lr.getString("GRAPHICSENV_CENTER_POINT")+": ");
    centerPointTF = new JTextField();
    centerPointTF.setText(""+((int)centerPoint.getX())+", "+((int)centerPoint.getY()));
    centerPointTF.setEditable(false);
    
    maximumWindowBoundsLB = new JLabel(lr.getString("GRAPHICSENV_MAX_W_BOUNDS")+": ");
    maximumWindowBoundsTF = new JTextField();
    maximumWindowBoundsTF.setText(""+((int)maximumWindowBounds.getWidth())+"x"+((int)maximumWindowBounds.getHeight())+" px");
    maximumWindowBoundsTF.setEditable(false);
    
    geometryPN = new JPanel();
    geometryPN.setBorder(BorderFactory.createTitledBorder(lr.getString("GRAPHICSENV_GEOM")));
    geometryPN.setLayout(new GridBagLayout());
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHEAST;
    geometryPN.add(centerPointLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHWEST;
    geometryPN.add(centerPointTF, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHEAST;
    geometryPN.add(maximumWindowBoundsLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHWEST;
    geometryPN.add(maximumWindowBoundsTF, c);
    
    fontsList = new JList<Font>(fonts);
    fontsList.setCellRenderer(new DefaultListCellRenderer(){

      private static final long serialVersionUID = Common.BUILD;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        Component comp = super.getListCellRendererComponent(list, ((Font)value).getName(), index, isSelected, cellHasFocus);
        Font font = ((Font)value).deriveFont(displayedFontSize);
        comp.setFont(font);
        return comp;
      }});
    
    fontsListSP = new JScrollPane(fontsList);
    
    fontsPN = new JPanel();
    fontsPN.setBorder(BorderFactory.createTitledBorder(lr.getString("GRAPHICSENV_FONTS")));
    fontsPN.setLayout(new BorderLayout());
    fontsPN.add(fontsListSP, BorderLayout.CENTER);
    
    
    displayLB = new JLabel(lr.getString("GRAPHICSENV_DISPLAY_ID")+": ");
    
    displayCB = new JComboBox<GraphicsDevice>(devices);
    displayCB.setRenderer(new DefaultListCellRenderer(){

      private static final long serialVersionUID = Common.BUILD;
      
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        
        String str = ((GraphicsDevice)value).getIDstring();
        
        switch(((GraphicsDevice)value).getType()){
          case GraphicsDevice.TYPE_IMAGE_BUFFER:
            str+=" ("+lr.getString("GRAPHICSENV_DISPLAY_TYPE_IMAGE")+")";
            break;
          case GraphicsDevice.TYPE_PRINTER:
            str+=" ("+lr.getString("GRAPHICSENV_DISPLAY_TYPE_PRINTER")+")";
            break;
          case GraphicsDevice.TYPE_RASTER_SCREEN:
            str+=" ("+lr.getString("GRAPHICSENV_DISPLAY_TYPE_SCREEN")+")";
            break;
        }
        
        Component comp = super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
        
        return comp;
      }
    });
    
    
    displayPN = new JPanel();
    displayPN.setBorder(BorderFactory.createTitledBorder(lr.getString("GRAPHICSENV_DISPLAY")));
    displayPN.setLayout(new BorderLayout());
    displayPN.add(displayLB, BorderLayout.WEST);
    displayPN.add(displayCB, BorderLayout.CENTER);
    
    setLayout(new GridBagLayout());
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.BOTH;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHEAST;
    add(geometryPN, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = 1;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.BOTH;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHEAST;
    add(displayPN, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 2;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.BOTH;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.NORTHEAST;
    add(fontsPN, c); 
    
    
  }
  
  /**
   * Refresh the panel GUI components.
   */
  protected void refreshGUI(){
    
  }

  /**
   * Set the {@link java.awt.GraphicsEnvironment GraphicsEnvironment} displayed within this panel.
   * @param ge the GraphicsEnvironment displayed
   */
  public void setGraphicsEnvironment(GraphicsEnvironment ge) {
    this.ge = ge;
  }

  /**
   * Get the {@link java.awt.GraphicsEnvironment GraphicsEnvironment} displayed within this panel.
   * @return the GraphicsEnvironment displayed
   */
  public GraphicsEnvironment getGraphicsEnvironment() {
    return ge;
  }
}
