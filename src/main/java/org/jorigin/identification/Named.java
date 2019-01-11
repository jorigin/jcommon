package org.jorigin.identification;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be named.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.9
 */
public interface Named {

	/**
	 * Get the name of the object.
	 * @return the name of the object.
	 * @see #setName(String)
	 */
	public String getName();
	
	/**
	 * Set the name of the object.
	 * @param name the name of the object.
	 * @see #getName()
	 */
	public void setName(String name);
}
