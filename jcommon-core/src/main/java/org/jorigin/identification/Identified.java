package org.jorigin.identification;

import org.jorigin.Common;

/**
 * An interface that describe an object that can be identified by an integer number.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
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
     * @param identifier the identification of the object an an integer.
     * @see #getIdentification()
     */
	public void setIdentification(int identifier);
	
}
