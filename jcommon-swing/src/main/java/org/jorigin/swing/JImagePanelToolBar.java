package org.jorigin.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jorigin.Common;

/**
 * A tool bar dedicated to {@link JImagePanel image panel} control.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.2
 */
public class JImagePanelToolBar extends JToolBar implements PropertyChangeListener {

  /**
   * 
   */
  private static final long serialVersionUID = 201708311445L;
  
  private JToggleButton modeSelectionPointButton = null;
  private JToggleButton modeSelectionRectButton  = null;
  private JToggleButton modeSelectionShapeButton = null;
  
  private ButtonGroup modeSelectionGroup         = null;

  private JLabel scaleLB = null;
  private JSpinner scaleSpinner = null;
  
  private JButton fitBT = null;
  
  private JImagePanel imagePanel = null;
  
  private boolean listening = true;
  
  /**
   * Create a new {@link JImagePanel image panel} control tool bar.
   * @param panel the image panel that this tool bar has to control.
   */
  public JImagePanelToolBar(JImagePanel panel){
    super();
    initGUI();
    setImagePanel(panel);
  }
  
  /**
   * Initialization of the Graphical User Interface components.
   */
  protected void initGUI(){
    modeSelectionPointButton = new JToggleButton("Point");
    modeSelectionPointButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (listening){
          if (imagePanel != null){
            imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_POINT);
          }
        }
      }
      
    });
    
    modeSelectionRectButton  = new JToggleButton("Rect");
    modeSelectionRectButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (listening){
          if (imagePanel != null){
            imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_RECT);
          }
        }
      }
      
    });
    
    modeSelectionShapeButton = new JToggleButton("Shape");
    modeSelectionShapeButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (listening){
          if (imagePanel != null){
            imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_SHAPE);
          }
        }
      }
      
    });
    
    modeSelectionGroup       = new ButtonGroup();
    modeSelectionGroup.add(modeSelectionPointButton);
    modeSelectionGroup.add(modeSelectionRectButton);
    modeSelectionGroup.add(modeSelectionShapeButton);
    
    scaleLB = new JLabel("Scale: ");
    
    scaleSpinner = null;
    scaleSpinner = new JSpinner(new SpinnerNumberModel(1.0d, 0.0d, 10.0d, 0.01d));
    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(scaleSpinner, "0%");
    scaleSpinner.setEditor(editor);
    scaleSpinner.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
        if (listening){
          if (imagePanel != null){
            imagePanel.setScale(((Number)scaleSpinner.getValue()).floatValue());
          }
        }
      }
    });
    
    fitBT = new JButton("Fit");
    fitBT.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (listening){
          if (imagePanel != null){
            imagePanel.fit();
          }
        }
      }
      
    });
    
    FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
    setLayout(layout);
    
    add(modeSelectionPointButton);
    add(modeSelectionRectButton);
    add(modeSelectionShapeButton);
    addSeparator();
    add(scaleLB);
    add(scaleSpinner);
    addSeparator();
    add(fitBT);
    
  }
  
  /**
   * Refreshing the Graphical User Interface components.
   */
  protected void refreshGUI(){
    listening = false;
    
    if (imagePanel != null){
      
      modeSelectionPointButton.setEnabled(true);
      modeSelectionRectButton.setEnabled(true);
      modeSelectionShapeButton.setEnabled(true);
      
      if ((imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_POINT){
        modeSelectionPointButton.setSelected(true);
      } else if ((imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_RECT){
        modeSelectionRectButton.setSelected(true);
      } else if ((imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_SHAPE){
        modeSelectionShapeButton.setSelected(true);
      }
      
      scaleLB.setEnabled(true);
      scaleSpinner.setValue(Double.valueOf(imagePanel.getScale()));
      
      fitBT.setEnabled(true);
      
    } else {
      modeSelectionPointButton.setSelected(false);
      modeSelectionPointButton.setEnabled(false);
      modeSelectionRectButton.setSelected(false);
      modeSelectionRectButton.setEnabled(false);
      modeSelectionShapeButton.setSelected(false);
      modeSelectionShapeButton.setEnabled(false);
      
      scaleLB.setEnabled(false);
      scaleSpinner.setValue(1.0f);
      
      fitBT.setEnabled(false);
    }
    

    
    listening = true;
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    
    if (listening){
      refreshGUI();
    }
  }
  
  /**
   * Get the {@link JImagePanel image panel} controlled by this tool bar.
   * @return the {@link JImagePanel image panel} controlled by this tool bar.
   * @see #setImagePanel(JImagePanel)
   */
  public JImagePanel getImagePanel(){
    return imagePanel;
  }
  
  /**
   * Set the {@link JImagePanel image panel} to control with this tool bar.
   * @param panel the {@link JImagePanel image panel} controlled by this tool bar.
   * @see #getImagePanel()
   */
  public void setImagePanel(JImagePanel panel){
    
    if (this.imagePanel != null){
      this.imagePanel.removePropertyChangeListener(this);
    }
    
    this.imagePanel = panel;
    
    if (this.imagePanel != null){
      this.imagePanel.addPropertyChangeListener(this);
    }
    
    refreshGUI();
  }

}

