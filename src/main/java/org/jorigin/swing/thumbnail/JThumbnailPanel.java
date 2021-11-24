package org.jorigin.swing.thumbnail;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;

import org.jorigin.task.TaskEvent;
import org.jorigin.task.TaskListener;

/**
 * A panel dedicated to {@link JThumbnail thumbnail} display. 
 * This object works like a {@link JList} but enable more flexibility on the thumbnail interaction.
 * @author Julien Seinturier - COMEX SA
 * @param <T> the type enclosed by the thumbnails of the list.
 * @version 1.0.8
 * @since 1.0.8
 */
public class JThumbnailPanel<T> extends JPanel {

  private static final long serialVersionUID = 1L;

  protected int labelWidth = 0;
  protected int labelHeight = 0;

  protected double thumbRatio = 0;

  protected int labelMargin = 5;

  protected int labelCount = 0;

  /** The label added to the list */
  protected ArrayList<JThumbnail<T>> labels = null;
  
  /** The selected labels */
  protected List<JThumbnail<T>> selectedLabels;

  /** The index corresponding to the last selected index */
  protected int lastSelectedIndex = -1;
  
  /** The first selected label */
  protected JThumbnail<T> activatedLabel;

  protected volatile boolean isLabelLoading = false;

  /**
   * Liste des ecouteurs informes des evenements du thumbnail
   */
  protected EventListenerList idListenerList = new EventListenerList();

  /**
   * Selection mode of the thumbnail<br>
   * Selection mode can be:<br>
   * <b>Exclusive</b> by using the setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);<br>
   * <b>Contiguous</b> by using the selectection(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION );<br>
   * <b>Multiple Contiguous</b> by using the selectection(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
   */
  private int selectionMode = ListSelectionModel.SINGLE_SELECTION;

  /**
   * Color used for the coloring the background of the label where it's in standard state
   */
  protected Color normalColor = Color.LIGHT_GRAY;

  /**
   * Color used for the coloring the background of the label where it's in focus state
   */
  protected Color focusColor = Color.GREEN;

  /**
   * Color used for the coloring the background of the label where it's in selected state
   */
  protected Color selectedColor = Color.RED;

  protected Color itemPResentColor = Color.YELLOW;

  // Variables d'etat d'une tache
  protected String state = "";

  /**
   * The number of labels to show per line.
   */
  private int labelPerLine = 1;
  
  /**
   * Create a new empty thumbnail panel.
   */
  public JThumbnailPanel() {
    this(1);
  }

  /**
   * Create a new thumbnail panel that display the given number of thumbnail per line.
   * @param labelPerLine the number of thumbnail to display per line.
   */
  public JThumbnailPanel(int labelPerLine){

    super();

    this.selectedLabels = new ArrayList<JThumbnail<T>>();
    this.labels = new ArrayList<JThumbnail<T>>();
    
    this.labelPerLine = labelPerLine;
    
    this.setBackground(Color.WHITE);
    this.setBorder(null);
    
    setLayout(new GridBagLayout());
  }

  /**
   * Returns the bounds of the specified range of items in JList  coordinates. 
   * Returns <code>null</code> if index isn't valid.
   * @param index0 the index of the first JList cell in the range
   * @param index1 the index of the last JList cell in the range
   * @return the bounds of the indexed cells in pixels
   */
  public Rectangle getCellBounds(int index0, int index1){
    
    Rectangle rectangle = null;
    JThumbnail<T> l0 = null;
    JThumbnail<T> l1 = null;
    
    // L'index 0 doit être inférieur à l'index 1
    // L'index 0 doit être positif ou nul
    // L'index 1 ne doit pas être plus grand que la taille de la liste de labels
    if ((index0 > index1)||(index0 < 0)||(index1 > labels.size())){
      return null;
    }
    
    // Recuperation des labels aux index demandés
    l0 = labels.get(index0);
    l1 = labels.get(index1);
    
    if ((l0 == null)||(l1 == null)){
      return null;
    }
    
    // Calcul du rectangle englobant les deux labels
    rectangle = new Rectangle((int)l0.getBounds().getX(), 
                               (int)l0.getBounds().getY(),
                               (int)(l1.getBounds().getX()+l1.getBounds().getWidth()),
                               (int)(l1.getBounds().getY()+l1.getBounds().getHeight()));
    
    return rectangle;
  }
  
  /**
   * Changes the selection to be the set of indices specified by the given array. 
   * Indices greater than or equal to the size are ignored. 
   * @param indices an array of the indices of the cells to select, non-null
   */
  public void setSelectedIndices(int[] indices){
    this.unselectAll();  
    
    if ((indices != null)&&(labels != null)){
      for(int i = 0; i < indices.length; i++){
        if ((indices[i] >= 0)&&(indices[i] < labels.size())){
          select(labels.get(indices[i]));
        }
      }
    }
  }
  
  /**
   * Returns an array of all of the selected indices, in increasing order.
   * @return all of the selected indices, in increasing order, or an empty array if nothing is selected
   * @see #setSelectedIndices(int[])
   */
  public int[] getSelectedIndices(){

    int[] indices = null;
    Iterator<JThumbnail<T>> iter = null;
    int index = 0;
    int i = 0;
    
    if(selectedLabels.size() > 0){
      indices = new int[selectedLabels.size()];
      
      iter = selectedLabels.iterator();
      while(iter.hasNext()){
        index = labels.indexOf(iter.next());
        indices[i] = index;
        i++;
      }
      
      // Tri du tableau dans l'ordre croissant
      Arrays.sort(indices);
    } else {
      indices = new int[0];
    }
    
    return indices;
  }
  
  /**
   * Returns the smallest selected cell index; 
   * the selection when only a single item is selected in the list. 
   * When multiple items are selected, it is simply the smallest selected index. 
   * Returns -1 if there is no selection.
   * @return the smallest selected cell index
   * @see #setSelectedIndex(int)
   */
  public int getSelectedIndex(){
    
    if (selectedLabels.size() > 0){
      return labels.indexOf(selectedLabels.get(0));
    }
      
    return -1;
  }

  /**
   * Selects a single cell. 
   * Does nothing if the given index is greater than or equal to the model size.
   * @param index the index of the cell to select
   * @see #getSelectedIndex()
   */
  public void setSelectedIndex(int index){
  
    if((index > 0)&&(index < labels.size())){
      unselectAll();
      select(labels.get(index));
    }
    
  }
   
  /**
   * Selects the specified object from the list.
   * @param anObject the object to select
   * @param shouldScroll true if the list should scroll to display the selected object, if one exists; otherwise false
   */
  public void setSelectedValue(Object anObject, boolean shouldScroll){
    
    JThumbnail<T> label = null;
    Iterator<JThumbnail<T>> iter = null;
    boolean found = false;
    
    iter = labels.iterator();
    while(iter.hasNext() && (!found)){
      label = iter.next();
      if (label == anObject){
        found = true;
      }
    }
    
    if (label != null){
      
      unselectAll();
      
      select(label);
      
      if (shouldScroll){
        scrollToSelected();
      } 
    }
  }
  
  
  /**
   * Selects the specified label in the list.
   * @param label the label to select
   * @param shouldScroll true if the list should scroll to display the selected label, if one exists; otherwise false
   */
  public void setSelectedThumbnail(JThumbnail<T> label, boolean shouldScroll){
    
    if (labels.contains(label)){
      
      unselectAll();
      
      select(label);
      
      if (shouldScroll){
        scrollToSelected();
      } 
    }
  }
  
  
  /**
   * Returns the first selected label, or null if the selection is empty.
   * @return the first selected label
   */
  public JThumbnail<T> getSelectedThumbnail(){
    
    
    if (selectedLabels.size() > 0){
      return selectedLabels.get(0);
    }
    
    return null;
  }
  
  /**
   * Returns an array of the labels for the selected cells. The returned labels are sorted in increasing index order.
   * @return the selected labels or an empty list if nothing is selected.
   */
  public List<JThumbnail<T>> getSelectedThumbnails(){
    
    List<JThumbnail<T>> thumbnails = new ArrayList<JThumbnail<T>>(selectedLabels);
    
    Collections.sort(thumbnails, new Comparator<JThumbnail<T>>(){
      @Override
      public int compare(JThumbnail<T> o1, JThumbnail<T> o2) {
        int i1 = labels.indexOf(o1);
        int i2 =  labels.indexOf(o2);
        
        if (i1 < i2){
          return 1;
        } else if (i2 > i1){
          return -1;
        }
        return 0;
      }

      });
    return thumbnails;  
  }
  
  /**
   * Returns an array of the values for the selected cells. The returned values are sorted in increasing index order.
   * @return the selected values or an empty list if nothing is selected.
   */
  public Object[] getSelectedValues(){
    
    @SuppressWarnings("unchecked")
    JThumbnail<T>[] labelsArray = selectedLabels.toArray(new JThumbnail[selectedLabels.size()]);
    Object[] values = null;
    
    Arrays.sort(labelsArray, new Comparator<JThumbnail<T>>(){
      @Override
      public int compare(JThumbnail<T> o1, JThumbnail<T> o2) {
        int i1 = labels.indexOf(o1);
        int i2 =  labels.indexOf(o2);
        
        if (i1 < i2){
          return 1;
        } else if (i2 > i1){
          return -1;
        }
        return 0;
      }

      });
    
    
    values = new Object[labelsArray.length];
    
    for(int i = 0; i < labelsArray.length; i++){
      values[i] = labelsArray[i].getContent();
    }
    
    return values;  
  }
  
  
  /**
   * Determines whether single-item or multiple-item
   * selections are allowed.
   * The following <code>selectionMode</code> values are allowed:
   * <ul>
   * <li> <code>ListSelectionModel.SINGLE_SELECTION</code>
   * Only one list index can be selected at a time.  In this
   * mode the <code>setSelectionInterval</code> and 
   * <code>addSelectionInterval</code>
   *
   * methods are equivalent, and only the second index
   * argument is used.
   * </li><li> <code>ListSelectionModel.SINGLE_INTERVAL_SELECTION</code>
   * One contiguous index interval can be selected at a time.
   * In this mode <code>setSelectionInterval</code> and 
   * <code>addSelectionInterval</code>
   * are equivalent.
   * </li><li> <code>ListSelectionModel.MULTIPLE_INTERVAL_SELECTION</code>
   * 
   * In this mode, there's no restriction on what can be selected.
   * This is the default.
   * </li></ul>
   * @param selectionMode an integer specifying the type of selections that are permissible
   * @see #getSelectionMode()
   */
  public void setSelectionMode(int selectionMode){
    if ((selectionMode == ListSelectionModel.SINGLE_SELECTION)
       || (selectionMode == ListSelectionModel.SINGLE_INTERVAL_SELECTION)
       || (selectionMode == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)){
      
      this.selectionMode = selectionMode;  
    }
  }
  
  
  /**
   * Returns whether single-item or multiple-item selections are allowed.
   * @return the value of the selectionMode property
   */
  public int getSelectionMode(){
    return this.selectionMode;
  }
  
//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
//FF FIN FONCTIONS DE JLIST                                                   FF
//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

//LISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLI
//LI  FONCTIONS DE java.util.List                                             ST
//LISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLISTLI

  /**
   * Returns the index in this list of the first occurrence of the specified element, 
   * or -1 if this list does not contain this element. More formally, returns the 
   * lowest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 
   * if there is no such index.
   * @param o element to search for.
   * @return the index in this list of the first occurrence of the specified element, 
   * or -1 if this list does not contain this element.
   */  
  public int indexOf(Object o){
    int index = -1;
    int i = 0;
  
    Iterator<JThumbnail<T>> iter = this.labels.iterator();
    Object content = null;
    while((iter.hasNext()) && (index == -1)){
      content = iter.next().getContent();
      if (content.equals(o)){
        index = i;
      } else {
        i++;    
      }
    }
  
    return index; 
  }


  /**
   * Returns the index in this list of the first occurrence of the specified active label, 
   * or -1 if this list does not contain this active label. More formally, returns the 
   * lowest index i such that (o==null ? get(i)==null : o.equals(get(i))), or -1 
   * if there is no such index.
   * @param label the active label to search for.
   * @return the index in this list of the first occurrence of the specified element, 
   * or -1 if this list does not contain this element.
   */  
  public int indexOf(JThumbnail<T> label){
    int index = -1;
    int i = 0;
  
    Iterator<JThumbnail<T>> iter = this.labels.iterator();
    JThumbnail<T> content = null;
    while((iter.hasNext()) && (index == -1)){
      content = iter.next();
      if (content.equals(label)){
        index = i;
      } else {
        i++;    
      }
    }
  
    return index;
  }
  
  
  /**
   * Returns true if this thumbnail contains the specified element. More formally, 
   * returns true if and only if this thumbnail contains at least one element e such 
   * that (o==null ? e==null : o.equals(e)).
   * @param o element whose presence in this list is to be tested.
   * @return true if this list contains the specified element.
   */
  public boolean contains(Object o){
    boolean contain = false;
    Iterator<JThumbnail<T>> iter = this.labels.iterator();
    
    while((iter.hasNext()) && (!contain)){
      contain = iter.next().getContent().equals(o);
    }
    
    return contain;
  }

  
  /**
   * Returns true if this thumbnail contains the specified active label. More formally, 
   * returns true if and only if this active thumbnail contains at least one 
   * element e such that (o==null ? e==null : o.equals(e)).
   * @param label element whose presence in this thumbnail is to be tested.
   * @return true if this thumbnail contains the specified label.
   */
  public boolean contains(JThumbnail<T> label){
    boolean contain = false;
    Iterator<JThumbnail<T>> iter = this.labels.iterator();
    
    while((iter.hasNext()) && (!contain)){
      contain = iter.next().equals(label);
    }
    
    return contain;
  }

  /**
   * Get the activated thumbnail.
   * @return the activated thumbnail.
   */
  public JThumbnail<T> getActivatedLabel() {
    return this.activatedLabel;
  }

  /**
   * Set the thumb ratio.
   * @param thumbRatio the thumb ratio.
   */
  public void setThumbRatio(double thumbRatio) {
    this.thumbRatio = thumbRatio;
  }

  /**
   * Get the thumb ratio.
   * @return the thumb ratio.
   */
  public double getThumbRatio() {
	    return thumbRatio;
	  }
  
  /**
   * Set the default width of the thumbnails displayed within this panel.
   * @param width the default width of the thumbnails displayed within this panel.
   * @see #getLabelWidth()
   */
  public void setLabelWidth(int width) {
    labelWidth = width;
  }

  /**
   * Get the default width of the thumbnails displayed within this panel.
   * @return the default width of the thumbnails displayed within this panel.
   * @see #setLabelWidth(int)
   */
  public int getLabelWidth() {
    return this.labelWidth;
  }

  /**
   * Set the default height of the thumbnails displayed within this panel.
   * @param height the default height of the thumbnails displayed within this panel.
   * @see #getLabelHeight()
   */
  public void setLabelHeight(int height) {
    labelHeight = height;
  }

  /**
   * Get the default height of the thumbnails displayed within this panel.
   * @return the default height of the thumbnails displayed within this panel.
   * @see #setLabelHeight(int)
   */
  public int getLabelHeight() {
    return this.labelHeight;
  }

  /**
   * Set the default margin size of the thumbnails displayed within this panel.
   * @param margin the default margin size of the thumbnails displayed within this panel.
   * @see #getLabelMargin()
   */
  public void setLabelMargin(int margin) {
    labelMargin = margin;
  }

  /**
   * Get the default margin size of the thumbnails displayed within this panel.
   * @return the default margin size of the thumbnails displayed within this panel.
   * @see #setLabelMargin(int)
   */
  public int getLabelMargin() {
    return this.labelMargin;
  }

  /**
   * Set the number of thumbnails that are attached to this panel.
   * @param count the number of thumbnails that are attached to this panel.
   * @see #getLabelCount()
   */
  public void setLabelCount(int count) {
    labelCount = count;
  }

  /**
   * Get the number of thumbnails that are attached to this panel.
   * @return the number of thumbnails that are attached to this panel.
   * @see #setLabelCount(int)
   */
  public int getLabelCount() {
    return this.labelCount;
  }

  /**
   * Get the state of this panel.
   * @return the state of this panel.
   */
  public String getState() {
    return this.state;
  }

  /**
   * Set if this panel is currently loading.
   * @param b <code>true</code> if the panel is loading and <code>false</code> otherwise.
   * @see #isLoading()
   */
  public void setLoading(boolean b) {
    this.isLabelLoading = b;
  }

  /**
   * Get if this panel is currently loading.
   * @return <code>true</code> if the panel is loading and <code>false</code> otherwise.
   * @see #setLoading(boolean)
   */
  public boolean isLoading() {
    return this.isLabelLoading;
  }

  /**
   * Scroll the view to selected thumbnails.
   */
  public void scrollToSelected(){
    scrollToSelectedIndices();  
  }

  /**
   * Refresh the thumbnail by refreshing all the activelabels conained
   */
  @SuppressWarnings("unchecked")
  public void refresh() {
    for (int i = 0; i < this.getComponents().length; i++) {
      if (this.getComponent(i) instanceof JThumbnail) {
        this.getComponent(i).validate();
        this.getComponent(i).setVisible(true);
        
        if (getComponent(i) instanceof JThumbnail){
          ( (JThumbnail<T>)getComponent(i)).refresh();
        }
      }
    }
    
    this.repaint();
  }

  
  /**
   * Add an active label to the thumbnail.
   * @param label the label to add
   * @return true if the label is added, false otherwise
   */
  public boolean add(JThumbnail<T> label){
       
    GridBagConstraints c = null;
    
    Insets insets        = new Insets(2,2,2,2);
    
    // Ajout du label à la liste des labels
    if (labels.add(label)){
      
      if (getLayout() instanceof GridBagLayout){
        // Ajout du label au composant graphique (JPanel)    
        if ((labels.size() % labelPerLine) == 0){
          c           = new GridBagConstraints ();
          c.gridx     = GridBagConstraints.RELATIVE;
          c.gridy     = GridBagConstraints.RELATIVE;
          c.gridheight= 1;
          c.gridwidth = GridBagConstraints.REMAINDER;
          c.fill      = GridBagConstraints.NONE;
          c.insets    = insets;
          c.weightx   = 0.0;
          c.weighty   = 0.0;
          c.anchor    = GridBagConstraints.WEST;
          add(label, c);
        } else {
          c           = new GridBagConstraints ();
          c.gridx     = GridBagConstraints.RELATIVE;
          c.gridy     = GridBagConstraints.RELATIVE;
          c.gridheight= 1;
          c.gridwidth = 1;
          c.fill      = GridBagConstraints.NONE;
          c.insets    = insets;
          c.weightx   = 0.0;
          c.weighty   = 0;
          c.anchor    = GridBagConstraints.WEST;
          add(label, c);
        }
      } else {
        super.add(label);
      }

      label.addMouseListener(new MouseListener(){

        @Override
        public void mouseClicked(MouseEvent e) {
          processActiveLabelMouseEvent(e); 
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          processActiveLabelMouseEvent(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          processActiveLabelMouseEvent(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
          processActiveLabelMouseEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
          processActiveLabelMouseEvent(e);
        }});
    } else {
      return false;
    }
    
    
    
    //  Validation du placement par recalcul du layout
    this.validate();
    // Activation visuelle du nouveau label
    label.setVisible(true);
    
    // Rafraichissement du thumbnail
    this.repaint();

    // Mise é jour du nombre de label
    this.setLabelCount(this.getLabelCount() + 1);

    // Generation d'un evenement pour l'addition d'un label
    fireThumbnailAdded(this, label);
    
    return true;
  }
  
  /**
   * Remove the given thumbnail from this panel.
   * @param label the thumbnail to remove.
   * @return <code>true</code> if the thumbnail is removed and <code>false</code> otherwise.
   */
  public boolean remove(JThumbnail<T> label){

    if (labels.remove(label)){
      super.remove(label);
      this.setLabelCount(this.getLabelCount() - 1);
    } else {
      return false;
    }
    
    // Validation du placement par recalcul du layout
    this.validate();
    
    // Generation d'un evenement pour l'addition d'un label
    fireThumbnailRemoved(this, label);
    
    return true;  
  }
  

  /**
   * Add the label given in parameter to the selected labels
   * @param l ActiveLabel the label newly selected
   */
  public void select(JThumbnail<T> l) {

    selectedLabels.add(l);
    l.setSelected(true);

    refresh();
  }

  /**
   * Remove the label given in parameter to the selected labels
   * @param l ActiveLabel the label to remove selected
   */
  public void unselect(JThumbnail<T> l) {
    selectedLabels.remove(l);
    l.setSelected(false);
    refresh();
  }

  /**
   * Unselect all the selected label. The graphical refresh of the Active Label
   * is called by this method
   */
  public void unselectAll() {
    Iterator<JThumbnail<T>> iter = selectedLabels.iterator();
    
    JThumbnail<T> lr;
    while (iter.hasNext()) {
      lr = iter.next();
      lr.setSelected(false);
      lr.refresh();
    }
    
    selectedLabels.clear();
  }

  /**
   * Move view rect to the component assigned to selected indices.
   *
   */
  public void scrollToSelectedIndices(){
    //  Scroll automatique vers les composants selectiones
    int[] indices =  this.getSelectedIndices();
    if ((indices != null) && (indices.length > 0)){
      Rectangle rect = this.getCellBounds(indices[0],
                                                 indices[indices.length - 1]);
    
      this.scrollRectToVisible(rect);
    }  
  }
  

  /**
   * Move view rect to the component assigned the indices given in parameter.
   * @param indices the indices to view.
   */
  public void scrollToIndices(int[] indices){
    //  Scroll automatique vers les composants correspondants
    if ((indices != null) && (indices.length > 0) && (indices.length <= labels.size())){
      Rectangle rect = this.getCellBounds(indices[0],
                                                 indices[indices.length - 1]);
    
      this.scrollRectToVisible(rect);
    }  
  }
  
  /**
   * Get the active label displaying the content object. If no label is displaying
   * the given object, then <code>null</code> is returned.
   * @param content the content attached to the searched label.
   * @return the label.
   */
  public JThumbnail<T> getLabelContaining(Object content){
    
    Iterator<JThumbnail<T>> iter = null;
    JThumbnail<T> label          = null;
    boolean found              = false;
    
    if ((labels == null) || (labels.size() == 0)){
      return null;
    } else {
      iter = labels.iterator();
      while(iter.hasNext() && (!found)){
	label = iter.next();
	if (label.getContent() == null){
	  if (content == null){
	    found = true;
	  }
	} else {
	  found = label.getContent().equals(content);
	}
      }
      
      if (found == true){
	return label;
      } else {
	return null;
      }
    }
  }
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
// EVENEMENT
//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailAdded(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailAdded(panel, thumbnail);
      }
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailRemoved(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailRemoved(panel, thumbnail);
      }
    }
  }
 
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailSelected(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailSelected(panel, thumbnail);
      }
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailSelected(JThumbnailPanel<T> panel, List<JThumbnail<T>> thumbnails){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailSelected(panel, thumbnails);
      }
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailActivated(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailActivated(panel, thumbnail);
      }
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailEntered(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailEntered(panel, thumbnail);
      }
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailExited(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailExited(panel, thumbnail);
      }
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fireThumbnailNeedRefresh(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == JThumbnailPanelListener.class) {
        ( (JThumbnailPanelListener) listeners[i + 1]).thumbnailNeedRefresh(panel, thumbnail);
      }
    }
  }
  
  protected void fireEvent(TaskEvent e) {
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TaskListener.class) {
        ( (TaskListener) listeners[i + 1]).eventDispatched(e);
      }
    }
  }

  /**
   * Add an Active Thumbnail Listener to this Active Thumbnail
   * @param l ActiveThumbnailListener Listener added to the Active Thumbnail
   */
  public void addThumbnailPanelListener(JThumbnailPanelListener<T> l) {
    idListenerList.add(JThumbnailPanelListener.class, l);
  }

  /**
   * Remove an Active Thumbnail Listener from this Active Thumbnail
   * @param l ActiveThumbnailListener Active Listener to remove
   */
  public void removeThumbnailPanelListener(JThumbnailPanelListener<T> l) {
    idListenerList.remove(JThumbnailPanelListener.class, l);
  }

  /**
   * Add a Task Listener to this object
   * @param l TaskListener Listener added to the object
   */
  public void addTaskListener(TaskListener l) {
    idListenerList.add(TaskListener.class, l);
  }

  /**
   * Remove a Task Listener from this object
   * @param l TaskListener listener to remove
   */
  public void removeTaskListener(TaskListener l) {
    idListenerList.remove(TaskListener.class, l);
  }  
  
  /**
   * Process a MouseEvent fired by an active label.
   * @param e The event fired.
   */
  protected void processActiveLabelMouseEvent(MouseEvent e){

    int clickCount = 0;
    boolean shiftActivated = false;
    boolean ctrlActivated = false;

    
    clickCount = e.getClickCount();
    shiftActivated = e.isShiftDown();
    ctrlActivated = e.isControlDown();
    
    @SuppressWarnings("unchecked")
    JThumbnail<T> source = (JThumbnail<T>) e.getSource();
    
    int labelIndex = -1;
    
    int start = -1;
    int end   = -1;
    
    labelIndex = labels.indexOf(source);
    
    switch (e.getID()){
      case MouseEvent.MOUSE_CLICKED:
        // Click de selection
        if (clickCount == 1){
          
          
          switch (this.selectionMode){
            case ListSelectionModel.SINGLE_SELECTION:
              
              // Si SHIFT est active, ne rien faire
              if (shiftActivated){
                
              // Si CTRL est active et que l'on clique sur le label deje selectionne,
              // on le déselectionne. Si le label n'est pas celui cliqué, il est 
              // selectionne normalement
              } else if (ctrlActivated){
                if (labelIndex == this.lastSelectedIndex){
                  unselect(source);
                } else{
                  unselectAll();
                  select(source);
                }
              } else {
                unselectAll();
                select(source);  
              }
              this.lastSelectedIndex = labelIndex;
              fireThumbnailSelected(this, source);
              break;
              
            case ListSelectionModel.SINGLE_INTERVAL_SELECTION:
              labelIndex = labels.indexOf(source);
              // Verification que l'index de départ soit valide. Il peut être 
              // invalide si aucune valeur n'a été selectionnee.
              if (this.lastSelectedIndex < 0){
                this.lastSelectedIndex = 0;
              }
              
              // Creation d'un intervale de selection
              start = -1;
              end   = -1;
              if (labelIndex > this.lastSelectedIndex){
                start = lastSelectedIndex;
                end = labelIndex;
              } else {
                start = labelIndex;
                end = lastSelectedIndex;
              }
              
              // Desélection des anciens label et selection de la nouvelle plage 
              unselectAll();
              for(int i = start; i<= end; i++){
                select(labels.get(i));
              }
              
              this.lastSelectedIndex = labelIndex;
              fireThumbnailSelected(this, selectedLabels);
              break;
              
            case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:
              
              // Selection simple en cas de click sans modification
              if ((!shiftActivated) && (!ctrlActivated)){
                unselectAll();
                select(source); 
                
              // Selection / Deselection d'un label si appui sur la touche CTRL
              } else if ((!shiftActivated) && (ctrlActivated)){
                if (labelIndex == this.lastSelectedIndex){
                  unselect(source);
                } else{
                  select(source);
                }  
                
              // Selection d'une plage continue de label en cas d'appui sur
              // la touche SHIFT
              } else if ((shiftActivated) && (!ctrlActivated)){
                // Verification que l'index de départ soit valide. Il peut être 
                // invalide si aucune valeur n'a été selectionnee.
                if (this.lastSelectedIndex < 0){
                  this.lastSelectedIndex = 0;
                }
                
                // Creation d'un intervale de selection
                start = -1;
                end   = -1;
                if (labelIndex > this.lastSelectedIndex){
                  start = lastSelectedIndex;
                  end = labelIndex;
                } else {
                  start = labelIndex;
                  end = lastSelectedIndex;
                }
                
                // Desélection des anciens label et selection de la nouvelle plage 
                unselectAll();
                for(int i = start; i<= end; i++){
                  select(labels.get(i));
                }
                
              // Selection de plusieurs plages de labels  
              } else if ((shiftActivated) && (ctrlActivated)){
                // Verification que l'index de départ soit valide. Il peut être 
                // invalide si aucune valeur n'a été selectionnee.
                if (this.lastSelectedIndex < 0){
                  this.lastSelectedIndex = 0;
                }  
                
                // Creation d'un intervale de selection
                start = -1;
                end   = -1;
                if (labelIndex > this.lastSelectedIndex){
                  start = lastSelectedIndex;
                  end = labelIndex;
                } else {
                  start = labelIndex;
                  end = lastSelectedIndex;
                }
                
                // Selection de la nouvelle plage de label en faisant attention
                // de ne pas selectionner deux fois les meme labels
                for(int i = start; i<= end; i++){
                  if (!selectedLabels.contains(source)){
                    select(labels.get(i));
                  }
                }
                
              }
              
              this.lastSelectedIndex = labelIndex;
              fireThumbnailSelected(this, selectedLabels);
              break;
          }
          
          // Si shift est active durant la selection
          if (shiftActivated){
            
          }
        } else if (e.getClickCount() > 1){
          this.activatedLabel = source;
          fireThumbnailActivated(this, source);
        }
        break;
    }
  }
//LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
// FIN LISTENER
//LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL


}