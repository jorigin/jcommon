package org.jorigin.gui;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be displayed.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.9
 */
public interface Displayable {
	
	/**
	 * Get if the object is currently displaying.
	 * @return <code>true</code> if the object is currently displaying abd <code>false</code> otherwise.
	 * @see #setDisplaying(boolean)
	 */
    public boolean isDisplaying();
    
    /**
     * Set if the object has to be displaying.
     * @param displaying <code>true</code> if the object is currently displaying abd <code>false</code> otherwise.
     * @see #isDisplaying()
     */
    public void setDisplaying(boolean displaying);
}
