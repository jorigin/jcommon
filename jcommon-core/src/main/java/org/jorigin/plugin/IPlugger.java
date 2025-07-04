/*
  This file is part of JOrigin Common Library.

    JOrigin Common is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JOrigin Common is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JOrigin Common.  If not, see <http://www.gnu.org/licenses/>.

 */
package org.jorigin.plugin;

import org.jorigin.Common;

/**
 * This class represents a plugger. A plugger is the interface between the kernel of the application and the plugins.
 * The main method of the plugger is {@link #getExensionPoint(String) getExensionPoint(String)} which return an extension point that can be
 * accessed by the plugins. The extension keys can be obtained by the method {@link #getExtensionPointKeys() getExtensionPointKeys()}. <br><br>
 * A class implementing the interface {@link org.jorigin.plugin.IPlugin IPlugin} can also implements the <code>IPlugger</code> interface. 
 * So a plugin can also be a plugger and can be extended by others plugins.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public interface IPlugger {

	/**
	 * Return the extension point referenced by the given key. If the key does not patch a plugin, <code>null</code> is returned.
	 * @param key the key of the extension point.
	 * @return the extension point.
	 */  
	public Object getExensionPoint(String key);


	/**
	 * Return all the keys identifying extension points.
	 * @return an array of String representing the key referencing extension points.
	 */
	public String[] getExtensionPointKeys();

	/**
	 * Add a new extension point to the plugger. The extension point is assigned to the
	 * key given. If the key is already affected to another extension point, the new extension 
	 * point is not added.
	 * @param key the key of the extension point to add
	 * @param extensionPoint the extension point to add
	 * @return true if the extension point is added, false otherwise
	 */
	public boolean addExtensionPoint(String key, Object extensionPoint);
}
