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
    this.modeSelectionPointButton = new JToggleButton("Point");
    this.modeSelectionPointButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (JImagePanelToolBar.this.listening){
          if (JImagePanelToolBar.this.imagePanel != null){
            JImagePanelToolBar.this.imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_POINT);
          }
        }
      }
      
    });
    
    this.modeSelectionRectButton  = new JToggleButton("Rect");
    this.modeSelectionRectButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (JImagePanelToolBar.this.listening){
          if (JImagePanelToolBar.this.imagePanel != null){
            JImagePanelToolBar.this.imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_RECT);
          }
        }
      }
      
    });
    
    this.modeSelectionShapeButton = new JToggleButton("Shape");
    this.modeSelectionShapeButton.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (JImagePanelToolBar.this.listening){
          if (JImagePanelToolBar.this.imagePanel != null){
            JImagePanelToolBar.this.imagePanel.setSelectionMode(JImagePanel.MODE_SELECTION_SHAPE);
          }
        }
      }
      
    });
    
    this.modeSelectionGroup       = new ButtonGroup();
    this.modeSelectionGroup.add(this.modeSelectionPointButton);
    this.modeSelectionGroup.add(this.modeSelectionRectButton);
    this.modeSelectionGroup.add(this.modeSelectionShapeButton);
    
    this.scaleLB = new JLabel("Scale: ");
    
    this.scaleSpinner = null;
    this.scaleSpinner = new JSpinner(new SpinnerNumberModel(1.0d, 0.0d, 10.0d, 0.01d));
    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this.scaleSpinner, "0%");
    this.scaleSpinner.setEditor(editor);
    this.scaleSpinner.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
        if (JImagePanelToolBar.this.listening){
          if (JImagePanelToolBar.this.imagePanel != null){
            JImagePanelToolBar.this.imagePanel.setScale(((Number)JImagePanelToolBar.this.scaleSpinner.getValue()).floatValue());
          }
        }
      }
    });
    
    this.fitBT = new JButton("Fit");
    this.fitBT.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        if (JImagePanelToolBar.this.listening){
          if (JImagePanelToolBar.this.imagePanel != null){
            JImagePanelToolBar.this.imagePanel.fit();
          }
        }
      }
      
    });
    
    FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
    setLayout(layout);
    
    add(this.modeSelectionPointButton);
    add(this.modeSelectionRectButton);
    add(this.modeSelectionShapeButton);
    addSeparator();
    add(this.scaleLB);
    add(this.scaleSpinner);
    addSeparator();
    add(this.fitBT);
    
  }
  
  /**
   * Refreshing the Graphical User Interface components.
   */
  protected void refreshGUI(){
    this.listening = false;
    
    if (this.imagePanel != null){
      
      this.modeSelectionPointButton.setEnabled(true);
      this.modeSelectionRectButton.setEnabled(true);
      this.modeSelectionShapeButton.setEnabled(true);
      
      if ((this.imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_POINT){
        this.modeSelectionPointButton.setSelected(true);
      } else if ((this.imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_RECT){
        this.modeSelectionRectButton.setSelected(true);
      } else if ((this.imagePanel.getSelectionMode() & JImagePanel.MODE_SELECTION_POINT) == JImagePanel.MODE_SELECTION_SHAPE){
        this.modeSelectionShapeButton.setSelected(true);
      }
      
      this.scaleLB.setEnabled(true);
      this.scaleSpinner.setValue(Double.valueOf(this.imagePanel.getScale()));
      
      this.fitBT.setEnabled(true);
      
    } else {
      this.modeSelectionPointButton.setSelected(false);
      this.modeSelectionPointButton.setEnabled(false);
      this.modeSelectionRectButton.setSelected(false);
      this.modeSelectionRectButton.setEnabled(false);
      this.modeSelectionShapeButton.setSelected(false);
      this.modeSelectionShapeButton.setEnabled(false);
      
      this.scaleLB.setEnabled(false);
      this.scaleSpinner.setValue(1.0f);
      
      this.fitBT.setEnabled(false);
    }
    

    
    this.listening = true;
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    
    if (this.listening){
      refreshGUI();
    }
  }
  
  /**
   * Get the {@link JImagePanel image panel} controlled by this tool bar.
   * @return the {@link JImagePanel image panel} controlled by this tool bar.
   * @see #setImagePanel(JImagePanel)
   */
  public JImagePanel getImagePanel(){
    return this.imagePanel;
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

