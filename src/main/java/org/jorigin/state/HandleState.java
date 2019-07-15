package org.jorigin.state;

import org.jorigin.Common;

/**
 * An interface that describe an object that can handle various states.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.11
 */
public interface HandleState {

	/**
	 * An integer that represents the activation of an item.
	 */
	public static int STATE_ACTIVE    = 1;
	
	/**
	 * An integer that represents the selection of an item.
	 */
	public static int STATE_SELECTED  = 2;
	
	/**
	 * An integer that represents the display of an item.
	 */
	public static int STATE_DISPLAYED = 4;
	
	/**
	 * Get the state of the item as an integer value. 
	 * This integer can be the combination of various states.
	 * @return the state of the item as an integer value. 
	 */
	public int getStateValue();
	
	/**
	 * Set the state of the item as an integer value. 
	 * This integer can be the combination of various states.
	 * @param state the state of the item as an integer value. 
	 */
	public void setStateValue(int state);
}
