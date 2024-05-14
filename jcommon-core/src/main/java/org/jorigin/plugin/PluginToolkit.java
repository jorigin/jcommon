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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;

import javax.swing.event.EventListenerList;

import org.jorigin.Common;
import org.jorigin.lang.PathUtil;


/**
 * This class is a toolkit used for helping plugin management. Using this class, user can scan and load a list of plugins
 * related to a given package ({@link #setPluginPackage(String)}) from given locations on the system ({@link #addPluginDir(String)}, 
 * {@link #addPluginDir(File)}). The plugin loading is launched via the {@link #loadPlugins()} method.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public class PluginToolkit {
  
  private ArrayList<IPlugin> plugins         = null;

  private ArrayList<File> pluginDirs         = null;
    
  private String corePackage                 = null;
  
  private int taskCurrentTime                = 0;

  /**
   * The attached listeners.
   */
  protected EventListenerList idListenerList = new EventListenerList();

  
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC CONSTRUCTEUR                                             CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  /**
   * Constuct a new default plugin toolkit.
   */
  public PluginToolkit(){
    this.plugins        = null;
    
    this.pluginDirs     = new ArrayList<File>();
    
  }
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC FIN CONSTRUCTEUR                                         CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA ACCESSEUR                                                AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  /**
   * Add a new path in the plugin search path.
   * @param pluginDir the path to insert in the plugin search list.
   * @return boolean true if the plugin search dir is added, false otherwise.
   */
  public boolean addPluginDir(String pluginDir){
    if (pluginDir != null){
      File f = new File(PathUtil.URIToPath(pluginDir));
      if (!this.pluginDirs.contains(f)){
        return this.pluginDirs.add(f);
      }
    }
    return false;
  }
  
  /**
   * Add a new path in the plugin search path.
   * @param pluginDir the path to insert in the plugin search list.
   * @return boolean true if the plugin search dir is added, false otherwise.
   */
  public boolean addPluginDir(File pluginDir){
    if (pluginDir != null){
      if (!this.pluginDirs.contains(pluginDir)){
        return this.pluginDirs.add(pluginDir);
      }
    }
    
    return false;
  }  
  
  /**
   * Remove the given path from the plugin search path list.
   * @param pluginDir the plugin directory to remove.
   * @return true if the directory is succesfully removed, false otherwise.
   */
  public boolean removePluginDir(String pluginDir){
    if (pluginDir !=  null){
      File f = new File(PathUtil.URIToPath(pluginDir));
      return removePluginDir(f);
    }
    return false;
  }

  /**
   * Remove the given path from the plugin search path list.
   * @param pluginDir the plugin directory to remove.
   * @return true if the directory is succesfully removed, false otherwise.
   */
  public boolean removePluginDir(File pluginDir){
    return this.pluginDirs.remove(pluginDir);  
  }
  
  /**
   * Set the core package of the plugins.
   * @param pluginPackage the core package of the plugins
   */
  public void  setPluginPackage(String pluginPackage){
    this.corePackage = pluginPackage;
  }

  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA FIN ACCESSEUR                                            AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  
  //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  //EE EVENEMENT                                                EE
  //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

  /**
   * Add a Plugin Toolkit Listener to this plugin toolkit
   * @param l the listener to add to the toolkit
   * @see #removePluginToolkitListener(PluginToolkitListener)
   */
  public void addPluginToolkitListener(PluginToolkitListener l) {
    this.idListenerList.add(PluginToolkitListener.class, l);
  }

  /**
   * Remove a Plugin Toolkit Listener from this plugin toolkit
   * @param l the listener to remove
   * @see #addPluginToolkitListener(PluginToolkitListener)
   */
  public void removePluginToolkitListener(PluginToolkitListener l) {
    this.idListenerList.remove(PluginToolkitListener.class, l);
  }
  
  /**
   * Fire a new plugin toolkit event to all registered listeners
   * @param e the event to fire.
   * @see #addPluginToolkitListener(PluginToolkitListener)
   * @see #removePluginToolkitListener(PluginToolkitListener)
   */
  protected void fireEvent(PluginToolkitEvent e) {
    Object[] listeners = this.idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == PluginToolkitListener.class) {
            ((PluginToolkitListener) listeners[i + 1]).eventDispatched(e);
        }
    }
  }
  //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  //EE FIN EVENEMENT                                            EE
  //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
  
  /**
   * Load all plugins available in the locations given to the toolkit. This method scan the available locations
   * and looks for plugins with given package name. When available plugins are detected, the method instanciate them
   * and store the instanciated plugins in the returned list. 
   * @return ArrayList&lt;{@link org.jorigin.plugin.IPlugin IPlugin}&gt; the plugins availables and instanciated.
   * @see #addPluginDir(String)
   * @see #addPluginDir(File)
   * @see #removePluginDir(String)
   * @see #removePluginDir(File)
   */
  public  ArrayList<IPlugin> loadPlugins(){

    IPlugin ap                         = null;

    this.plugins                            = new ArrayList<IPlugin>();

    String pluginClass                 = null;
    
    Iterator<File> fileIter            = null;
    File pluginDir                     = null;
    
    ArrayList<String> pluginClassNames = null;
    String[] corePluginFiles           = null;
    
    String archive                     = null;
    String path                        = null;
      
    File jarFile                       = null;
    JarInputStream jis                 = null;
    
    JarEntry jarEntry                  = null;
    
    // Parcours des répertoires de plugin pour trouver les plugins.
    fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_DISCOVERING_START, "Plugin discovering", this.pluginDirs.size()));
    
    fileIter = this.pluginDirs.iterator();
    while(fileIter.hasNext()){
    
      pluginDir = fileIter.next();

      // Verifie si les classes sont contenues dans un jar
      if (pluginDir.getPath().contains("!")){
        archive = pluginDir.getPath().split("!")[0];
        path    = pluginDir.getPath().split("!")[1];

        archive = PathUtil.URIToPath(archive);
        path    = path.replace("\\", "/");

        if (path.startsWith("/")){
          path = path.substring(1);
        }
      } 

      // Chargement depuis une archive
      if (archive != null){

        if (archive.endsWith(".jar")){
          Common.logger.log(Level.INFO, "[PluginToolKit][loadPlugins()] Plugin load from jar file: "+archive);
          Common.logger.log(Level.INFO, "[PluginToolKit][loadPlugins()]  - directory             : "+path);
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_DISCOVERING_ARCHIVE, archive));
          try {
            
            switch (PathUtil.getProtocol(archive)) {
              case PathUtil.SYSTEM:
              case PathUtil.URL_FILE:
        	jarFile = new File(PathUtil.URIToPath(archive));
        	jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFile)));
        	break;
        	
              case PathUtil.URL_HTTP:
        	jis = new JarInputStream(new BufferedInputStream(PathUtil.pathToURL(archive).openStream()));
        	break;
        	
              case PathUtil.URL_FTP:
              case PathUtil.URL_SFTP:
        	break;
            }
            
            if (jis != null){
              if (pluginClassNames == null){
                pluginClassNames = new ArrayList<String>();
              }
              
              jarEntry = jis.getNextJarEntry();
              while (jarEntry != null){
                
                if ((jarEntry.getName().startsWith(path) && (jarEntry.getName().toUpperCase().endsWith("PLUGIN.CLASS")))){

                  pluginClass = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf("."));
                  pluginClass = pluginClass.replace("/", ".");

                  pluginClassNames.add(pluginClass);

                  Common.logger.log(Level.INFO, "[PluginToolKit][loadPlugins()]  - Found plugin: "+pluginClass);
                }
                
                jarEntry = jis.getNextJarEntry();
              }

              if (pluginClassNames.size() < 1){
                Common.logger.log(Level.INFO, "[PluginToolKit][loadPlugins()]  - No plugin available");
                fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_NO_DISCOVERY, archive));
              }
            } else {
              Common.logger.log(Level.SEVERE, "[PluginToolKit][loadPlugins()] Unable to open stream from archive "+archive);
            }

          } catch (IOException ex) {
            Common.logger.log(Level.SEVERE, "[PluginToolKit][loadPlugins()] Cannot open jar "+archive, ex);
          }
        }

        // Chargement directement depuis un répertoire de classes  
      } else {
        Common.logger.log(Level.INFO, "[PluginToolKit][initPlugins()] Plugin load from dir : "+pluginDir.getPath());
        fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_DISCOVERING_DIR, pluginDir.getPath()));
        
        // Recuperation des fichiers presents dans le repertoire plugin d'ametist
        try {
          corePluginFiles = pluginDir.list(new FilenameFilter(){
            public boolean accept(File dir, String name) {


              if (name.endsWith("Plugin.class")){
                if (name.equals("IPlugin.class")){
                  return false;
                }
                return true;
              } else {
                return false;
              }
            }});

          if (corePluginFiles != null){
            if (pluginClassNames == null){
              pluginClassNames = new ArrayList<String>();
            }

            for(int i = 0; i < corePluginFiles.length; i++){
              pluginClass = corePluginFiles[i].replace(File.separatorChar, '.');
              pluginClass = pluginClass.substring(0, pluginClass.length() - 6);
              pluginClass = this.corePackage + "." + pluginClass;

           
              
              if (pluginClass != null){
                pluginClassNames.add(pluginClass);
              }

              Common.logger.log(Level.INFO, "[PluginToolKit][loadPlugins()]  - Found plugin: "+pluginClass);
            } 
          } else {
            Common.logger.log(Level.SEVERE, "[PluginToolKit][loadPlugins()] Unable to list plugin dir: "+pluginDir.getPath());
          }
          
        } catch (Exception ex) {
          Common.logger.log(Level.SEVERE, "[PluginToolKit][loadPlugins()] Unable to list plugin dir: "+pluginDir.getPath(), ex);
        }
      }
    }
    
    fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_DISCOVERING_FINISHED, "Plugin discovering finished"));
    
    if ((pluginClassNames != null) && (pluginClassNames.size() > 0)){
      
      fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_START, "", pluginClassNames.size()));
      for(int i = 0; i < pluginClassNames.size(); i++){
        try {

          pluginClass = pluginClassNames.get(i);
          
          Common.logger.log(Level.INFO, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass);
          ap = (IPlugin) Class.forName(pluginClass).getDeclaredConstructor().newInstance();
          this.plugins.add(ap);
          this.taskCurrentTime += 1;
          
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_LOADED, ap, this.taskCurrentTime));
          
          Common.logger.log(Level.INFO, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [OK]");
        } catch (ClassNotFoundException ex) {
          Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
          Common.logger.log(Level.SEVERE, "PluginToolkit Cannot register plugin "+pluginClass);
          Common.logger.log(Level.SEVERE, "the plugin is not available", ex);
          this.taskCurrentTime += 1;
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
          
        } catch (IllegalAccessException ex) {
          Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
          Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
          Common.logger.log(Level.SEVERE, "the plugin cannot be accessed (illegal access)", ex);
          this.taskCurrentTime += 1;
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
          
        } catch (InstantiationException ex) {
          Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
          Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
          Common.logger.log(Level.SEVERE, "the plugin cannot be instanciated", ex);
          
          this.taskCurrentTime += 1;
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
        } catch (IllegalArgumentException ex) {
          Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
          Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
          Common.logger.log(Level.SEVERE, "the plugin cannot be instanciated", ex);
          this.taskCurrentTime += 1;
          fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
		} catch (InvocationTargetException ex) {
		  Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
	      Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
	      Common.logger.log(Level.SEVERE, "the plugin cannot be instanciated", ex);
	      this.taskCurrentTime += 1;
	      fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
		} catch (NoSuchMethodException ex) {
		  Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
	      Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
	      Common.logger.log(Level.SEVERE, "the plugin cannot be instanciated", ex);
	      this.taskCurrentTime += 1;
	      fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
		} catch (SecurityException ex) {
		  Common.logger.log(Level.SEVERE, "[PluginToolkit][loadPlugins()]  - Load "+pluginClass+" [FAIL]");
	      Common.logger.log(Level.SEVERE, "Cannot register plugin "+pluginClass);
	      Common.logger.log(Level.SEVERE, "the plugin cannot be instanciated", ex);
	      this.taskCurrentTime += 1;
	      fireEvent(new PluginToolkitEvent(this, PluginToolkitEvent.PLUGIN_LOADING_ERROR, pluginDir.getPath(), this.taskCurrentTime));
		}   
      }
    } else{
      this.plugins = null;
    }
    
    return this.plugins;
  }

}
