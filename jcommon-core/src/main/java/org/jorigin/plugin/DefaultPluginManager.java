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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;

import org.jorigin.Common;

/**
 * This class is a default implementation of the {@link org.jorigin.plugin.IPluginManager IPluginManager} interface. 
 * The plugins are stored in an underlying {@link java.util.ArrayList ArrayList}.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public class DefaultPluginManager implements org.jorigin.plugin.IPluginManager{

	/**
	 * The list of available plugins
	 */
	private ArrayList<IPlugin> plugins  = null;

	/**
	 * The names of the excluded plugins. These plugins will not be activated.
	 */
	private ArrayList<String> excludes = new ArrayList<String>();

	/**
	 * The name of the included plugins. These plugins will be activated.
	 */
	private ArrayList<String> includes = new ArrayList<String>();

	/**
	 * The pluger that the manager handle
	 */
	private IPlugger plugger           = null;

	/**
	 * The list of all registered plugin manager listeners
	 */
	private ArrayList<PluginManagerListener> listeners = null;

	/** the plugins count */
	private int pluginCount     = 0;

	/** The number of plugins that have been processed. */
	private int pluginProcessed = 0;

	//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
	//II IMPLEMENTATION                                      II
	//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
	@Override
	public boolean addPluginExclude(String pluginName) {
		if (!this.excludes.contains(pluginName)){
			return this.excludes.add(pluginName);
		} else {
			return false;
		}
	}

	@Override
	public boolean addPluginInclude(String pluginName) {
		if (!this.includes.contains(pluginName)){
			return this.includes.add(pluginName);
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<String> getPluginExcludes() {
		return this.excludes;
	}

	@Override
	public ArrayList<String> getPluginIncludes() {
		return this.includes;
	}

	@Override
	public void pluginAllInit() {
		Iterator<IPlugin> iter = null;
		IPlugin plugin         = null;

		java.util.regex.Pattern pattern;
		java.util.regex.Matcher matcher;

		boolean isExcluded = false;
		boolean isIncluded = false;

		if ( this.plugins== null){
			return;
		}

		this.pluginCount     = this.plugins.size();
		this.pluginProcessed = 0;
		initStarted(this.pluginCount);

		// Classement des plugins en fonction de leur dependances
		// Les plugins dépendance d'autre plugins sont classes en premier
		Collections.sort(this.plugins, new Comparator<IPlugin>(){

			public int compare(IPlugin o1, IPlugin o2) {
				String[] p1dep = o1.getDependencies();
				String[] p2dep = o2.getDependencies();

				boolean p1dependOfP2 = false;
				boolean p2dependOfP1 = false;

				int i   = 0;
				int ret = 0;

				// Verification de la presence de dependences pour les plugins
				if ((p1dep == null) && (p2dep == null)){
					return 0;
				} else if (p1dep == null){
					return -1;
				} else if (p2dep == null){
					return 1;
				}

				// p1 est il dependant de p2
				i = 0;
				while((i < p1dep.length) && (p1dependOfP2 == false)){
					if (p1dep[i].equals(o2.getName())){
						p1dependOfP2 = true;  
					}
					i++;
				}

				// p2 est il dependant de p1
				i = 0;
				while((i < p2dep.length) && (p2dependOfP1 == false)){
					if (p2dep[i].equals(o1.getName())){
						p2dependOfP1 = true;  
					}
					i++;
				}

				// Les deux plugins sont dependant l'un de l'autre
				// Ce cas est une erreur mais par defaut on considere les plugins comme
				// etant de même priorité!!
				if (p1dependOfP2 && p2dependOfP1){
					ret = 0;

					// Le plugin 1 depend du plugin 2, 2 est alors plus grand que 1
				} else if (p1dependOfP2){
					ret = 1;

					// Le plugin 2 depend du plugin 1, 1 est alors plus grand que 2
				} else if (p2dependOfP1){
					ret = -1;

					// Les deux plugins ne sont pas dependant.
				} else {
					ret = 0;
				}

				p1dep = null;
				p2dep = null;

				return ret;
			}});


		// Initialisation des plugins en des moins dépendant aux plus dépendants
		Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()] Init plugins");
		iter = this.plugins.iterator();
		while(iter.hasNext()){
			plugin = iter.next();

			int j = 0;
			while((j < this.excludes.size()) && (!isExcluded)){    

				pattern = java.util.regex.Pattern.compile(this.excludes.get(j));
				matcher = pattern.matcher(plugin.getName());

				if(matcher.matches()){
					Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - NOT Init "+plugin.getName()+" (explicitely excluded by "+this.excludes.get(j)+")");
					isExcluded = true;
				}

				j++;
			}

			if (!isExcluded){
				j = 0;
				while((j < this.includes.size()) && (!isIncluded)){    

					pattern = java.util.regex.Pattern.compile(this.includes.get(j));
					matcher = pattern.matcher(plugin.getName());

					if(matcher.matches()){
						isIncluded = true;
						try {
							if (plugin.pluginInit(this.plugger)){
								Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" (included) [OK]");
							} else {
								Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" (included) [FAIL]");
							}
						} catch (Exception ex) {
							Common.logger.log(Level.SEVERE, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" (included) [FAIL]", ex);
						}
					}
				}
			}

			if ((!isExcluded)&&(!isIncluded)){
				Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" Plugin is not included / excluded, loading by default");
				try {
					if (plugin.pluginInit(this.plugger)){
						Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" Plugin is not included / excluded, loading by default [OK]");
					} else {
						Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" Plugin is not included / excluded, loading by default [FAIL]");
					}
				} catch (Exception ex) {
					Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()]  - Init "+plugin.getName()+" Plugin is not included / excluded, loading by default [FAIL]", ex);
				}
			}
		}

		initComplete(this.pluginProcessed);
		this.pluginProcessed = 0;
		this.pluginCount     = 0;
	}

	@Override
	public void pluginAllRestart() {
		pluginAllStop();
		pluginAllStart();
	}

	@Override
	public void pluginAllStart() {
		Iterator<IPlugin> iter = null;
		IPlugin plugin         = null;
		int i = 0;

		if (this.plugins == null){
			return;
		}

		Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllInit()] Start plugins");


		this.pluginCount     = this.plugins.size();
		this.pluginProcessed = 0;
		startStarted(this.pluginCount);

		iter = this.plugins.iterator();
		while(iter.hasNext()){
			plugin = iter.next();

			if (plugin.isPluginInitialized()){
				if (plugin.pluginStart()){
					pluginStarted(plugin);
					this.pluginProcessed++;
					Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllStart()]  - Start "+plugin.getName()+" [OK]");
				} else {
					pluginStartError(plugin);
					Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllStart()]  - Start "+plugin.getName()+" [FAIL]");
				}  
			} else {
				pluginStartError(plugin);
				Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllStart()]  - Cannot start "+plugin.getName()+" because it is not initialized [FAIL]");
			}

			startProgress(plugin, (float)( ((float)i) / ((float)this.pluginCount)));

			i++;
		}

		startComplete(this.pluginProcessed);
		this.pluginProcessed = 0;
		this.pluginCount     = 0;

	}

	@Override
	public void pluginAllStop() {
		Iterator<IPlugin> iter = null;
		IPlugin plugin         = null;

		iter = this.plugins.iterator();
		while(iter.hasNext()){
			plugin = iter.next();

			if (plugin.pluginStop()){
				Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllStop()] Stop "+plugin.getName()+" [OK] ");
			} else {
				Common.logger.log(Level.INFO, "[DefaultPluginManager][pluginAllStop()] Stop "+plugin.getName()+" [FAIL] ");
			}
		}  
	}

	@Override
	public void pluginInit(IPlugin plugin) {
		try {
			plugin.pluginInit(this.plugger);
		} catch (Exception ex) {
			Common.logger.log(Level.INFO, plugin.getName()+" initialization error", ex);
		}
	}

	@Override
	public void pluginRegister(IPlugin plugin) {
		if (!this.plugins.contains(plugin)){
			this.plugins.add(plugin);
		}
	}

	@Override
	public void pluginRestart(IPlugin plugin) {
		plugin.pluginStop();
		plugin.pluginStart();
	}

	@Override
	public void pluginStart(IPlugin plugin) {
		plugin.pluginStart();
	}

	@Override
	public void pluginStop(IPlugin plugin) {
		if ((plugin != null)&&(this.plugins.contains(plugin))){
			plugin.pluginStop();
		}
	}

	@Override
	public void pluginUnregister(IPlugin plugin) {
		pluginStop(plugin);
		this.plugins.remove(plugin);
	}

	@Override
	public boolean removePluginExclude(String pluginName) {
		return this.excludes.remove(pluginName);
	}

	@Override
	public boolean removePluginInclude(String pluginName) {
		return this.includes.remove(pluginName);
	}

	@Override
	public boolean addPluginManagerListener(PluginManagerListener listener){
		if (this.listeners == null){
			this.listeners = new ArrayList<PluginManagerListener>();
		} 

		if (listener != null){
			return this.listeners.add(listener);
		} else {
			return false;
		}
	}

	@Override  
	public boolean removePluginManagerListener(PluginManagerListener listener){
		if (this.listeners == null){
			return false;
		} else {
			return this.listeners.remove(listener);
		}
	}
	//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
	//II IMPLEMENTATION                                      II
	//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

	//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
	//EE EVENEMENTS                                          EE
	//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

	/**
	 * Method called when a plugin initialization has started.
	 * @param pluginCount the number of plugin to initialize
	 */
	private void initStarted(int pluginCount){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().initStarted(pluginCount);
			}
		}
	}

	/**
	 * Method called when a plugin initialization is complete.
	 * @param pluginCount the number of plugin initialized
	 */
	private void initComplete(int pluginCount){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().initComplete(pluginCount);
			}
		}
	}

	/**
	 * Method called when a plugin start has started.
	 * @param pluginCount the number of plugin to start
	 */
	private void startStarted(int pluginCount){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().startStarted(pluginCount);
			}
		}
	}

	/**
	 * Method called when a plugin start progress.
	 * @param plugin the plugin started
	 * @param percentDone the percent of the task already done
	 */
	private void startProgress(IPlugin plugin, float percentDone){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().startProgress(plugin, percentDone);
			}
		}
	}


	/**
	 * Method called when a plugin start is complete.
	 * @param pluginCount the number of plugin started.
	 */
	private void startComplete(int pluginCount){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().startComplete(pluginCount);
			}
		}
	}

	/**
	 * Method called when a plugin is started
	 * @param plugin the started plugin
	 */
	private void pluginStarted(IPlugin plugin){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().pluginStarted(plugin);
			}
		}
	}

	/**
	 * method called when a plugin start throws an error
	 * @param plugin the involved plugin
	 */
	private void pluginStartError(IPlugin plugin){
		if (this.listeners != null){
			Iterator<PluginManagerListener> iter = this.listeners.iterator();
			while(iter.hasNext()){
				iter.next().pluginStartError(plugin);
			}
		}
	}

	//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
	//EE FIN EVENEMENTS                                      EE
	//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

	//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
	//CC CONSTRUCTEUR                                        CC
	//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
	/**
	 * Create a default plugin manager.
	 * @param plugger The {@link org.jorigin.plugin.IPlugger IPlugger} to manage.
	 */
	public DefaultPluginManager(IPlugger plugger){
		this.plugger = plugger;
		this.plugins  = new ArrayList<IPlugin>();
		this.excludes = new ArrayList<String>();
		this.includes = new ArrayList<String>();
	}
	//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
	//CC CONSTRUCTEUR                                        CC
	//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

}
