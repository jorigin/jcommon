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

import java.util.ArrayList;

import org.jorigin.Common;


/**
 * This interface specify the methods of a plugin manager. A plugin manager provide
 * plugin integration within an application.
 *
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public interface IPluginManager{

	/** 
	 * Register a plugin to the plugin manager.
	 * @param plugin the plugin to register.
	 */
	void pluginRegister(IPlugin plugin);

	/**
	 * Unregister the given plugin from the plugin manager
	 * @param plugin the plugin to unregister
	 */
	void pluginUnregister(IPlugin plugin);

	/**
	 * Initialize the plugin given in parameter. Most of the time, this function
	 * call the function {@link org.jorigin.plugin.IPlugin#pluginInit(IPlugger plugger) IPlugin.pluginInit(IPlugger plugger)}.
	 * @param plugin the plugin to initialize.
	 * @see org.jorigin.plugin.IPlugin
	 */
	void pluginInit(IPlugin plugin);


	/**
	 * Start the plugin given in parameter. Most of the time, this function
	 * call the function {@link org.jorigin.plugin.IPlugin#pluginStart() IPlugin.pluginStart()}.
	 * @param plugin the plugin to start.
	 * @see org.jorigin.plugin.IPlugin
	 */
	void pluginStart(IPlugin plugin);


	/**
	 * Stop the plugin given in parameter. Most of the time, this function
	 * call the function {@link org.jorigin.plugin.IPlugin#pluginStop() IPlugin.pluginStop()}.
	 * @param plugin the plugin to stop.
	 * @see org.jorigin.plugin.IPlugin
	 */
	void pluginStop(IPlugin plugin);


	/**
	 * Restart the plugin given in parameter. Most of the time, this function
	 * call the method {@link org.jorigin.plugin.IPlugin#pluginStop() IPlugin.pluginStop()}
	 * then the method {@link org.jorigin.plugin.IPlugin#pluginStart() IPlugin.pluginStart()}.
	 * @param plugin the plugin to restart.
	 * @see org.jorigin.plugin.IPlugin
	 */
	void pluginRestart(IPlugin plugin);

	/**
	 * Init all plugins registered to this manager.
	 */
	void pluginAllInit();

	/**
	 * Start all plugins registered to this manager.
	 */
	void pluginAllStart();

	/**
	 * Stop all plugins registered to theis manager.
	 */
	void pluginAllStop();

	/**
	 * Restart all plugins registered to this manager.
	 */
	void pluginAllRestart();


	/**
	 * Get the list of the name of the excluded plugins.
	 * @return the list of the name of the excluded plugins.
	 * @see #addPluginExclude(String)
	 * @see #removePluginExclude(String)
	 * @see #addPluginInclude(String)
	 * @see #removePluginInclude(String)
	 */
	public ArrayList<String> getPluginExcludes();

	/**
	 * Get the list of the name of the included plugins.
	 * @return the list of the name of the excluded plugins.
	 * @see #addPluginExclude(String)
	 * @see #removePluginExclude(String)
	 * @see #addPluginInclude(String)
	 * @see #removePluginInclude(String)
	 */
	public ArrayList<String> getPluginIncludes();

	/**
	 * Add a plugin name to the exclude list.
	 * @param pluginName the name of the plugin to exclude.
	 * @return <code>true</code> if the plugin has been successfully added to the exclude list, false otherwise. Please note that
	 * if the plugin name is already in the exclude list it will not be added twice.
	 * @see #removePluginExclude(String)
	 * @see #addPluginInclude(String)
	 * @see #removePluginInclude(String)
	 */
	public boolean addPluginExclude(String pluginName);

	/**
	 * Remove a plugin name from the exclude list.
	 * @param pluginName the name of the plugin to remove from exclusion.
	 * @return <code>true</code> if the plugin has been successfully removed from the exclude list, false otherwise.
	 * @see #addPluginExclude(String)
	 * @see #addPluginInclude(String)
	 * @see #removePluginInclude(String)
	 */
	public boolean removePluginExclude(String pluginName);

	/**
	 * Add a plugin name to the include list.
	 * @param pluginName the name of the plugin to include.
	 * @return <code>true</code> if the plugin has been succesfully added to the include list, false otherwise. Please note that
	 * if the plugin name is already in the include list it will not be added twice.
	 * @see #addPluginExclude(String)
	 * @see #removePluginExclude(String)
	 * @see #removePluginInclude(String)
	 */
	public boolean addPluginInclude(String pluginName);

	/**
	 * Remove a plugin name from the include list.
	 * @param pluginName the name of the plugin to remove from inclusion.
	 * @return <code>true</code> if the plugin has been successfully removed from the include list, false otherwise.
	 * @see #addPluginExclude(String)
	 * @see #removePluginExclude(String)
	 * @see #addPluginInclude(String)
	 */
	public boolean removePluginInclude(String pluginName);

	/**
	 * Add a plugin manager listener to the plugin manager. This method return <code>true</code> is the listener 
	 * has been successfully registered to the plugin and <code>false</code> otherwise.
	 * @param listener the listener to register to the plugin manager
	 * @return <code>true</code> is the listener 
	 * has been successfully registered to the plugin and <code>false</code> otherwise.
	 * @see org.jorigin.plugin.PluginManagerListener
	 */
	public boolean addPluginManagerListener(PluginManagerListener listener);

	/**
	 * Remove a plugin manager listener from the plugin manager. This method return <code>true</code> is the listener 
	 * has been successfully removed from the plugin and <code>false</code> otherwise.
	 * @param listener the listener to remove from the plugin manager
	 * @return <code>true</code> is the listener 
	 * has been successfully removed from the plugin and <code>false</code> otherwise.
	 * @see org.jorigin.plugin.PluginManagerListener
	 */
	public boolean removePluginManagerListener(PluginManagerListener listener);
}
