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
 * This interface represent an application plugin. An aplication plugin enable to extends
 * an application with various functions. The plugin interface specifies the method necessary to the plugin manager
 * for managing the plugin.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public interface IPlugin {

  /**
   * Final identifier for the plugin state corresponding to initialized.
   * @see #getPluginState()
   */
  public static final int PLUGIN_INITIALIZED = 1;
  
  /**
   * Identifier for the plugin state corresponding to started.
   * @see #getPluginState()
   */
  public static final int PLUGIN_STARTED     = 2;
  
  /**
   * Init the plugin and give the plugin a reference to the plugger where it is registered.
   * @param plugger plugger the reference to the plugger
   * @return true if the plugin is correctly initialized, false otherwise.
   * @throws Exception if an error occurs.
   */
  boolean pluginInit(IPlugger plugger) throws Exception;


  /**
   * Method called when the plugger starts the plugin
   * @return <code>true</code> if the plugin is started, <code>false</code> otherwise.
   */
  boolean pluginStart();


  /**
   * Method called when the plugger stop the plugin
   * @return <code>true</code> if the plugin is stopped, <code>false</code> otherwise.
   */
  boolean pluginStop();
  
  /**
   * Get the name of the plugin. The name must be unique. The canonical name of the plugin class
   * can be used.
   * @return the name of the plugin.
   */
  String getName();
  
  /**
   * Return the dependencies of the plugin. Dependencies are represented by the name of the required plugins 
   * to make this plugin to work.
   * @return An array of plugin names.
   */
  String[] getDependencies();

  /**
   * Return the state of the plugin. State can be composed by: {@link IPlugin#PLUGIN_INITIALIZED PLUGIN_INITIALIZED}, 
   * {@link IPlugin#PLUGIN_STARTED PLUGIN_STARTED}.
   * @return the plugin state.
   */
  int getPluginState();
  
  /**
   * Return <code>true</code> if the plugin is initialized.
   * @return <code>true</code> if the plugin is initialized, <code>false</code> otherwise.
   */
  boolean isPluginInitialized();
  
  /**
   * Return <code>true</code> if the plugin is started, <code>false</code> otherwise.
   * @return <code>true</code> if the plugin is started, <code>false</code> otherwise.
   */
  boolean isPluginStarted();
}
