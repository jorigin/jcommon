package org.jorigin.identification;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be named.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
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
