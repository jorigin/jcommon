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

import java.util.Enumeration;
import java.util.Locale;

import org.jorigin.Common;

/**
 * A test dedicated to language resources.
 * @author Julien Seinturier - (c) 2010 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 *
 */
public class LangResourceBundleTest {

  

  /**
   * The test main method.
   * @param args the command line arguments.
   */
  public static void main(String[] args){
    
    Locale locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    
    LangResourceBundle lres    = (LangResourceBundle) LangResourceBundle.getBundle(locale);
    
    Common.logger.info("Locale: "+locale);
    
    Enumeration<String> enumeration = lres.getKeys();
    String key = null;
    while(enumeration.hasMoreElements()){
      key = enumeration.nextElement();
      Common.logger.info("  Key: "+key+" value: "+lres.getString(key));
    }
  }
  
}
