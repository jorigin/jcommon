package org.jorigin.identification;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be identified by an integer number.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.9
 */
public interface Identified {

	/**
	 * Get the identification of the object an an integer.
	 * @return the identification of the object an an integer.
	 * @see #setIdentification(int)
	 */
	public int getIdentification();
	
    /**
     * Set the identification of the object as an integer.
     * @param indentifier the identification of the object an an integer.
     * @see #getIdentification()
     */
	public void setIdentification(int indentifier);
	
}
