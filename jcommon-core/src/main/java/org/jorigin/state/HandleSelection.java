package org.jorigin.state;

import org.jorigin.Common;

/**
 * An interface that describe an object that can handle selection state.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.11
 */
public interface HandleSelection {

	/**
	 * Get if the object is selected.
	 * @return <code>true</code> if the object is selected and <code>false</code> otherwise.
	 * @see #isStateSelectable()
	 * @see #setStateSelected(boolean)
	 */
	public boolean isStateSelected();
	
	/**
	 * Set if the object is selected. 
	 * This method has to modify the selection state only if its {@link #isStateSelectable() selectability} is set to <code>true</code>.
	 * @param selected <code>true</code> if the object can be selected and <code>false</code> otherwise.
	 * @see #setStateSelectable(boolean)
	 * @see #isStateSelected()
	 */
	public void setStateSelected(boolean selected);
	
	/**
	 * Get if the object can be selected. 
	 * @return <code>true</code> if the object selection state can be modified and <code>false</code> otherwise.
	 * @see #isStateSelected()
	 * @see #setStateSelectable(boolean)
	 */
	public boolean isStateSelectable();
	
	/**
	 * Set if the object can be selected. 
	 * @param selectable <code>true</code> if the object selection state can be modified and <code>false</code> otherwise.
	 * @see #setStateSelected(boolean)
	 * @see #isStateSelectable()
	 */
	public void setStateSelectable(boolean selectable);
}
