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

import java.util.HashMap;

import org.jorigin.Common;

/**
 * This class is a default implementation of the {@link org.jorigin.plugin.IPlugger IPlugger} interface. 
 * The extension points are stored in an underlying {@link java.util.HashMap HashMap}.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public class DefaultPlugger implements org.jorigin.plugin.IPlugger{

  
  /**
   * This map contains all the available extension points for the aplication.
   */
  private HashMap<String, Object> extensionPoints = null;
  
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  //II IMPLEMENTATION                                      II
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  @Override
  public boolean addExtensionPoint(String key, Object extensionPoint) {
    // Verification que la cle n'est pas deje presente  
    if (this.extensionPoints.keySet().contains(key)){
      return false;
    } else {
      if (this.extensionPoints.put(key, extensionPoint) != null){
          return true;
      }
    }
    
    return false;
  }

  @Override
  public Object getExensionPoint(String key) {
    return extensionPoints.get(key);  
  }

  @Override
  public String[] getExtensionPointKeys() {
    return extensionPoints.keySet().toArray(new String[extensionPoints.size()]);
  }
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  //II FIN IMPLEMENTATION                                  II
  //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
  
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC CONSTRUCTEUR                                        CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  /**
   * Create a new default plugger.
   */
  public DefaultPlugger(){
    extensionPoints = new HashMap<String, Object>();
  }
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  //CC FIN CONSTRUCTEUR                                    CC
  //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

}
