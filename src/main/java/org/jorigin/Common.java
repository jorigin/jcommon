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
package org.jorigin;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jorigin.logging.LogHandler;

/**
 * The information class about the Common libraries for JOrigin project.
 * This class is only used for gathering informations about the Common libraries.<br>
 * The logging level of the common logger can be set by the system property <code>java.util.logging.level</code> with values:
 * <ul>
 * <li>OFF
 * <li>SEVERE
 * <li>WARNING
 * <li>INFO
 * <li>CONFIG
 * <li>FINE
 * <li>FINER
 * <li>FINEST
 * <li>ALL
 * </ul>
 * When a value is set, all level above are implicitly included. For example, a level <code>FINE</code> enable also <code>CONFIG</code>, <code>INFO</code>, 
 * <code>WARNING</code> and <code>SEVERE</code>.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class Common {

  /**
   * The {@link java.util.logging.Logger logger} used for reporting.
   */
  public static Logger logger = null;
  
  static {
    init();
  }
  
  /**
   * The build version.
   */
  public static final long BUILD     = 202005081200L;
  
  /**
   * The version number
   */
  public static final String version = "1.0.13";
  
  /**
   * Initialize the JOrigin common package.
   */
  public static final void init(){

    logger = Logger.getLogger("org.jorigin.Common");
    
    LogHandler handler = new LogHandler();

    String property = System.getProperty("java.util.logging.level");
    Level level = Level.INFO;
    if (property != null){
      if (property.equalsIgnoreCase("OFF")){
        level = Level.OFF;
      } else if (property.equalsIgnoreCase("SEVERE")){
        level = Level.SEVERE;
      } else if (property.equalsIgnoreCase("WARNING")){
        level = Level.WARNING;
      } else if (property.equalsIgnoreCase("INFO")){
        level = Level.INFO;
      } else if (property.equalsIgnoreCase("CONFIG")){
        level = Level.CONFIG;
      } else if (property.equalsIgnoreCase("FINE")){
        level = Level.FINE;
      } else if (property.equalsIgnoreCase("FINER")){
        level = Level.FINER;
      } else if (property.equalsIgnoreCase("FINEST")){
        level = Level.FINEST;
      } else if (property.equalsIgnoreCase("ALL")){
        level = Level.ALL;
      }
    }

    handler.setLevel(level);
    logger.setLevel(level);
    
    logger.addHandler(handler);
    logger.setUseParentHandlers(false);
    
  }
  
  /**
   * Set the {@link java.util.logging.Logger logger} to use for reporting.
   * @param logger the {@link java.util.logging.Logger logger} to use for reporting.
   */
  public static void setLogger(Logger logger){
    Common.logger = logger;
  }
}
