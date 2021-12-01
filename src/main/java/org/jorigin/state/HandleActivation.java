package org.jorigin.state;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be activated.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.11
 */
public interface HandleActivation {
	/**
	 * Get if the object is activated.
	 * @return <code>true</code> if the object is activated and <code>false</code> otherwise.
	 * @see #isStateActivable()
	 * @see #setStateActivated(boolean)
	 */
	public boolean isStateActivated();
	
	/**
	 * Set if the object is activated. 
	 * This method has to modify the activation state only if its {@link #isStateActivable() activability} is set to <code>true</code>
	 * @param activated <code>true</code> if the object can be activated and <code>false</code> otherwise.
	 * @see #setStateActivable(boolean)
	 * @see #isStateActivated()
	 */
	public void setStateActivated(boolean activated);
	
	/**
	 * Get if the object can be activated. 
	 * @return <code>true</code> if the object activation state can be modified and <code>false</code> otherwise.
	 * @see #isStateActivated()
	 * @see #setStateActivable(boolean)
	 */
	public boolean isStateActivable();
	
	/**
	 * Set if the object can be activated. 
	 * @param activable <code>true</code> if the object activation state can be modified and <code>false</code> otherwise.
	 * @see #setStateActivated(boolean)
	 * @see #isStateActivable()
	 */
	public void setStateActivable(boolean activable);
}
