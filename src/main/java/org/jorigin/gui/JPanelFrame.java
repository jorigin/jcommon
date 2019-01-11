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
package org.jorigin.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

import org.jorigin.Common;

/**
 * A <code>JPanel</code> subclass that has a drop shadow border and
 * that provides a header with icon, title and tool bar.<p>
 *
 * This class can be used to replace the <code>JInternalFrame</code>,
 * for use outside of a <code>JDesktopPane</code>.
 * The <code>SimpleInternalFrame</code> is less flexible but often
 * more usable; it avoids overlapping windows and scales well
 * up to IDE size.
 * Several customers have reported that they and their clients feel
 * much better with both the appearance and the UI feel.<p>
 *
 * The SimpleInternalFrame provides the following bound properties:
 * <i>frameIcon, title, toolBar, content, selected.</i><p>
 *
 * By default the SimpleInternalFrame is in <i>selected</i> state.
 * If you don't do anything, multiple simple internal frames will
 * be displayed as selected.
 *
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.1
 *
 * @see    javax.swing.JInternalFrame
 * @see    javax.swing.JDesktopPane
 */
public class JPanelFrame extends JPanel implements RootPaneContainer{

    private static final long serialVersionUID         = Common.BUILD;
  
    private JLabel        titleLabel        = null;
    private GradientPanel gradientPanel     = null;
    private JPanel        headerPanel       = null ;
    private boolean       selected          = false;

    private JRootPane     rootPane          = null;

    /** If it's true, calls to add and setLayout are forwarded to the contentPane */
    private boolean rootPaneCheckingEnabled = true;

    /** Title icon of the panel frame */
    private Image iconImage                 = null;

    // Instance Creation ****************************************************

    /**
     * Constructs a JPanelFrame with the specified title.
     * The title is intended to be non-blank, or in other words
     * should contain non-space characters.
     *
     * @param title       the initial title
     */
    public JPanelFrame(String title) {
        this(null, title, null, null);
    }


    /**
     * Constructs a JPanelFrame with the specified
     * icon, and title.
     *
     * @param icon        the initial icon
     * @param title       the initial title
     */
    public JPanelFrame(Icon icon, String title) {
        this(icon, title, null, null);
    }


    /**
     * Constructs a JPanelFrame with the specified
     * title, tool bar, and content panel.
     *
     * @param title       the initial title
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public JPanelFrame(String title, JToolBar bar, JComponent content) {
        this(null, title, bar, content);
    }


    /**
     * Constructs a JPanelFrame with the specified
     * icon, title, tool bar, and content panel.
     *
     * @param icon        the initial icon
     * @param title       the initial title
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public JPanelFrame(Icon icon, String title, JToolBar bar, JComponent content) {
      super(new BorderLayout());
      this.createRootPane();
      this.selected = false;
      this.titleLabel = new JLabel(title, icon, SwingConstants.LEADING);

      // Creation de l'en tête du panneau fenêtre
      JPanel top = buildHeader(titleLabel, bar);

      // Ajout de l'en tête du panneau fenêtre au panel
      super.addImpl(top, BorderLayout.NORTH, -1);

      // Ajout du panneau racine au centre du panneau fenêtre
      super.addImpl(rootPane, BorderLayout.CENTER, -1);

      if (content != null) {
        this.getContentPane().add(content);
      }

      setBorder(new ShadowBorder());
      setSelected(true);
      updateHeader();
    }


//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II IMPLEMENTATION                                                           II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

  /**
   * Return this component's single JRootPane child.  A conventional
   * implementation of this interface will have all of the other
   * methods indirect through this one.  The rootPane has two
   * children: the glassPane and the layeredPane.
   *
   * @return this components single JRootPane child.
   * @see JRootPane
   */
  public JRootPane getRootPane(){
    return this.rootPane;
  }


  /**
   * The "contentPane" is the primary container for application
   * specific components.  Applications should add children to
   * the contentPane, set its layout manager, and so on.
   * <p>
   * The contentPane my not be null.
   * <p>
   * Generally implemented with
   * <code>getRootPane().setContentPane(contentPane);</code>
   * @param contentPane the Container to use for the contents of this
   *        JRootPane
   * @exception java.awt.IllegalComponentStateException (a runtime
   *            exception) if the content pane parameter is null
   * @see JRootPane#getContentPane
   * @see #getContentPane
   */
  public void setContentPane(Container contentPane){

  }


  /**
   * Returns the contentPane.
   *
   * @return the value of the contentPane property.
   * @see #setContentPane
   */
  public Container getContentPane(){
    return this.getRootPane().getContentPane();
  }


  /**
   * A Container that manages the contentPane and in some cases a menu bar.
   * The layeredPane can be used by descendants that want to add a child
   * to the RootPaneContainer that isn't layout managed.  For example
   * an internal dialog or a drag and drop effect component.
   * <p>
   * The layeredPane may not be null.
   * <p>
   * Generally implemented with<pre>
   *    getRootPane().setLayeredPane(layeredPane);</pre>
   *
   * @exception java.awt.IllegalComponentStateException (a runtime
   *            exception) if the layered pane parameter is null
   * @see #getLayeredPane
   * @see JRootPane#getLayeredPane
   */
  public void setLayeredPane(JLayeredPane layeredPane){
    getRootPane().setLayeredPane(layeredPane);
  }


  /**
   * Returns the layeredPane.
   *
   * @return the value of the layeredPane property.
   * @see #setLayeredPane
   */
  public JLayeredPane getLayeredPane(){
    return getRootPane().getLayeredPane();
  }


  /**
   * The glassPane is always the first child of the rootPane
   * and the rootPanes layout manager ensures that it's always
   * as big as the rootPane.  By default it's transparent and
   * not visible.  It can be used to temporarily grab all keyboard
   * and mouse input by adding listeners and then making it visible.
   * by default it's not visible.
   * <p>
   * The glassPane may not be null.
   * <p>
   * Generally implemented with
   * <code>getRootPane().setGlassPane(glassPane);</code>
   *
   * @see #getGlassPane
   * @see JRootPane#setGlassPane
   */
  public void setGlassPane(Component glassPane){
    getRootPane().setGlassPane(glassPane);
  }


  /**
   * Returns the glassPane.
   *
   * @return the value of the glassPane property.
   * @see #setGlassPane
   */
  public Component getGlassPane(){
    return getRootPane().getGlassPane();
  }
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//II FIN IMPLEMENTATION                                                       II
//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
//SS SURCHARGES                                                               SS
//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

  /**
   * Appends the specified component to the end of this container.
   * This is a convenience method for addImpl(java.awt.Component, java.lang.Object, int).<br>
   * Note: If a component has been added to a container that has been displayed,
   * validate must be called on that container to display the new component.
   * If multiple components are being added, you can improve efficiency by calling
   * validate only once, after all the components have been added.
   * @param comp Component the component to be added
   * @return Component the component argument
   * @see #validate()
   * @see javax.swing.JComponent#revalidate()
   */
  public Component add(Component comp){
    addImpl(comp, null, -1);
    return comp;
  }


  /**
   * Adds the specified component to this container at the given position.
   * This is a convenience method for addImpl(java.awt.Component, java.lang.Object, int).<br>
   * Note: If a component has been added to a container that has been displayed,
   * validate must be called on that container to display the new component.
   * If multiple components are being added, you can improve efficiency by
   * calling validate only once, after all the components have been added.
   * @param comp Component the component to be added
   * @param index the position at which to insert the component, or -1 to append the component to the end
   * @return the added component.
   * @see #remove(int)
   * @see #validate()
   * @see javax.swing.JComponent#revalidate()
   */
  public Component add(Component comp, int index){
    addImpl(comp, null, index);
    return comp;
  }


  /**
   * Adds the specified component to this container with the specified constraints
   * to the end of this conainer. Also notifies the layout manager to add the component
   * to the this container's layout using the specified constraints object.
   * This is a convenience method for addImpl(java.awt.Component, java.lang.Object, int).<br><br>
   * Note: If a component has been added to a container that has been displayed,
   * validate must be called on that container to display the new component.
   * If multiple components are being added, you can improve efficiency by calling
   * validate only once, after all the components have been added.
   * @param comp Component the component to be added
   * @param constraints Object an object expressing layout contraints for this
   * @see #validate()
   * @see javax.swing.JComponent#revalidate()
   * @see #remove(int)
   * @see java.awt.LayoutManager
   */
  public void add(Component comp, Object constraints){
    addImpl(comp, constraints, -1);
  }


  /**
   * Adds the specified component to this container with the specified constraints
   * at the specified index. Also notifies the layout manager to add the component
   * to the this container's layout using the specified constraints object.
   * This is a convenience method for addImpl(java.awt.Component, java.lang.Object, int).<br><br>
   * Note: If a component has been added to a container that has been displayed,
   * validate must be called on that container to display the new component.
   * If multiple components are being added, you can improve efficiency by calling
   * validate only once, after all the components have been added.
   * @param comp Component the component to be added
   * @param constraints Object an object expressing layout constraints for this
   * @param index int the position in the container's list at which to
   * insert the component; -1 means insert at the end component
   * @see #validate()
   * @see javax.swing.JComponent#revalidate()
   * @see #remove(int)
   * @see java.awt.LayoutManager
   */
  public void add(Component comp, Object constraints, int index){
    addImpl(comp, constraints, index);
  }

  /**
   * Removes the component, specified by index,
   * from this container.
   * This method also notifies the layout manager to remove the component from
   * this container's layout via the removeLayoutComponent method.
   * @param index the index of the component to be removed
   * @see #add(java.awt.Component)
   */
  public void remove(int index){
    getContentPane().remove(index);
  }


  /**
   * Removes all the components from this container. This method also notifies
   * the layout manager to remove the components from this container's
   * layout via the removeLayoutComponent method.
   * @see #add(java.awt.Component)
   * @see #remove(int)
   */
  public void removeAll(){
    getContentPane().removeAll();
  }
//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
//SS FIN SURCHARGES                                                           SS
//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS


  /**
   * Called by the constructor methods to create the default rootPane.
   */
  protected void createRootPane(){
    this.rootPane = new JRootPane();
    this.rootPane.getContentPane().setLayout(new BorderLayout());
  }



//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                                               AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA




//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                                           AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA


//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
//FF METHODES DE JFRAME                                                       FF
//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

  /**
   * Adds the specified child Component. This method is overridden to conditionally
   * forwad calls to the contentPane.
   * By default, children are added to the contentPane instead of the frame,
   * refer to RootPaneContainer for details.
   * @param comp the component to be enhanced
   * @param constraints the constraints to be respected
   * @param index the index
   * @see #setRootPaneCheckingEnabled(boolean)
   * @see javax.swing.RootPaneContainer
   */
  protected void addImpl(Component comp, Object constraints, int index){
    if (this.rootPaneCheckingEnabled){
      getContentPane().add(comp, constraints, index);
    } else{
      super.add(comp, constraints, index);
    }
  }


  /**
   * Sets the menubar for this panel frame.
   * @param menu JMenuBar the menubar being placed in the frame
   * @see #getJMenuBar()
   */
  public void setJMenuBar(JMenuBar menu) {
    this.getRootPane().setJMenuBar(menu);
  }


  /**
   * Returns the menubar set on this panel frame.
   * @return JMenuBar the menubar for this frame
   * @see #setJMenuBar(javax.swing.JMenuBar)
   */
  public JMenuBar getJMenuBar() {
    return getRootPane().getJMenuBar();
  }


  /**
   * Sets whether calls to add and setLayout are forwarded to the contentPane.
   * @param enabled boolean true if add and setLayout  are forwarded,
   * false if they should operate directly on the JPanelFrame.
   * @see #addImpl(java.awt.Component, java.lang.Object, int)
   * @see #setLayout(java.awt.LayoutManager)
   * @see #isRootPaneCheckingEnabled()
   * @see javax.swing.RootPaneContainer

   */
  protected void setRootPaneCheckingEnabled(boolean enabled) {
    this.rootPaneCheckingEnabled = enabled;
  }


  /**
   * Returns whether calls to add and setLayout are forwarded to the contentPane.
   * @return boolean true if add and setLayout are fowarded; false otherwise
   * @see #addImpl(java.awt.Component, java.lang.Object, int)
   * @see #setLayout(java.awt.LayoutManager)
   * @see #setRootPaneCheckingEnabled(boolean)
   * @see javax.swing.RootPaneContainer
   */
  protected boolean isRootPaneCheckingEnabled() {
    return this.rootPaneCheckingEnabled;
  }


  /**
   * Processes window events occurring on this component.
   * Hides the window or disposes of it, as specified by
   * the setting of the defaultCloseOperation property.
   * @param e the window event
   */
  protected void processWindowEvent(WindowEvent e) {
  }


  /**
   * Removes the specified component from the container.
   * If comp is not the rootPane, this will forward the call to the contentPane.
   * This will do nothing if comp is not a child of the JFrame or contentPane.
   * @param comp Component the component to be removed
   * @see java.awt.Container#add(java.awt.Component)
   * @see javax.swing.RootPaneContainer
   */
  public void remove(Component comp) {
    if (comp != this.rootPane) {
      getContentPane().remove(comp);
    }
  }


  /**
   * A convenience method for setting default close operation.
   * @param operation the operation processed. By default this method does nothing.
   * @see javax.swing.JFrame#setDefaultCloseOperation(int)
   */
  public void setDefaultCloseOperation(int operation) {
  }


  /**
   * A convenience method for getting default close operation.
   * By default this method return 0.
   * @return the default close operation.
   */
  public int getDefaultCloseOperation() {
    return 0;
  }

  /**
   * Just calls paint(g). This method was overridden to prevent an unnecessary
   * call to clear the background.
   * @param g the Graphics context in which to paint
   * @see java.awt.Component#update(Graphics)
   */
  public void update(Graphics g) {
    this.paint(g);
  }


  /**
   * Sets the image to be displayed in the minimized icon for this frame.
   * Not all platforms support the concept of minimizing a window.
   * @param image the icon image to be displayed.
   * If this parameter is null then the icon image is set to the default image,
   * which may vary with platform.
   * @see java.awt.Frame#getIconImage()
   */
  public void setIconImage(Image image){
    this.iconImage = image;
  }
  
  /**
   * Get the image that is displayed in the panel.
   * @return the icon image that is displayed.
   */
  public Image getIconImage(){
    return iconImage;
  }
//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
//FF METHODES DE JFRAME                                                       FF
//FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

  /**
   * Returns the frame's icon.
   * @return the frame's icon
   */
  public Icon getFrameIcon() {
    return titleLabel.getIcon();
  }


  /**
   * Sets a new frame icon.
   * @param newIcon the icon to be set
   */
  public void setFrameIcon(Icon newIcon) {
    Icon oldIcon = getFrameIcon();
    titleLabel.setIcon(newIcon);
    firePropertyChange("frameIcon", oldIcon, newIcon);
  }


  /**
   * Returns the frame's title text.
   * @return String   the current title text
   */
  public String getTitle() {
    return titleLabel.getText();
  }


  /**
   * Sets a new title text.
   * @param newText  the title text tp be set
   */
  public void setTitle(String newText) {
    String oldText = getTitle();
    titleLabel.setText(newText);
    firePropertyChange("title", oldText, newText);
  }


  /**
   * Returns the current {@link javax.swing.JToolBar JToolBar}, <code>null</code> if none has been set before.
   * @return the current toolbar - if any
   */
  public JToolBar getToolBar() {
    return headerPanel.getComponentCount() > 1
            ? (JToolBar) headerPanel.getComponent(1)
            : null;
  }


  /**
   * Sets a new {@link javax.swing.JToolBar JToolBar} in the header.
   * @param newToolBar the tool bar to be set in the header
   */
  public void setToolBar(JToolBar newToolBar) {
    JToolBar oldToolBar = getToolBar();
    if (oldToolBar == newToolBar) {
      return;
    }
    if (oldToolBar != null) {
      headerPanel.remove(oldToolBar);
    }
    if (newToolBar != null) {
      newToolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      headerPanel.add(newToolBar, BorderLayout.EAST);
    }
    updateHeader();
    firePropertyChange("toolBar", oldToolBar, newToolBar);
  }


  /**
   * Returns the content - <code>null</code>, if none has been set.
   * @return the current content
   */
  public Component getContent() {
    //return hasContent() ? getComponent(1) : null;
    return getContentPane();
  }


  /**
   * Answers if the panel is currently selected (or in other words active)
   * or not. In the selected state, the header background will be
   * rendered differently.
   * @return boolean  a boolean, where true means the frame is selected
   *                  (currently active) and false means it is not
   */
  public boolean isSelected() {
    return selected;
  }


  /**
   * This panel draws its title bar differently if it is selected,
   * which may be used to indicate to the user that this panel
   * has the focus, or should get more attention than other
   * simple internal frames.
   *
   * @param newValue  a boolean, where true means the frame is selected
   *                  (currently active) and false means it is not
   */
  public void setSelected(boolean newValue) {
    boolean oldValue = isSelected();
    selected = newValue;
    updateHeader();
    firePropertyChange("selected", oldValue, newValue);
  }


  // Building *************************************************************

  /**
   * Creates the header panel composed of:<br><br>
   * - an icon<br>
   * - a title label<br>
   * - a tool bar<br>
   * - a gradient background.<br><br>
   * @param label   the label to paint the icon and text
   * @param bar     the panel's tool bar
   * @return the panel's built header area
   */
  private JPanel buildHeader(JLabel label, JToolBar bar) {
    gradientPanel = new GradientPanel(new BorderLayout(), getHeaderBackground());
    label.setOpaque(false);

    gradientPanel.add(label, BorderLayout.WEST);
    gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));

    headerPanel = new JPanel(new BorderLayout());
    headerPanel.add(gradientPanel, BorderLayout.CENTER);
    setToolBar(bar);
    headerPanel.setBorder(new RaisedHeaderBorder());
    headerPanel.setOpaque(false);
    return headerPanel;
  }

  /**
   * Updates the header.
   */
  private void updateHeader() {
    gradientPanel.setBackground(getHeaderBackground());
    gradientPanel.setOpaque(isSelected());
    titleLabel.setForeground(getTextForeground(isSelected()));
    headerPanel.repaint();
  }


  /**
   * Updates the UI. In addition to the superclass behavior, we need
   * to update the header component.
   */
  public void updateUI() {
    super.updateUI();
    if (titleLabel != null) {
      updateHeader();
    }
  }


  // Helper Code **********************************************************

  /**
   * Determines and answers the header's text foreground color.
   * Tries to lookup a special color from the L&amp;F.
   * In case it is absent, it uses the standard internal frame forground.
   *
   * @param isSelected   true to lookup the active color, false for the inactive
   * @return the color of the foreground text
   */
  protected Color getTextForeground(boolean isSelected) {
    Color c = null;

    if (isSelected){
      c = UIManager.getColor("SimpleInternalFrame.activeTitleForeground");
    } else{
      c = UIManager.getColor("SimpleInternalFrame.inactiveTitleForeground");
    }

    if (c != null) {
      return c;
    } else{
      if (isSelected){
        return UIManager.getColor("InternalFrame.activeTitleForeground");
      } else{
        return UIManager.getColor("Label.foreground");
      }
    }
  }

  /**
   * Determines and answers the header's background color.
   * Tries to lookup a special color from the L&amp;F.
   * In case it is absent, it uses the standard internal frame background.
   *
   * @return the color of the header's background
   */
  protected Color getHeaderBackground() {
    Color c = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");

    //if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED)
    //    c = UIManager.getColor("InternalFrame.activeTitleGradient");

    if (c != null){
      return c;
    } else{
      return UIManager.getColor("InternalFrame.activeTitleBackground");
    }
  }


  // Helper Classes *******************************************************

  // A custom border for the raised header pseudo 3D effect.
  private static class RaisedHeaderBorder extends AbstractBorder {

    /**
     * 
     */
    private static final long serialVersionUID = Common.BUILD;
    
    private static final Insets INSETS = new Insets(1, 1, 1, 0);

    public Insets getBorderInsets(Component c) {
      return INSETS;
    }

    public void paintBorder(Component c, Graphics g,
                            int x, int y, int w, int h) {

      g.translate(x, y);
      g.setColor(UIManager.getColor("controlLtHighlight"));
      g.fillRect(0, 0, w, 1);
      g.fillRect(0, 1, 1, h - 1);
      g.setColor(UIManager.getColor("controlShadow"));
      g.fillRect(0, h - 1, w, 1);
      g.translate( -x, -y);
    }
  }


  // A custom border that has a shadow on the right and lower sides.
  private static class ShadowBorder extends AbstractBorder {

    private static final long serialVersionUID = Common.BUILD;
    
    private static final Insets INSETS = new Insets(1, 1, 3, 3);

    public Insets getBorderInsets(Component c) {
      return INSETS;
    }

    public void paintBorder(Component c, Graphics g,
                            int x, int y, int w, int h) {

      Color shadow = UIManager.getColor("controlShadow");
      if (shadow == null) {
        shadow = Color.GRAY;
      }
      Color lightShadow = new Color(shadow.getRed(),
                                    shadow.getGreen(),
                                    shadow.getBlue(),
                                    170);
      Color lighterShadow = new Color(shadow.getRed(),
                                      shadow.getGreen(),
                                      shadow.getBlue(),
                                      70);
      g.translate(x, y);

      g.setColor(shadow);
      g.fillRect(0, 0, w - 3, 1);
      g.fillRect(0, 0, 1, h - 3);
      g.fillRect(w - 3, 1, 1, h - 3);
      g.fillRect(1, h - 3, w - 3, 1);
      // Shadow line 1
      g.setColor(lightShadow);
      g.fillRect(w - 3, 0, 1, 1);
      g.fillRect(0, h - 3, 1, 1);
      g.fillRect(w - 2, 1, 1, h - 3);
      g.fillRect(1, h - 2, w - 3, 1);
      // Shadow line2
      g.setColor(lighterShadow);
      g.fillRect(w - 2, 0, 1, 1);
      g.fillRect(0, h - 2, 1, 1);
      g.fillRect(w - 2, h - 2, 1, 1);
      g.fillRect(w - 1, 1, 1, h - 2);
      g.fillRect(1, h - 1, w - 2, 1);
      g.translate( -x, -y);
    }
  }


  // A panel with a horizontal gradient background.
  private static class GradientPanel extends JPanel {

    private static final long serialVersionUID = Common.BUILD;
    
    private GradientPanel(LayoutManager lm, Color background) {
      super(lm);
      setBackground(background);
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (!isOpaque()) {
        return;
      }
      Color control = UIManager.getColor("control");
      int width = getWidth();
      int height = getHeight();

      Graphics2D g2 = (Graphics2D) g;
      Paint storedPaint = g2.getPaint();
      g2.setPaint(
              new GradientPaint(0, 0, getBackground(), width, 0, control));
      g2.fillRect(0, 0, width, height);
      g2.setPaint(storedPaint);
    }
  }

}