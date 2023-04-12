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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import org.jorigin.Common;

/**
 * This class represent a bundle of XML based language resources. The XML based language resource bundle behavior 
 * is most the same that the classic {@link java.util.ResourceBundle ResourceBundle}. 
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class LangResourceBundle extends ResourceBundle{

  /**
   * The lang resource wrapped by the bundle.
   */
  private LangResource resource             = new LangResource(); 
 
  private static LangResourceBundle bundle  = null;

  static {
    init();
  }
  
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
//RR REDEFINITION                                                                     RR
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
  // java.util.ResourceBundle ----------------------------------------------------------
  /**
   * Get the {@link java.util.ResourceBundle resource bundle} attached to the given {@link java.util.Locale locale}.
   * @param targetLocale the {@link java.util.Locale locale} to use.
   * @return the {@link java.util.ResourceBundle resource bundle} attached to the given {@link java.util.Locale locale}.
   */
  public static ResourceBundle getBundle(Locale targetLocale) {
    return bundle;
  }
  
  /**
   * Get the {@link java.util.ResourceBundle resource bundle} attached to the given parameters.
   * @param baseName the base name
   * @param targetLocale the target locale
   * @param loader the class loader to use
   * @param control the control to use
   * @return the resource bundle
   */
  public static ResourceBundle getBundle(String baseName, Locale targetLocale,
      ClassLoader loader, Control control) {
    return bundle;
  }
  
  @Override
  public boolean containsKey(String key){
    return resource.getKeySet().contains(key);
  }
  
  
  @Override
  public Enumeration<String> getKeys(){
    Vector<String> v = new Vector<String>(resource.getKeySet());
    return v.elements();
  }
  
  @Override
  public Locale getLocale(){
    return resource.getLocale();
  }
  
  @Override
  protected Object handleGetObject(String key){
    return resource.getResource(key);
  }
  
  @Override
  protected Set<String>  handleKeySet(){
    return resource.getKeySet();
  }
  
  @Override
  public Set<String> keySet(){
    return resource.getKeySet();
  }
  
  @Override
  protected  void setParent(ResourceBundle parent){
    super.setParent(parent);
  }
  // -----------------------------------------------------------------------------------
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
//RR REDEFINITION                                                                     RR
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR

  
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                                                       AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  
  /**
   * Set the locale of the lang resource. When a locale is set, all lang resource file associated
   * to the locale are loaded from the resource path.
   * @param locale the new locale.
   */
  public static void setLocale(Locale locale){
    bundle.resource.setLocale(locale);
    init(bundle.resource.getResourcePath(), locale);
  }

  /**
   * Set the root path of the resource language files.
   * @param path the path of the resource language  files root.
   */
  public static void setResourcePath(String path){
    bundle.resource.setResourcePath(path);
    init(path, bundle.getLocale());
  }
  
  
  /**
   * Set the root path of the resource language files.
   * @return the path of the resource language  files root.
   */
  public static String getResourcePath(){
    return bundle.resource.getResourcePath(); 
  }
  
  
  /**
   * Get the map containing the lang resources.
   * @return a hash map containing the lang resource.
   */
  public static HashMap<String, String> getResources(){
    return bundle.resource.getResources();
  }
  
  /**
   * Get the available locales.
   * @return a list of available locales.
   */
  public static ArrayList<String> getAvailableLocales(){
    return bundle.resource.getAvailableLocales();
  }
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                                                   AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
 
  
  
  /**
   * Get a resource associated to a key. If the key does not correspond to any resource, the key itself
   * is returned. The parameter given is applied to the resource if it accept parameter.
   * @param key the key inditifying the resource to return
   * @param param the parameter to apply to the resource.
   * @return the resource or the key if no resource is available.
   */
  public static Object getResource(String key, String param){  
    return bundle.resource.getResource(key, param);
  }
  
  /**
   * Get a parametrized resource associated to a key. If the key does not correspond to any resource, 
   * the key itself is returned. Parameters enable to caraterize the resource. Parameter are identified
   * in a resource by a <code>%</code> followed by the parameter number. For example, if the resource
   * is "Hello %1 and %2.", and parameters are <code>"Fox"</code> and <code>"Dana"</code>, the object
   * returned is <code>"Hello Fox and Dana."</code>
   * @param key the key inditifying the resource to return
   * @param params the parameters of the resource.
   * @return the resource parametrized or the key if no resource is available.
   */
  public static Object getResource(String key, String[] params){
    return bundle.resource.getResource(key, params);
  }
 
  /**
   * Get a resource into a String form. This method is simply a call of <code>getResource(key).toString();</code>.
   * @param key the key inditifying the resource to return
   * @return the resource or the key if no resource is available.
   */
  public static String getStringS(String key){
    return bundle.resource.getString(key);
  }
  
  /**
   * Get a resource into a String form. This method call the <code>getString(key)</code> and put
   * in upper case the fisrt character of the string.
   * @param key the key identifying the resource.
   * @return the resource or the key if no resource is available.
   */
  public static String getStringCap(String key){
    String chaine     = getStringS(key);
    char[] char_table = chaine.toCharArray();
    char_table[0]     = Character.toUpperCase(char_table[0]);
    return new String(char_table);
  }
  
  /**
   * Get a parametric resource into a String form. This method is simply a call of 
   * <code>getResource(key, aParams).toString();</code> on the current bundle.
   * @param key the key referencing the resource.
   * @param aParams the parameters to use as replacements in the resource (if any).
   * @return the localized string with integrated parameters.
   */
  public static String getString(String key, String[] aParams){
    return bundle.resource.getString(key, aParams);
  }  
  
  private static void init(String baseName, Locale locale){
    bundle = new LangResourceBundle();
    bundle.resource = new LangResource(baseName, locale);  
  }
  
  private static void init(){
    bundle = new LangResourceBundle();
    bundle.resource = new LangResource();  
  }
}
