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
package org.jorigin.lang.xml;

import java.util.logging.Level;

import org.jorigin.Common;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An error handler used with the {@link org.jorigin.lang.xml.LangResourceContentHandler LangResourceContentHandler}. 
 * This error handler only output errors on the {@link org.jorigin.Common Common} logger.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 *
 */
public class LangResourceErrorHandler implements org.xml.sax.ErrorHandler {

  @Override
  public void error(SAXParseException exception) throws SAXException {
    Common.logger.log(Level.SEVERE, "Lang resource XML parse error", exception);
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    Common.logger.log(Level.SEVERE, "Lang resource XML parse error", exception);
  }

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    Common.logger.log(Level.SEVERE, "Lang resource XML parse error", exception);
  }

}
