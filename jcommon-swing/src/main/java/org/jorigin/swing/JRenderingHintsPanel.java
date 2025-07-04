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

	/**
	 * The serial version UID.
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID         = Common.BUILD;

	/**
	 * The resource bundle used to localize the panel.
	 * @see org.jorigin.lang.LangResourceBundle
	 */
	private LangResourceBundle lr              = (LangResourceBundle) LangResourceBundle.getBundle(Locale.getDefault()); 

	/**
	 * Is the border has to be shown. <code>true</code> if the panel has a titled border, <code>false</code> otherwise.
	 * @see javax.swing.BorderFactory#createTitledBorder(String)
	 */
	private boolean showBorder                 = false;

	/**
	 * The rendering hints to edit
	 */
	private RenderingHints hints = null;

	/**
	 * The values for the rendering hints.
	 * @see java.awt.RenderingHints
	 */
	private Object[] alphaInterpolationValues = new Object[]{RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY};

	/**
	 * The values for the anti-aliasing rendering hint.
	 * @see java.awt.RenderingHints#KEY_ANTIALIASING
	 */
	private Object[] antiAliasingValues       = new Object[]{RenderingHints.VALUE_ANTIALIAS_DEFAULT,
			RenderingHints.VALUE_ANTIALIAS_OFF,
			RenderingHints.VALUE_ANTIALIAS_ON};

	/**
	 * The values for the color rendering hint.
	 * @see java.awt.RenderingHints#KEY_COLOR_RENDERING
	 */
	private Object[] colorRenderingValues     = new Object[]{RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
			RenderingHints.VALUE_COLOR_RENDER_SPEED,
			RenderingHints.VALUE_COLOR_RENDER_QUALITY};

	/**
	 * The values for the dithering rendering hint.
	 * @see java.awt.RenderingHints#KEY_DITHERING
	 */
	private Object[] ditheringValues          = new Object[]{RenderingHints.VALUE_DITHER_DEFAULT,
			RenderingHints.VALUE_DITHER_DISABLE,
			RenderingHints.VALUE_DITHER_ENABLE};

	/**
	 * The values for the fractional metrics rendering hint.
	 * @see java.awt.RenderingHints#KEY_FRACTIONALMETRICS
	 */
	private Object[] fractionalMetricsValues  = new Object[]{RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
			RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
			RenderingHints.VALUE_FRACTIONALMETRICS_ON};  

	/**
	 * The values for the interpolation rendering hint.
	 * @see java.awt.RenderingHints#KEY_INTERPOLATION
	 */
	private Object[] interpolationValues      = new Object[]{RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC};

	/**
	 * The values for the rendering rendering hint.
	 * @see java.awt.RenderingHints#KEY_RENDERING
	 */
	private Object[] renderingValues          = new Object[]{RenderingHints.VALUE_RENDER_DEFAULT,
			RenderingHints.VALUE_RENDER_SPEED,
			RenderingHints.VALUE_RENDER_QUALITY};

	/**
	 * The values for the stroke control rendering hint.
	 * @see java.awt.RenderingHints#KEY_STROKE_CONTROL
	 */
	private Object[] strokeControlValues      = new Object[]{RenderingHints.VALUE_STROKE_DEFAULT,
			RenderingHints.VALUE_STROKE_PURE,
			RenderingHints.VALUE_STROKE_NORMALIZE};

	/**
	 * The values for the text anti-aliasing rendering hint.
	 * @see java.awt.RenderingHints#KEY_TEXT_ANTIALIASING
	 */
	private Object[] textAntiAliasingValues   = new Object[]{RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
			RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
			RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
			RenderingHints.VALUE_TEXT_ANTIALIAS_GASP,
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR,
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB,
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR};

	/**
	 * The values for the text LCD contrast rendering hint.
	 * @see java.awt.RenderingHints#KEY_TEXT_LCD_CONTRAST
	 */
	private JLabel alphaInterpolationLB       = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel antiAliasingLB             = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel colorRenderingLB           = null;
	
	/**	
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel ditheringLB                = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel fractionalLB               = null; 
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel interpolationLB            = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel renderingLB                = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel strokeControlLB            = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel textAntiAliasingLB         = null;
	
	/**
	 * The labels and combo boxes for the rendering hints.
	 * @see javax.swing.JLabel
	 * @see javax.swing.JComboBox
	 */
	private JLabel textLCDContrastLB          = null;

	/**
	 * The combo box for the alpha interpolation rendering hint.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> alphaInterpolationCB = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> antiAliasingCB       = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> colorRenderingCB     = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> ditheringCB          = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> fractionalCB         = null; 
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> interpolationCB      = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> renderingCB          = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> strokeControlCB      = null;
	
	/**
	 * The combo boxes for the rendering hints.
	 * @see javax.swing.JComboBox
	 */
	private JComboBox<Object> textAntiAliasingCB   = null;
	
	/**
	 * The spinner for the text LCD contrast rendering hint.
	 * @see javax.swing.JSpinner
	 */
	private JSpinner  textLCDContrastSI            = null;

	/**
	 * Is the panel listening to the combo boxes and spinner events.
	 * If <code>false</code>, the panel does not listen to the combo boxes and spinner events.
	 * This is useful when the panel is used in a dialog and you want to prevent the
	 * panel from listening to the events while the dialog is being closed.
	 */
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

		 this.showBorder = showTitle;

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


		 this.isListening = false;

		 ItemListener itemListener = new ItemListener(){

			 @Override
			 public void itemStateChanged(ItemEvent e) {
				 processItemEvent(e);
			 }

		 };

		 this.alphaInterpolationLB       = new JLabel(this.lr.getString("GUI_RH_ALPHA_INTERPOLATION_LB")+": ");
		 this.alphaInterpolationLB.setToolTipText(this.lr.getString("GUI_RH_ALPHA_INTERPOLATION_TIP"));

		 this.antiAliasingLB             = new JLabel(this.lr.getString("GUI_RH_ANTIALIASING_LB")+": ");
		 this.antiAliasingLB.setToolTipText(this.lr.getString("GUI_RH_ANTIALIASING_TIP"));

		 this.colorRenderingLB           = new JLabel(this.lr.getString("GUI_RH_COLOR_RENDERING_LB")+": ");
		 this.colorRenderingLB.setToolTipText(this.lr.getString("GUI_RH_COLOR_RENDERING_TIP"));

		 this.ditheringLB                = new JLabel(this.lr.getString("GUI_RH_DITHERING_LB")+": ");
		 this.ditheringLB.setToolTipText(this.lr.getString("GUI_RH_DITHERING_TIP"));

		 this.fractionalLB               = new JLabel(this.lr.getString("GUI_RH_FRACTIONALMETRICS_LB")+": ");
		 this.fractionalLB.setToolTipText(this.lr.getString("GUI_RH_FRACTIONALMETRICS_TIP"));

		 this.interpolationLB            = new JLabel(this.lr.getString("GUI_RH_INTERPOLATION_LB")+": ");
		 this.interpolationLB.setToolTipText(this.lr.getString("GUI_RH_INTERPOLATION_TIP"));

		 this.renderingLB                = new JLabel(this.lr.getString("GUI_RH_RENDERING_LB")+": ");
		 this.renderingLB.setToolTipText(this.lr.getString("GUI_RH_RENDERING_TIP"));

		 this.strokeControlLB            = new JLabel(this.lr.getString("GUI_RH_STROKE_CONTROL_LB")+": ");
		 this.strokeControlLB.setToolTipText(this.lr.getString("GUI_RH_STROKE_CONTROL_TIP"));

		 this.textAntiAliasingLB         = new JLabel(this.lr.getString("GUI_RH_TEXT_ANTIALIASING_LB")+": ");
		 this.textAntiAliasingLB.setToolTipText(this.lr.getString("GUI_RH_TEXT_ANTIALIASING_TIP"));

		 this.textLCDContrastLB          = new JLabel(this.lr.getString("GUI_RH_TEXT_LCD_CONTRAST_LB")+": ");
		 this.textLCDContrastLB.setToolTipText(this.lr.getString("GUI_RH_TEXT_LCD_CONTRAST_TIP"));

		 this.alphaInterpolationCB = new JComboBox<Object>(this.alphaInterpolationValues);
		 this.alphaInterpolationCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_SPEED_LB"));
					 } else if (value.equals(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_QUALITY_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.alphaInterpolationCB.addItemListener(itemListener);

		 this.antiAliasingCB = new JComboBox<Object>(this.antiAliasingValues);
		 this.antiAliasingCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_ANTIALIAS_OFF)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_OFF_LB"));
					 } else if (value.equals(RenderingHints.VALUE_ANTIALIAS_ON)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_ON_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.antiAliasingCB.addItemListener(itemListener);

		 this.colorRenderingCB   = new JComboBox<Object>(this.colorRenderingValues);
		 this.colorRenderingCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_COLOR_RENDER_SPEED)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_OFF_LB"));
					 } else if (value.equals(RenderingHints.VALUE_COLOR_RENDER_QUALITY)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_ON_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.colorRenderingCB.addItemListener(itemListener);

		 this.ditheringCB        = new JComboBox<Object>(this.ditheringValues);
		 this.ditheringCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_DITHER_DISABLE)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_OFF_LB"));
					 } else if (value.equals(RenderingHints.VALUE_DITHER_ENABLE)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_ON_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.ditheringCB.addItemListener(itemListener);

		 this.fractionalCB       = new JComboBox<Object>(this.fractionalMetricsValues);
		 this.fractionalCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_FRACTIONALMETRICS_OFF)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_OFF_LB"));
					 } else if (value.equals(RenderingHints.VALUE_FRACTIONALMETRICS_ON)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_ON_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.fractionalCB.addItemListener(itemListener);

		 this.interpolationCB    = new JComboBox<Object>(this.interpolationValues);
		 this.interpolationCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_NEAREST_NEIGHBOUR_LB"));
					 } else if (value.equals(RenderingHints.VALUE_INTERPOLATION_BILINEAR)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_BILINEAR_LB"));
					 } else if (value.equals(RenderingHints.VALUE_INTERPOLATION_BICUBIC)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_BICUBIC_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.interpolationCB.addItemListener(itemListener);

		 this.renderingCB        = new JComboBox<Object>(this.renderingValues);
		 this.renderingCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_RENDER_SPEED)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_SPEED_LB"));
					 } else if (value.equals(RenderingHints.VALUE_RENDER_QUALITY)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_QUALITY_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.renderingCB.addItemListener(itemListener);

		 this.strokeControlCB    = new JComboBox<Object>(this.strokeControlValues);
		 this.strokeControlCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_STROKE_PURE)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_PURE_LB"));
					 } else if (value.equals(RenderingHints.VALUE_STROKE_NORMALIZE)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_NORMALIZE_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.strokeControlCB.addItemListener(itemListener);

		 this.textAntiAliasingCB = new JComboBox<Object>(this.textAntiAliasingValues);
		 this.textAntiAliasingCB.setRenderer(new DefaultListCellRenderer(){
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
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_DEFAULT_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_GASP_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_HBGR_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_HRGB_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_VBGR_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_TEXT_ANTIALIAS_LCD_VRGB_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_OFF_LB"));
					 } else if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON)){
						 label.setText(JRenderingHintsPanel.this.lr.getString("GUI_RH_VALUE_ON_LB"));
					 }
				 }

				 return label;

			 }
		 });
		 this.textAntiAliasingCB.addItemListener(itemListener);
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
		 this.textLCDContrastSI  = new JSpinner(sopacmodel);
		 this.textLCDContrastSI.addChangeListener(new ChangeListener(){

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
		 add(this.renderingLB, c);

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
		 add(this.renderingCB, c);

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
		 add(this.colorRenderingLB, c);

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
		 add(this.colorRenderingCB, c);

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
		 add(this.interpolationLB, c);

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
		 add(this.interpolationCB, c);

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
		 add(this.alphaInterpolationLB, c);

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
		 add(this.alphaInterpolationCB, c);

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
		 add(this.antiAliasingLB, c);

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
		 add(this.antiAliasingCB, c);

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
		 add(this.ditheringLB, c);

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
		 add(this.ditheringCB, c);

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
		 add(this.fractionalLB, c);

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
		 add(this.fractionalCB, c);

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
		 add(this.strokeControlLB, c);

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
		 add(this.strokeControlCB, c);

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
		 add(this.textAntiAliasingLB, c);

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
		 add(this.textAntiAliasingCB, c);

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
		 add(this.textLCDContrastLB, c);

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
		 add(this.textLCDContrastSI, c);


		 if (this.showBorder){
			 setBorder(BorderFactory.createTitledBorder(this.lr.getString("GUI_RH_ID")));
		 }

		 this.isListening = true;

	 }

	 /**
	  * Refresh the GUI
	  */
	 public void refreshGUI(){
		 this.isListening = false;

		 if (this.hints != null){
			 this.renderingCB.setSelectedItem(this.hints.get(RenderingHints.KEY_RENDERING));
			 this.colorRenderingCB.setSelectedItem(this.hints.get(RenderingHints.KEY_COLOR_RENDERING));
			 this.interpolationCB.setSelectedItem(this.hints.get(RenderingHints.KEY_INTERPOLATION));
			 this.alphaInterpolationCB.setSelectedItem(this.hints.get(RenderingHints.KEY_ALPHA_INTERPOLATION));
			 this.antiAliasingCB.setSelectedItem(this.hints.get(RenderingHints.KEY_ANTIALIASING));
			 this.ditheringCB.setSelectedItem(this.hints.get(RenderingHints.KEY_DITHERING));
			 this.fractionalCB.setSelectedItem(this.hints.get(RenderingHints.KEY_FRACTIONALMETRICS));
			 this.strokeControlCB.setSelectedItem(this.hints.get(RenderingHints.KEY_STROKE_CONTROL));
			 this.textAntiAliasingCB.setSelectedItem(this.hints.get(RenderingHints.KEY_TEXT_ANTIALIASING));

			 if (this.hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST) != null){
				 this.textLCDContrastSI.setValue(this.hints.get(RenderingHints.KEY_TEXT_LCD_CONTRAST));
			 } else {
				 this.textLCDContrastSI.setValue(140);
			 }

		 }
		 this.isListening = true;
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
		 if (this.isListening){
			 if (e.getSource() == this.alphaInterpolationCB){
				 this.hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, this.alphaInterpolationCB.getSelectedItem());
			 } else if (e.getSource() == this.antiAliasingCB){
				 this.hints.put(RenderingHints.KEY_ANTIALIASING, this.antiAliasingCB.getSelectedItem());
			 } else if (e.getSource() == this.colorRenderingCB){
				 this.hints.put(RenderingHints.KEY_COLOR_RENDERING, this.colorRenderingCB.getSelectedItem());
			 } else if (e.getSource() == this.ditheringCB){
				 this.hints.put(RenderingHints.KEY_DITHERING, this.ditheringCB.getSelectedItem());
			 } else if (e.getSource() == this.fractionalCB){
				 this.hints.put(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalCB.getSelectedItem());
			 } else if (e.getSource() == this.interpolationCB){
				 this.hints.put(RenderingHints.KEY_INTERPOLATION, this.interpolationCB.getSelectedItem());
			 } else if (e.getSource() == this.renderingCB){
				 this.hints.put(RenderingHints.KEY_RENDERING, this.renderingCB.getSelectedItem());
			 } else if (e.getSource() == this.strokeControlCB){
				 this.hints.put(RenderingHints.KEY_STROKE_CONTROL, this.strokeControlCB.getSelectedItem());
			 } else if (e.getSource() == this.textAntiAliasingCB){
				 this.hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, this.textAntiAliasingCB.getSelectedItem());
			 }
		 }
	 }

	 /**
	  * Process a change event dispatched by a component of the panel.
	  * @param e the change event to process.
	  */
	 protected void processChangeEvent(ChangeEvent e){
		 if (this.isListening){
			 if (e.getSource() == this.textLCDContrastSI){
				 this.hints.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, this.textLCDContrastSI.getValue());
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