package org.jorigin.state;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be displayed.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.11
 */
public interface HandleDisplay {
	
	/**
	 * Get if the object is currently displaying.
	 * @return <code>true</code> if the object is currently displaying and <code>false</code> otherwise.
	 * @see #setStateDisplaying(boolean)
	 */
    public boolean isStateDisplaying();
    
    /**
     * Set if the object has to be displaying.
     * This method has to modify the display state only if its {@link #isStateDisplayable() displayability} is set to <code>true</code>.
     * @param displaying <code>true</code> if the object is currently displaying and <code>false</code> otherwise.
     * @see #isStateDisplaying()
     */
    public void setStateDisplaying(boolean displaying);
    
	/**
	 * Get if the object display state can be modified. 
	 * @return <code>true</code> if the object display state can be modified and <code>false</code> otherwise.
	 * @see #isStateDisplaying()
	 * @see #setStateDisplayable(boolean)
	 */
	public boolean isStateDisplayable();
	
	/**
	 * Set if the object display state can be modified. 
	 * @param displayable <code>true</code> if the object display state can be modified and <code>false</code> otherwise.
	 * @see #setStateDisplaying(boolean)
	 * @see #isStateDisplayable()
	 */
	public void setStateDisplayable(boolean displayable);
}
