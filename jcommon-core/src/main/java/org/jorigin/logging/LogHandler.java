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
package org.jorigin.logging;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import org.jorigin.Common;

/**
 * This class is a convenience rewriting of {@link java.util.logging.ConsoleHandler ConsoleHandler} class. 
 * Its provide a more flexible management of logging.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class LogHandler extends StreamHandler {

  /**
   * Configure the handler
   */
  private void configure() {

    Filter filter = new Filter(){

      @Override
      public boolean isLoggable(LogRecord record) {
        return true;
      }};
  
    Formatter formatter = new Formatter(){

      private String lineSeparator = System.getProperty("line.separator");
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
      
      @Override
      public String format(LogRecord record) {
        if (record.getThrown() == null){
          return "("+this.sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] "+record.getMessage()+this.lineSeparator;
        } else {
          String str = "("+this.sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] caused by "+record.getThrown().getMessage()+this.lineSeparator;
          
          StackTraceElement[] elements = record.getThrown().getStackTrace();
          for(int i = 0; i < elements.length; i++){
            str += "("+this.sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] at "+elements[i]+this.lineSeparator;
          }
          return "("+this.sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] "+record.getMessage()+this.lineSeparator+str;
        }
      }};  
    
    setFilter(filter);
    setFormatter(formatter);
    
    try {
      setEncoding("UTF-8");
    } catch (Exception ex) {
      try {
          setEncoding(null);
      } catch (Exception ex2) {
      // doing a setEncoding with null should always work.
      // assert false;
      }
    }
  }

  /**
   * Create a Log Handler for {@link System#out}.
   */
  public LogHandler() {
    configure();
    setOutputStream(System.out);
    setLevel(Level.CONFIG);
  }

  /**
   * Create a Log Handler that publish its records to the given <code>output</code>
   * @param output the output stream where the records capted by this handler are published.
   */
  public LogHandler(OutputStream output) {
    configure();
    setOutputStream(output);
    setLevel(Level.CONFIG);
  }
  
  /**
   * Create a Log Handler that publish its records to the given <code>output</code>. 
   * The record level is given by parameter <code>level</code>
   * @param output the output stream where the records capted by this handler are published.
   * @param level the level of the publihed records.
   */
  public LogHandler(OutputStream output, Level level) {
    configure();
    setOutputStream(output);
    setLevel(level);
  }
  
  /**
   * Publish a {@link LogRecord log record}.<br>
   *
   * The logging request was made initially to a Logger object,
   * which initialized the {@link LogRecord log record} and forwarded it here.
   *
   * @param  record  description of the log event. A null record is
   *                 silently ignored and is not published
   */
  public void publish(LogRecord record) {
    super.publish(record);  
    flush();
  }

  /**
   * Override {@link StreamHandler#close} to do a flush but not
   * to close the output stream.  That is, we do <b>not</b> close {@link System#err}.
   */
  public void close() {
    flush();
  }
}
