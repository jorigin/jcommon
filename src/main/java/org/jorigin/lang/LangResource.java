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
package org.jorigin.lang;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

import org.jorigin.Common;
import org.jorigin.lang.xml.LangResourceXMLReader;


/**
 * This class enable to use lang localization in a Java application. 
 * The lang resource are stored in XML file and can be easily maintained without any link with the Java code.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class LangResource {

  /**
   * The resources stored in a hash map
   */
  private HashMap<String, String> resources = null;

  /**
   * The root path of the language resources.
   */
  private String resourcesPath = "resource"+File.separator+"lang"+File.separator;


  /**
   * The locale of the resource
   */
  private Locale locale      = null;

  /**
   * The locale available in the path.
   */
  private ArrayList<String> availableLocales = null;

  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC CONSTRUCTEUR                                                                     CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  /**
   * Default constructor of lang resource. The default resource path is set to 
   * <code>"ressource"+File.separator+"lang"+File.separator</code> and the default locale is
   * set to <code>System.getProperty("user.language")+"_"+System.getProperty("user.country")</code>.<br>
   * If the system property <code>lang.resource.root</code> is set to a valid resource directory, it is then used 
   * instead of the default directory.<br>
   * The resources are loaded by the constructor.
   * @see #LangResource(String, Locale)
   */
  public LangResource(){
    this(null, null);
  }

  /**
   * Construct a lang resource with the language file root given in parameter.
   * The locale is set by default to 
   * <code>System.getProperty("user.language")+"_"+System.getProperty("user.country")</code>.<br>
   * If the given path is not valid, the resource will be loaded for the path given by the system property <code>lang.resource.root</code>.<br>
   * If the system property is not set or invalid, the default path is used as resource base.
   * @param baseName the path of resource files directory
   * @see #LangResource(String, Locale)
   */
  public LangResource(String baseName){
    this(baseName, null);
  }

  /**
   * Construct a lang resource with given path and for a given locale. The path is
   * the root of the localized language files.<br>
   * If the given path is not valid, the resource will be loaded for the path given by the system property <code>lang.resource.root</code>.<br> 
   * If the system property is not set or invalid, the path is set to 
   * <code>"ressource"+File.separator+"lang"+File.separator</code>. The locale string
   * is made of <code>[language]_[country]</code>. By default the locale is set to
   * <code>System.getProperty("user.language")+"_"+System.getProperty("user.country")</code>.
   * @param baseName the path of the root of the locale
   * @param locale the locale.
   */
  public LangResource(String baseName, Locale locale){

    if (baseName == null){
      
      if (System.getProperty("lang.resource.root") != null){
        try {
          resourcesPath = PathUtil.URIToPath(System.getProperty("lang.resource.root"));
        } catch (Exception e) {
          Common.logger.log(Level.SEVERE, "Cannot use lang.resource.root property value: "+System.getProperty("lang.resource.root"), e);
        }
      }

    } else {
      resourcesPath = ""+baseName;
    }

    if (locale == null){
      try {
        this.locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
      } catch (RuntimeException e) {
        Common.logger.log(Level.SEVERE, "Cannot instanciate locale: "+System.getProperty("user.language")+"_"+System.getProperty("user.country"), e);
      }    
    } else {
      this.locale = locale;
    }

    this.locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));

    resources = new HashMap<String, String>();

    availableLocales = new ArrayList<String>(); 

    Common.logger.log(Level.CONFIG, "Lang resource path         : "+resourcesPath);
    Common.logger.log(Level.CONFIG, "Lang resource Given locale : "+locale);
    Common.logger.log(Level.CONFIG, "Lang resource System locale: "+this.locale);
    
    init();
  }
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC FIN CONSTRUCTEUR                                                                 CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  //II INITIALISATION                                                                   II
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  /**
   * Load all lang resource associated to the current locale.
   */
  protected void init(){

    File f       = null;

    File[] files = null;

    String str   = null;

    if (resourcesPath != null){
      f = new File(resourcesPath);
    } else {
      Common.logger.log(Level.SEVERE, "[LangResource] [init()] Lang resource directory "+PathUtil.URIToPath(resourcesPath)+" does not exist.");
      return;
    }

    if (((f == null)) || (!f.exists())){
      Common.logger.log(Level.SEVERE, "[LangResource] [init()] Lang resource directory "+PathUtil.URIToPath(resourcesPath)+" does not exist.");
      return;
    } 

    // Recherche de toutes les locales disponibles
    files = f.listFiles(new FileFilter(){

      public boolean accept(File pathname) {
        if (pathname.getPath().endsWith(".xml")){
          return true;
        }
        return false;
      }});

    // Extraction des locale de ressource à partir des nom de fichiers
    if (files != null){
      for(int i = 0; i < files.length; i++){

        str = files[i].getPath();
        str = str.substring(0, str.lastIndexOf("."));

        if (str.length() > 4){
          str = str.substring(str.length() - 5);

          if (!availableLocales.contains(str)){
            availableLocales.add(str);
          }
        }
      }
    }


    if (locale != null){

      // Liste tous les répertoires et tous les fichiers XML de l'arboressence
      // des ressources
      f = new File(resourcesPath);

      addResource(f);

      f = null;
    }


  }

  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  //II FIN INITIALISATION                                                               II
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII


  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA ACCESSEURS                                                                       AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

  /**
   * Set the locale of the lang resource. When a locale is set, all lang resource file associated
   * to the locale are loaded from the resource path.
   * @param locale the new locale.
   */
  public void setLocale(Locale locale){
    this.locale = locale;  
    init();
  }

  /**
   * Get the locale of the lang resource. When a locale is set, all lang resource file associated
   * to the locale are loaded from the resource path.
   * @return the current locale.
   */
  public Locale getLocale(){
    return locale;
  }


  /**
   * Set the root path of the resource language files. The resources are reloaded from the new path.
   * @param path the path of the resource language  files root.
   */
  public void setResourcePath(String path){
    this.resourcesPath = path;
    init();
  }

  /**
   * Set the root path of the resource language files.
   * @return the path of the resource language  files root.
   */
  public String getResourcePath(){
    return this.resourcesPath;  
  }

  /**
   * Get the map containing the lang resources.
   * @return a hash map containing the lang resource.
   */
  public HashMap<String, String> getResources(){
    return this.resources;
  }

  /**
   * Get the available locales.
   * @return a list of available locales.
   */
  public ArrayList<String> getAvailableLocales(){
    return this.availableLocales;
  }

  /**
   * Get the keys attached to the values.
   * @return the set of keys attached to the values.
   */
  public Set<String> getKeySet(){
    return this.resources.keySet();
  }

  /**
   * Get the number of resourced values.
   * @return int the number of resourced values.
   */
  public int size(){
    return this.resources.size();
  }
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA FIN ACCESSEURS                                                                   AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA


  /**
   * Add the content of a language resource file to the current lang resource. If existing keys
   * are redefined in the file, value are overwrited. If the file given in parameter is a direcory,
   * the whole directory is recursively added to the lang resource.
   * @param resourceFile a resource file or directory
   */
  private void addResource(File resourceFile){

    File[] files = null;
    HashMap<String, String> resources = null;

    if (!resourceFile.exists()){
      return;
    }

    // Si le fichier passé en parametre est une ressource, 
    if (resourceFile.isDirectory()){
      files = resourceFile.listFiles(new FileFilter(){

        public boolean accept(File pathname) {
          if (pathname.isDirectory()){
            return true;
          } else if (pathname.getPath().endsWith(locale+".xml")){
            return true;
          }
          return false;
        }});

      if (files != null){
        for(int i = 0; i < files.length; i++){
          addResource(files[i]);
        }
      }
    } else {
      try {
        Common.logger.config("Lang resource file "+resourceFile.getPath()+" loaded");
        resources = new LangResourceXMLReader().getParsedLangResource(resourceFile.getPath());

        if (resources != null){
          addResources(resources);
        }
      } catch (IOException ex) {
        Common.logger.log(Level.SEVERE, "Cannot read resource file "+resourceFile.getPath(), ex);
      }
    }

    files = null;
  }

  /**
   * Add new map of lang resources to the current resources.
   * @param resources the new lang resources map to add.
   */
  public void addResources(HashMap<String, String> resources){
    this.resources.putAll(resources);
  }

  /**
   * Add new lang resources to the current resources.
   * @param resource the new lang resource to add.
   */
  public void addResources(LangResource resource){
    resources.putAll(resource.getResources());
  }

  /**
   * Get a resource associated to a key. If the key does not correspond to any resource, the key itself
   * is returned.
   * @param key the key inditifying the resource to return
   * @return Object the resource or the key if no resource is available.
   */
  public Object getResource(String key){
    return getResource(key, (String[])null);
  }


  /**
   * Get a resource associated to a key. If the key does not correspond to any resource, the key itself
   * is returned. The parameter given is applied to the resource if it accept parameter.
   * @param key the key inditifying the resource to return
   * @param param the parameter to apply to the resource.
   * @return Object the resource or the key if no resource is available.
   */
  public Object getResource(String key, String param){  
    return getResource(key, new String[]{param});
  }

  /**
   * Get a parametrized resource associated to a key. If the key does not correspond to any resource, 
   * the key itself is returned. Parameters enable to charaterize the resource. Parameter are identified
   * in a resource by a <code>%</code> followed by the parameter number. For example, if the resource
   * is "Hello %1 and %2.", and parameters are <code>"Fox"</code> and <code>"Dana"</code>, the object
   * returned is <code>"Hello Fox and Dana."</code>
   * @param key the key identifying the resource to return
   * @param params the parameters of the resource.
   * @return Object the resource parametrized or the key if no resource is available.
   */
  public Object getResource(String key, String[] params){

    String value   = null;
    int paramCount = -1;
    
    // Si les resourcs ne sont pas disponibles, on retourne la cle.
    if (resources == null){
      Common.logger.log(Level.WARNING, ("No resource bundle available "+key));
      return key;
    }
    
    // Recuperation de la valeur associee a la cle.
    value = resources.get(key);
    
    // Si aucune valeur ne correspond a la cle, on retourne la cle.
    if (value == null){
      Common.logger.log(Level.WARNING, ("No resource value available for key "+key));
      return key;
    } 
    
    // Si aucun parametre n'est defini
    if ((params == null)||(params.length < 1)){
      return value.replaceAll("%%\\d++", "");  
    } else {
      paramCount = params.length;
      
      for(int i = 0; i < paramCount; i++){
        value.replace("%%"+i, params[i]);  
      }
    }
    
    return value;
  }

  /**
   * Get a resource into a String form. This method is simply a call of <code>getResource(key).toString();</code>.
   * @param key the key identifying the resource to return
   * @return String the resource or the key if no resource is available.
   */
  public String getString(String key){
    return getResource(key).toString();
  }

  /**
   * Get a parametrized resource into a String form. This method is simply a call of 
   * <code>getResource(key, aParams).toString();</code>.
   * @param key the key inditifying the resource to return
   * @param aParams the parameters of the resource.
   * @return String he resource parametrized or the key if no resource is available.
   */
  public String getString(String key, String[] aParams){
    return getResource(key).toString();
  }
}
