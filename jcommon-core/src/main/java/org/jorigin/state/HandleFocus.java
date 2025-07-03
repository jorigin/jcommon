package org.jorigin.state;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be focused.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.14
 */
public interface HandleFocus {
	
	/**
	 * Get if the object is currently focused.
	 * @return <code>true</code> if the object is currently focused and <code>false</code> otherwise.
	 * @see #setStateFocused(boolean)
	 */
    public boolean isStateFocused();
    
    /**
     * Set if the object has to be focused.
     * This method has to modify the focus state only if its {@link #isStateFocusable() focusability} is set to <code>true</code>.
     * @param focused <code>true</code> if the object is currently focused and <code>false</code> otherwise.
     * @see #isStateFocused()
     */
    public void setStateFocused(boolean focused);
    
	/**
	 * Get if the object focus state can be modified. 
	 * @return <code>true</code> if the object focus state can be modified and <code>false</code> otherwise.
	 * @see #isStateFocused()
	 * @see #setStateFocusable(boolean)
	 */
	public boolean isStateFocusable();
	
	/**
	 * Set if the object focus state can be modified. 
	 * @param focusable <code>true</code> if the object focus state can be modified and <code>false</code> otherwise.
	 * @see #setStateFocused(boolean)
	 * @see #isStateFocused()
	 */
	public void setStateFocusable(boolean focusable);
}