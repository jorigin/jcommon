/*
  This file is part of JOrigin Common Library.

    JOrigin Common is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JOrigin Common is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JOrigin Common.  If not, see <http://www.gnu.org/licenses/>.
    
*/
package org.jorigin.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jorigin.Common;
import org.jorigin.lang.LangResourceBundle;

/**
 * A Panel designed for editing java {@link java.awt.RenderingHints Renderint hints}.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.1
 */
public class JRenderingHintsPanel extends JPanel {
 
  private static final long serialVersionUID         = Common.BUILD;
  
  private LangResourceBundle lr              = (LangResourceBundle) LangResourceBundle.getBundle(new Locale(System.getProperty("user.language"), System.getProperty("user.country"))); 

  private boolean showBorder                 = false;
  /**
   * The rendering hints to edit
   */
  private RenderingHints hints = null;
  
  private Object[] alphaInterpolationValues = new Object[]{RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,
                                                           RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,
                                                           RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY};
  
  private Object[] antiAliasingValues       = new Object[]{RenderingHints.VALUE_ANTIALIAS_DEFAULT,
                                                           RenderingHints.VALUE_ANTIALIAS_OFF,
                                                           RenderingHints.VALUE_ANTIALIAS_ON};
  
  private Object[] colorRenderingValues     = new Object[]{RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
                                                           RenderingHints.VALUE_COLOR_RENDER_SPEED,
                                                           RenderingHints.VALUE_COLOR_RENDER_QUALITY};
  
  private Object[] ditheringValues          = new Object[]{RenderingHints.VALUE_DITHER_DEFAULT,
                                                           RenderingHints.VALUE_DITHER_DISABLE,
                                                           RenderingHints.VALUE_DITHER_ENABLE};
  
  
  private Object[] fractionalMetricsValues  = new Object[]{RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
                                                           RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
                                                           RenderingHints.VALUE_FRACTIONALMETRICS_ON};  
  
  private Object[] interpolationValues      = new Object[]{RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
                                                           RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                                                           RenderingHints.VALUE_INTERPOLATION_BICUBIC};
  
  private Object[] renderingValues          = new Object[]{RenderingHints.VALUE_RENDER_DEFAULT,
                                                           RenderingHints.VALUE_RENDER_SPEED,
                                                           RenderingHints.VALUE_RENDER_QUALITY};
  
  private Object[] strokeControlValues      = new Object[]{RenderingHints.VALUE_STROKE_DEFAULT,
                                                           RenderingHints.VALUE_STROKE_PURE,
                                                           RenderingHints.VALUE_STROKE_NORMALIZE};
  
  private Object[] textAntiAliasingValues   = new Object[]{RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_GASP,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB,
                                                           RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR};

  private JLabel alphaInterpolationLB       = null;
  private JLabel antiAliasingLB             = null;
  private JLabel colorRenderingLB           = null;
  private JLabel ditheringLB                = null;
  private JLabel fractionalLB               = null; 
  private JLabel interpolationLB            = null;
  private JLabel renderingLB                = null;
  private JLabel strokeControlLB            = null;
  private JLabel textAntiAliasingLB         = null;
  private JLabel textLCDContrastLB          = null;
  
  private JComboBox<Object> alphaInterpolationCB = null;
  private JComboBox<Object> antiAliasingCB       = null;
  private JComboBox<Object> colorRenderingCB     = null;
  private JComboBox<Object> ditheringCB          = null;
  private JComboBox<Object> fractionalCB         = null; 
  private JComboBox<Object> interpolationCB      = null;
  private JComboBox<Object> renderingCB          = null;
  private JComboBox<Object> strokeControlCB      = null;
  private JComboBox<Object> textAntiAliasingCB   = null;
  private JSpinner  textLCDContrastSI            = null;
  
  private boolean isListening               = true;
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  /**
   * Create a new {@link java.awt.RenderingHints Rendering Hints} editor panel.
   * @param hints the rendering hints to edit.
   */
  public JRenderingHintsPanel(RenderingHints hints){

    this(hints, false);
  }
  
  /**
   * Create a new {@link java.awt.RenderingHints Rendering Hints} editor panel. The boolean <code>showTitle</code>
   * specify if the panel has a titled border or not.
   * @param hints the rendering hints to edit.
   * @param showTitle <code>true</code> if the panel has a titled border, <code>false</code> otherwise.
   */
  public JRenderingHintsPanel(RenderingHints hints, boolean showTitle){

    if (hints != null){
      this.hints = hints;
    } else {
      hints = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
    }

    showBorder = showTitle;
    
    initGUI();
    refreshGUI();
  }
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II INITIALISATION                                           II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

  /**
   * Init the GUI
   */
  protected void initGUI(){
    
    GridBagConstraints c = null;
    Insets labelInsets = new Insets(3, 6, 3, 0);
    Insets fieldInsets = new Insets(3, 0, 3, 6);
    
    
    isListening = false;
    
    ItemListener itemListener = new ItemListener(){

      @Override
      public void itemStateChanged(ItemEvent e) {
    processItemEvent(e);
      }
      
    };
    
    alphaInterpolationLB       = new JLabel(lr.getString("GUI_RH_ALPHA_INTERPOLATION_LB")+": ");
    alphaInterpolationLB.setToolTipText(lr.getString("GUI_RH_ALPHA_INTERPOLATION_TIP"));
    
    antiAliasingLB             = new JLabel(lr.getString("GUI_RH_ANTIALIASING_LB")+": ");
    antiAliasingLB.setToolTipText(lr.getString("GUI_RH_ANTIALIASING_TIP"));
    
    colorRenderingLB           = new JLabel(lr.getString("GUI_RH_COLOR_RENDERING_LB")+": ");
    colorRenderingLB.setToolTipText(lr.getString("GUI_RH_COLOR_RENDERING_TIP"));
    
    ditheringLB                = new JLabel(lr.getString("GUI_RH_DITHERING_LB")+": ");
    ditheringLB.setToolTipText(lr.getString("GUI_RH_DITHERING_TIP"));
    
    fractionalLB               = new JLabel(lr.getString("GUI_RH_FRACTIONALMETRICS_LB")+": ");
    fractionalLB.setToolTipText(lr.getString("GUI_RH_FRACTIONALMETRICS_TIP"));
    
    interpolationLB            = new JLabel(lr.getString("GUI_RH_INTERPOLATION_LB")+": ");
    interpolationLB.setToolTipText(lr.getString("GUI_RH_INTERPOLATION_TIP"));
    
    renderingLB                = new JLabel(lr.getString("GUI_RH_RENDERING_LB")+": ");
    renderingLB.setToolTipText(lr.getString("GUI_RH_RENDERING_TIP"));
    
    strokeControlLB            = new JLabel(lr.getString("GUI_RH_STROKE_CONTROL_LB")+": ");
    strokeControlLB.setToolTipText(lr.getString("GUI_RH_STROKE_CONTROL_TIP"));
    
    textAntiAliasingLB         = new JLabel(lr.getString("GUI_RH_TEXT_ANTIALIASING_LB")+": ");
    textAntiAliasingLB.setToolTipText(lr.getString("GUI_RH_TEXT_ANTIALIASING_TIP"));
    
    textLCDContrastLB          = new JLabel(lr.getString("GUI_RH_TEXT_LCD_CONTRAST_LB")+": ");
    textLCDContrastLB.setToolTipText(lr.getString("GUI_RH_TEXT_LCD_CONTRAST_TIP"));
    
    alphaInterpolationCB = new JComboBox<Object>(alphaInterpolationValues);
    alphaInterpolationCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
                                                int index, boolean isSelected,
                                                boolean cellHasFocus){
    
    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    
    
    if (value != null){
      if (value.equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED)){
        label.setText(lr.getString("GUI_RH_VALUE_SPEED_LB"));
      } else if (value.equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)){
        label.setText(lr.getString("GUI_RH_VALUE_QUALITY_LB"));
      }
    }
    
    return label;
    
      }
    });
    alphaInterpolationCB.addItemListener(itemListener);
    
    antiAliasingCB = new JComboBox<Object>(antiAliasingValues);
    antiAliasingCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    
    if (value != null){
      if (value.equals(RenderingHints.VALUE_ANTIALIAS_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_ANTIALIAS_OFF)){
        label.setText(lr.getString("GUI_RH_VALUE_OFF_LB"));
      } else if (value.equals(RenderingHints.VALUE_ANTIALIAS_ON)){
        label.setText(lr.getString("GUI_RH_VALUE_ON_LB"));
      }
    }

    return label;

      }
    });
    antiAliasingCB.addItemListener(itemListener);
    
    colorRenderingCB   = new JComboBox<Object>(colorRenderingValues);
    colorRenderingCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_COLOR_RENDER_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_COLOR_RENDER_SPEED)){
        label.setText(lr.getString("GUI_RH_VALUE_OFF_LB"));
      } else if (value.equals(RenderingHints.VALUE_COLOR_RENDER_QUALITY)){
        label.setText(lr.getString("GUI_RH_VALUE_ON_LB"));
      }
    }

    return label;

      }
    });
    colorRenderingCB.addItemListener(itemListener);

    ditheringCB        = new JComboBox<Object>(ditheringValues);
    ditheringCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_DITHER_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_DITHER_DISABLE)){
        label.setText(lr.getString("GUI_RH_VALUE_OFF_LB"));
      } else if (value.equals(RenderingHints.VALUE_DITHER_ENABLE)){
        label.setText(lr.getString("GUI_RH_VALUE_ON_LB"));
      }
    }

    return label;

      }
    });
    ditheringCB.addItemListener(itemListener);
    
    fractionalCB       = new JComboBox<Object>(fractionalMetricsValues);
    fractionalCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_FRACTIONALMETRICS_OFF)){
        label.setText(lr.getString("GUI_RH_VALUE_OFF_LB"));
      } else if (value.equals(RenderingHints.VALUE_FRACTIONALMETRICS_ON)){
        label.setText(lr.getString("GUI_RH_VALUE_ON_LB"));
      }
    }

    return label;

      }
    });
    fractionalCB.addItemListener(itemListener);
    
    interpolationCB    = new JComboBox<Object>(interpolationValues);
    interpolationCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)){
        label.setText(lr.getString("GUI_RH_VALUE_NEAREST_NEIGHBOUR_LB"));
      } else if (value.equals(RenderingHints.VALUE_INTERPOLATION_BILINEAR)){
        label.setText(lr.getString("GUI_RH_VALUE_BILINEAR_LB"));
      } else if (value.equals(RenderingHints.VALUE_INTERPOLATION_BICUBIC)){
        label.setText(lr.getString("GUI_RH_VALUE_BICUBIC_LB"));
      }
    }

    return label;

      }
    });
    interpolationCB.addItemListener(itemListener);
    
    renderingCB        = new JComboBox<Object>(renderingValues);
    renderingCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_RENDER_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_RENDER_SPEED)){
        label.setText(lr.getString("GUI_RH_VALUE_SPEED_LB"));
      } else if (value.equals(RenderingHints.VALUE_RENDER_QUALITY)){
        label.setText(lr.getString("GUI_RH_VALUE_QUALITY_LB"));
      }
    }

    return label;

      }
    });
    renderingCB.addItemListener(itemListener);
    
    strokeControlCB    = new JComboBox<Object>(strokeControlValues);
    strokeControlCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_STROKE_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_STROKE_PURE)){
        label.setText(lr.getString("GUI_RH_VALUE_PURE_LB"));
      } else if (value.equals(RenderingHints.VALUE_STROKE_NORMALIZE)){
        label.setText(lr.getString("GUI_RH_VALUE_NORMALIZE_LB"));
      }
    }

    return label;

      }
    });
    strokeControlCB.addItemListener(itemListener);
    
    textAntiAliasingCB = new JComboBox<Object>(textAntiAliasingValues);
    textAntiAliasingCB.setRenderer(new DefaultListCellRenderer(){
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public Component getListCellRendererComponent(JList<?> list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus){

    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value != null){
      if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)){
        label.setText(lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP)){
        label.setText(lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_GASP_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR)){
        label.setText(lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_HBGR_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)){
        label.setText(lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_HRGB_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR)){
        label.setText(lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_VBGR_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB)){
        label.setText(lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_VRGB_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)){
        label.setText(lr.getString("GUI_RH_VALUE_OFF_LB"));
      } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON)){
        label.setText(lr.getString("GUI_RH_VALUE_ON_LB"));
      }
    }

    return label;

      }
    });
    textAntiAliasingCB.addItemListener(itemListener);
/*    
    int contrastValue = 140;
    if (hints.keySet().contains(RenderingHints.KEY_TEXT_LCD_CONTRAST)){
      contrastValue = (Integer) hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST);
    } 
*/  
    SpinnerModel sopacmodel  = new SpinnerNumberModel(140, //initial value
        100, //min
        250, //max
        1);//step
    textLCDContrastSI  = new JSpinner(sopacmodel);
    textLCDContrastSI.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    processChangeEvent(e);
      }});
    
    
    setLayout(new GridBagLayout());
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(renderingLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(renderingCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(colorRenderingLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(colorRenderingCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(interpolationLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(interpolationCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(alphaInterpolationLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(alphaInterpolationCB, c);

    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(antiAliasingLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(antiAliasingCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(ditheringLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(ditheringCB, c);
     
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(fractionalLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(fractionalCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(strokeControlLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(strokeControlCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(textAntiAliasingLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(textAntiAliasingCB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.NONE;
    c.insets    = labelInsets;
    c.weightx   = 0.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.EAST;
    add(textLCDContrastLB, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0.0;
    c.anchor    = GridBagConstraints.WEST;
    add(textLCDContrastSI, c);
    
    
    if (showBorder){
      setBorder(BorderFactory.createTitledBorder(lr.getString("GUI_RH_ID")));
    }
    
    isListening = true;
    
  }
  
  /**
   * Refresh the GUI
   */
  public void refreshGUI(){
    isListening = false;
    
    if (hints != null){
      renderingCB.setSelectedItem(hints.get(RenderingHints.KEY_RENDERING));
      colorRenderingCB.setSelectedItem(hints.get(RenderingHints.KEY_COLOR_RENDERING));
      interpolationCB.setSelectedItem(hints.get(RenderingHints.KEY_INTERPOLATION));
      alphaInterpolationCB.setSelectedItem(hints.get(RenderingHints.KEY_ALPHA_INTERPOLATION));
      antiAliasingCB.setSelectedItem(hints.get(RenderingHints.KEY_ANTIALIASING));
      ditheringCB.setSelectedItem(hints.get(RenderingHints.KEY_DITHERING));
      fractionalCB.setSelectedItem(hints.get(RenderingHints.KEY_FRACTIONALMETRICS));
      strokeControlCB.setSelectedItem(hints.get(RenderingHints.KEY_STROKE_CONTROL));
      textAntiAliasingCB.setSelectedItem(hints.get(RenderingHints.KEY_TEXT_ANTIALIASING));
      
      if (hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST) != null){
    textLCDContrastSI.setValue(hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST));
      } else {
    textLCDContrastSI.setValue(140);
      }
      
    }
    isListening = true;
  }
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II FIN INITIALISATION                                       II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//EE EVENEMENT                                                EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  
  /**
   * Process an item event dispatched by a component of the panel.
   * @param e the item event to process.
   */
  protected void processItemEvent(ItemEvent e){
    if (isListening){
      if (e.getSource() == alphaInterpolationCB){
    hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, alphaInterpolationCB.getSelectedItem());
      } else if (e.getSource() == antiAliasingCB){
    hints.put(RenderingHints.KEY_ANTIALIASING, antiAliasingCB.getSelectedItem());
      } else if (e.getSource() == colorRenderingCB){
    hints.put(RenderingHints.KEY_COLOR_RENDERING, colorRenderingCB.getSelectedItem());
      } else if (e.getSource() == ditheringCB){
    hints.put(RenderingHints.KEY_DITHERING, ditheringCB.getSelectedItem());
      } else if (e.getSource() == fractionalCB){
    hints.put(RenderingHints.KEY_FRACTIONALMETRICS, fractionalCB.getSelectedItem());
      } else if (e.getSource() == interpolationCB){
    hints.put(RenderingHints.KEY_INTERPOLATION, interpolationCB.getSelectedItem());
      } else if (e.getSource() == renderingCB){
    hints.put(RenderingHints.KEY_RENDERING, renderingCB.getSelectedItem());
      } else if (e.getSource() == strokeControlCB){
    hints.put(RenderingHints.KEY_STROKE_CONTROL, strokeControlCB.getSelectedItem());
      } else if (e.getSource() == textAntiAliasingCB){
    hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, textAntiAliasingCB.getSelectedItem());
      }
    }
  }

  /**
   * Process a change event dispatched by a component of the panel.
   * @param e the change event to process.
   */
  protected void processChangeEvent(ChangeEvent e){
    if (isListening){
      if (e.getSource() == textLCDContrastSI){
    hints.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, textLCDContrastSI.getValue());
      }
    }
  }
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
//EE FIN EVENEMENT                                            EE
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

  
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                               AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  
  /**
   * Set the rendering hints to edit with this component.
   * @param hints the rendering hints to edit.
   */
  public void setRenderingHints(RenderingHints hints){
    if (hints != null){
      this.hints = hints;
      refreshGUI();
    }
  }
  
  /**
   * Get the rendering hints to edit with this component.
   * @return the rendering hints edited.
   */
  public RenderingHints getRenderingHints(){
    return this.hints;
  }
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                           AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

  
}