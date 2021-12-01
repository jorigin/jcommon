package org.jorigin.swing.thumbnail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;

import org.jorigin.Common;

/**
 * A SWING component that is dedicated to the display of a thumbnail.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.8
 * @param <T> the type of the object displayed within the thumbnail.
 */
public class JThumbnail<T> extends JPanel 
implements EventListener, MouseListener{


	private static final long serialVersionUID = Common.BUILD;

	/**
	 * The {@link JPanel panel} dedicated to thumbnail content display.
	 */
	protected JPanel thumbnailPN = null;

	/** The thumbnail name label */
	protected JLabel nameLB      = null;

	/** The thumbnail margin */
	protected int thumbnailMargin = 5;

	/** The thumbnail identifier font */
	protected Font idFont;

	/** The thumbnail identifier font color */
	protected Color idColor = Color.BLACK;

	/** The thumbnail standard background color */
	protected Color standardBgColor = (Color)UIManager.get("List.background");

	/** The thumbnail focused background color */
	protected Color focusBgColor = (Color)UIManager.get("Label.background");

	/** The thumbnail activated background color */
	protected Color selectedBgColor = (Color)UIManager.get("List.selectionBackground");

	/** The thumbnail marked background color */
	protected Color markedBgColor = Color.YELLOW;

	/** The thumbnail focused border */
	protected Border focusedBorder = (Border)UIManager.get("List.focusCellHighlightBorder");

	/** The thumbnail current border */
	protected Border currentBorder = null;

	/** The thumbnail current background color */
	protected Color currentBgColor = standardBgColor;

	// Variables d'etat du label

	/** Is the label is in standard state. */
	protected boolean standard = true;

	/** Is the label is in focused state. */
	protected boolean focused = false;

	/** Is the label is in selected state. */
	protected boolean selected = false;

	/** Is the label is in marked state. */
	protected boolean marked = false;

	/** The thumbnail numerical identifier. */
	protected int thumbnailID;

	/** The thumbnail name. */
	protected String name;

	/** The thumbnail underlying content. */
	protected T content;

	/** The thumbnail displayed component. */
	protected JComponent thumbnailComponent;

	/**
	 * Liste des ecouteurs informes des evenements du label
	 */
	protected EventListenerList idListenerList = new EventListenerList();

	/**
	 * Is the name of the thumbnail have to be visible.<br>
	 * By default this value is set to <code>true</code>.
	 */
	protected boolean nameVisible  = true;

	/**
	 * Where the name of the label has to be visible. Possible values can be:
	 * <ul>
	 * <li>{@link BorderLayout#NORTH}
	 * <li>{@link BorderLayout#SOUTH}
	 * <li>{@link BorderLayout#EAST}
	 * <li>{@link BorderLayout#WEST}
	 * </ul>
	 * by default this value is set to {@link BorderLayout#SOUTH}.
	 */
	protected String nameOrientation = BorderLayout.SOUTH;

	/**
	 * Is the numerical identifier of the thumbnail has to be visible.<br>
	 * By default this value is set to <code>true</code>.
	 */
	protected boolean idVisible = true;

	/**
	 * Create a new thumbnail.
	 * @param name the name of the thumbnail.
	 * @param width the width of the thumbnail.
	 * @param height the height of the thumbnail.
	 * @param margin the margin size of the thumbnail.
	 * @param ID the identifier of the thumbnail.
	 */
	public JThumbnail(String name, int width, int height, int margin, int ID){
		super();

		nameLB = new JLabel(name);

		thumbnailPN = new JPanel();
		thumbnailPN.setBackground(Color.cyan);
		thumbnailPN.setLayout(new FlowLayout());

		this.setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		BorderLayout labelLayout = new BorderLayout();
		labelLayout.setVgap(margin);
		labelLayout.setHgap(margin);

		this.setLayout(labelLayout);

		this.add(thumbnailPN, BorderLayout.CENTER);
		this.add(nameLB, nameOrientation);

		idFont = new Font("Dialog", Font.BOLD, 11);

		focused  = false;
		selected = false;
		marked   = false;

		this.thumbnailMargin = margin;

		this.setBackground(standardBgColor);

		this.thumbnailID = ID;

		this.setName(name);
		this.setContent(content);
		this.setToolTipText(name);

		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				processLabelMouseEvent(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				processLabelMouseEvent(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				processLabelMouseEvent(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				processLabelMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				processLabelMouseEvent(e);
			}});

		refreshGUI();
	}

	/**
	 * Create a new thumbnail that display the given component.
	 * @param name the name of the thumbnail.
	 * @param content the content of the thumbnail.
	 * @param component the component to use as display.
	 * @param width the width of the thumbnail.
	 * @param height the height of the thumbnail.
	 * @param margin the margin size of the thumbnail.
	 * @param labelID the identifier of the thumbnail.
	 */
	public JThumbnail(String name, T content, JComponent component, int width, int height, int margin,
			int labelID){

		// Creation du JPanel
		this(name, width, height, margin, labelID);

		this.content = content;
		this.thumbnailComponent = component;
		this.thumbnailComponent.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				processComponentMouseEvent(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				processComponentMouseEvent(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				processComponentMouseEvent(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				processComponentMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				processComponentMouseEvent(e);
			}});

		thumbnailPN.add(this.thumbnailComponent, BorderLayout.CENTER);

		refreshGUI();
	}

	/**
	 * Set if the thumbnail is standard.
	 * @param b <code>true</code> if the thumbnail is standard and <code>false</code> otherwise.
	 * @see #isStandard()
	 */
	public void setStandard(boolean b){
		standard = b;
		refresh();
	}

	/**
	 * Get if the thumbnail is standard.
	 * @return <code>true</code> if the thumbnail is standard and <code>false</code> otherwise.
	 * @see #setStandard(boolean)
	 */
	public boolean isStandard(){
		return standard;
	}

	/**
	 * Set if the thumbnail is marked.
	 * @param b <code>true</code> if the thumbnail is marked and <code>false</code> otherwise.
	 * @see #isMarked()
	 */
	public void setMarked(boolean b){
		marked = b;
		refresh();
	}

	/**
	 * Get if the thumbnail is marked.
	 * @return <code>true</code> if the thumbnail is marked and <code>false</code> otherwise.
	 * @see #setMarked(boolean)
	 */
	public boolean isMarked(){
		return marked;
	}

	/**
	 * Get if the thumbnail is focused.
	 * @param b <code>true</code> if the thumbnail is focused and <code>false</code> otherwise.
	 * @see #isFocused()
	 */
	public void setFocused(boolean b){
		focused = b;
		refresh();
	}

	/**
	 * Set if the thumbnail is focused.
	 * @return <code>true</code> if the thumbnail is focused and <code>false</code> otherwise.
	 * @see #setFocused(boolean)
	 */
	public boolean isFocused(){
		return focused;
	}

	@Override
	public void setName(String n){
		this.name = n;
	}

	@Override
	public String getName(){
		return this.name;
	}

	/**
	 * Set the content that is displayed within this thumbnail.
	 * @param content the content that is displayed within this thumbnail.
	 * @see #getContent()
	 */
	public void setContent(T content){
		this.content = content;
	}

	/**
	 * Get the content that is displayed within this thumbnail.
	 * @return the content that is displayed within this thumbnail.
	 * @see #setContent(Object)
	 */
	public T getContent(){
		return this.content;
	}

	/**
	 * Get the identifier of the thumbnail.
	 * @return the identifier of the thumbnail.
	 */
	public int getID(){
		return this.thumbnailID;
	}

	/**
	 * Set if the thumbnail is selected.
	 * @param b <code>true</code> if the thumbnail is selected and <code>false</code> otherwise.
	 * @see #isSelected()
	 */
	public void setSelected(boolean b){
		selected = b;
		refresh();
	}

	/**
	 * Get if the thumbnail is selected.
	 * @return <code>true</code> if the thumbnail is selected and <code>false</code> otherwise.
	 * @see #setSelected(boolean)
	 */
	public boolean isSelected(){
		return selected;
	}

	/**
	 * Set the {@link Font font} to use for the thumbnail identifier display.
	 * @param f the font to use for the thumbnail identifier display.
	 */
	public void setIdFont(Font f){
		idFont = f;
	}

	/**
	 * Get if the thumbnail name is visible.
	 * @return <code>true</code> if the thumbnail name is visible and <code>false</code> otherwise.
	 * @see #setNameVisible(boolean)
	 * @see #getNameOrientation()
	 */
	public boolean isNameVisible() {
		return nameVisible;
	}

	/**
	 * Set if the thumbnail name has to be visible.
	 * @param nameVisible  <code>true</code> if the thumbnail name has to be visible and <code>false</code> otherwise.
	 * @see #isNameVisible()
	 * @see #setNameOrientation(String)
	 */
	public void setNameVisible(boolean nameVisible) {
		this.nameVisible = nameVisible;
	}

	/**
	 * Get the orientation of the thumbnail name. Possible values can be:
	 * <ul>
	 * <li>{@link BorderLayout#NORTH}
	 * <li>{@link BorderLayout#SOUTH}
	 * <li>{@link BorderLayout#EAST}
	 * <li>{@link BorderLayout#WEST}
	 * </ul>
	 * by default this value is set to {@link BorderLayout#SOUTH}.
	 * @return the orientation of the thumbnail name.
	 * @see #setNameOrientation(String)
	 * @see #isNameVisible()
	 */
	public String getNameOrientation() {
		return nameOrientation;
	}

	/**
	 * Set the orientation of the thumbnail name. Possible values can be:
	 * <ul>
	 * <li>{@link BorderLayout#NORTH}
	 * <li>{@link BorderLayout#SOUTH}
	 * <li>{@link BorderLayout#EAST}
	 * <li>{@link BorderLayout#WEST}
	 * </ul>
	 * by default this value is set to {@link BorderLayout#SOUTH}.
	 * @param nameOrientation the orientation of the thumbnail name.
	 * @see #getNameOrientation()
	 * @see #setNameVisible(boolean)
	 */
	public void setNameOrientation(String nameOrientation) {
		this.nameOrientation = nameOrientation;
	}

	/**
	 * Get if the thumbnail numerical identifier is visible.
	 * @return <code>true</code> if the thumbnail numerical identifier is visible and <code>false</code> otherwise.
	 * @see #setIdVisible(boolean)
	 */
	public boolean isIdVisible() {
		return idVisible;
	}

	/**
	 * Set if the thumbnail numerical identifier has to be visible.
	 * @param visible <code>true</code> if the thumbnail numerical identifier has to be visible and <code>false</code> otherwise.
	 * @see #isIdVisible()
	 */
	public void setIdVisible(boolean visible) {
		this.idVisible = visible;
	}

	/**
	 * Refresh the graphical components of the thumbnail.
	 */
	public void refresh(){

		if (standard){
			currentBgColor = standardBgColor;
			currentBorder = null;
		}
		if (marked){
			currentBgColor = markedBgColor;
			currentBorder  = null;
		}
		if (focused){
			currentBgColor = focusBgColor;
			currentBorder = focusedBorder;
		}
		if (selected){
			currentBgColor = selectedBgColor;
			currentBorder = null;
		}

		this.setBorder(currentBorder);
		this.setBackground(currentBgColor);

		if (this.thumbnailComponent != null){
			this.thumbnailComponent.validate();
			this.thumbnailComponent.setVisible(true);
			this.thumbnailComponent.repaint();
		}

		this.repaint();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		// Sauvegarde de la couleur courante
		Color oldColor = g.getColor();

		//Dessine l'image sur le panel en respectant les marges
		//if (labelComponent != null){
		//  labelComponent.paint(g);
		//}

		if (idVisible){
			g.setColor(currentBgColor);
			g.fillRect(0, 0, 15, 10);
			g.setColor(idColor);
			g.setFont(idFont);
			g.drawString(""+thumbnailID, 3, 8);
		}

		if (nameVisible){
			nameLB.setText(getName());
		} else {
			nameLB.setText(null);
		}

		g.setColor(oldColor);

	}

	/**
	 * Fire a thumbnail focused event.
	 * @param thumbnail the thumbnail that has raised the event.
	 */
	@SuppressWarnings("unchecked")
	protected void fireThumbnailFocused(JThumbnail<T> thumbnail){
		Object[] listeners = idListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == JThumbnailListener.class) {
				((JThumbnailListener<T>) listeners[i + 1]).thumbnailFocused(thumbnail);
			}
		}
	}

	/**
	 * Fire a thumbnail unfocused event.
	 * @param thumbnail the thumbnail that has raised the event.
	 */
	@SuppressWarnings("unchecked")
	protected void fireThumbnailUnfocused(JThumbnail<T> thumbnail){
		Object[] listeners = idListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == JThumbnailListener.class) {
				((JThumbnailListener<T>) listeners[i + 1]).thumbnailUnfocused(thumbnail);
			}
		}
	}

	/**
	 * Fire a thumbnail selected event.
	 */
	protected void fireThumbnailSelected(){
		fireThumbnailSelected(this);
	}

	/**
	 * Fire a thumbnail selected event.
	 * @param thumbnail the thumbnail that has raised the event.
	 */
	@SuppressWarnings("unchecked")
	protected void fireThumbnailSelected(JThumbnail<T> thumbnail){
		Object[] listeners = idListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == JThumbnailListener.class) {
				((JThumbnailListener<T>) listeners[i + 1]).thumbnailSelected(thumbnail);
			}
		}
	}

	/**
	 * Fire a thumbnail activated event.
	 * @param thumbnail the thumbnail that has raised the event.
	 */
	@SuppressWarnings("unchecked")
	protected void fireThumbnailActivated(JThumbnail<T> thumbnail){
		Object[] listeners = idListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == JThumbnailListener.class) {
				((JThumbnailListener<T>) listeners[i + 1]).thumbnailActivated(thumbnail);
			}
		}
	}

	/**
	 * Fire a label activated event.
	 */
	@SuppressWarnings("unchecked")
	protected void labelActivated(){
		Object[] listeners = idListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == JThumbnailListener.class) {
				((JThumbnailListener<T>) listeners[i + 1]).thumbnailActivated(this);
			}
		}
	}

	/**
	 * Add an Active Label Listener to this Active Label
	 * @param l ActiveLabelListener Listener added to the Active Label
	 */
	public void addThumbnailListener(JThumbnailListener<T> l) {
		idListenerList.add(JThumbnailListener.class, l);
	}

	/**
	 * Remove an Active Label Listener from this Active Label
	 * @param l ActiveLabelListener Active Listener to remove
	 */
	public void removeThumbnailListener(JThumbnailListener<T> l) {
		idListenerList.remove(JThumbnailListener.class, l);
	}

	/**
	 * mouseClicked
	 *
	 * @param e MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2){
			labelActivated();
		}
		else if (e.getClickCount() == 1){
			fireThumbnailSelected();
		}
	}

	/**
	 * mouseEntered
	 *
	 * @param e MouseEvent
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * mouseExited
	 *
	 * @param e MouseEvent
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * mousePressed
	 *
	 * @param e MouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * mouseReleased
	 *
	 * @param e MouseEvent
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}


	/**
	 * Process a mouse event that occurred on the label.
	 * @param e the underlying mouse event
	 */
	protected void processLabelMouseEvent(MouseEvent e){
		switch (e.getID()){
		case MouseEvent.MOUSE_ENTERED: 
			if (!this.focused){
				this.focused = true;
				refresh();
				fireThumbnailFocused(this);
			}

			break;
		case MouseEvent.MOUSE_EXITED:
			// survol du label avec la souris - sortie du label
			this.focused = false;
			fireThumbnailUnfocused(this);
			refresh();
			break;

		case MouseEvent.MOUSE_CLICKED:
			if (e.getClickCount() >= 2){
				labelActivated();
			}
			else if (e.getClickCount() == 1){
				fireThumbnailSelected();
			}
			break;
		} 
	}

	/**
	 * Refresh the Graphical User Interface (GUI) components.
	 */
	protected void refreshGUI(){

		Dimension size = getSize();

		if (isNameVisible()){

			Dimension nameLBSize = nameLB.getSize();

			if (BorderLayout.SOUTH.equals(nameOrientation) || (BorderLayout.NORTH.equals(nameOrientation))){
				thumbnailPN.setSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin - nameLBSize.height));
				thumbnailPN.setPreferredSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin - nameLBSize.height));
				thumbnailPN.setMaximumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin - nameLBSize.height));
				thumbnailPN.setMinimumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin - nameLBSize.height));
			} else if (BorderLayout.WEST.equals(nameOrientation) || (BorderLayout.EAST.equals(nameOrientation))){
				thumbnailPN.setSize(new Dimension(size.width - 2 * thumbnailMargin  - nameLBSize.width, size.height - 2 * thumbnailMargin));
				thumbnailPN.setPreferredSize(new Dimension(size.width - 2 * thumbnailMargin - nameLBSize.width, size.height - 2 * thumbnailMargin));
				thumbnailPN.setMaximumSize(new Dimension(size.width - 2 * thumbnailMargin - nameLBSize.width, size.height - 2 * thumbnailMargin));
				thumbnailPN.setMinimumSize(new Dimension(size.width - 2 * thumbnailMargin - nameLBSize.width, size.height - 2 * thumbnailMargin));
			} else {
				thumbnailPN.setSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
				thumbnailPN.setPreferredSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
				thumbnailPN.setMaximumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
				thumbnailPN.setMinimumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
			}
		} else {
			thumbnailPN.setSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
			thumbnailPN.setPreferredSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
			thumbnailPN.setMaximumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
			thumbnailPN.setMinimumSize(new Dimension(size.width - 2 * thumbnailMargin, size.height - 2 * thumbnailMargin));
		}
	}

	/**
	 * Processin embed component mouse event.
	 * @param e The mouse event fired by embed component
	 */
	protected void processComponentMouseEvent(MouseEvent e){
		switch (e.getID()){
		case MouseEvent.MOUSE_ENTERED:

			if (!this.focused){
				this.focused = true;
				refresh();

				// Propagation de l'evènement souris
				e.setSource(this);
				processMouseEvent(e);

				fireThumbnailFocused(this);
			}

			break;
		case MouseEvent.MOUSE_EXITED:
			// survol du label avec la souris - sortie du label
			this.focused = false;

			// Propagation de l'evènement souris
			e.setSource(this);
			processMouseEvent(e);

			fireThumbnailUnfocused(this);
			refresh();
			break;

		case MouseEvent.MOUSE_CLICKED:
			if (e.getClickCount() >= 2){
				labelActivated();
			}
			else if (e.getClickCount() == 1){
				fireThumbnailSelected();
			}

			e.setSource(this);
			processMouseEvent(e);
			break;
		}
	}

	@Override
	public void validate(){
		super.validate(); 
	}
}