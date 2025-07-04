package org.jorigin.property;

import java.util.List;

import org.jorigin.Common;

/**
 * An interface that describe an object that can handle user properties.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.9
 */
public interface HandleUserProperties {

	/**
	 * Get the user property mapped with the given <code>name</code>. If no property is mapped, <code>null</code> is returned.
	 * @param propertyName the name of the property.
	 * @return the user property mapped with the given <code>name</code>. If no property is mapped, <code>null</code> is returned.
	 * @see #setUserProperty(String, Object)
	 */
	public Object getUserProperty(String propertyName);

	/**
	 * Set the user property mapped with the given <code>name</code> to the given <code>value</code>. 
	 * If a property is already mapped with this name, the old value is returned.
	 * @param name the name of the property.
	 * @param value the value of the property.
	 * @return the old value of the property that was mapped.
	 * @see #getUserProperty(String)
	 */
	public Object setUserProperty(String name, Object value);

	/**
	 * Get the list of the names of all properties attached to this object.
	 * @return the list of the names of all properties attached to this object.
	 */
	public List<String> getUserPropertyNames();

	/**
	 * Clear the user properties associated to this object.
	 */
	public void clearUserProperties();
}
