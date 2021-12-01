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

import java.util.HashMap;

import org.jorigin.Common;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A content handler for the lang resource XML files.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public class LangResourceContentHandler implements ContentHandler {


  /**
   * The name of the resource list XML element
   */
  public static String ELEMENT_RESOURCES = "resources";
  
  /**
   * The name of the resource XML element
   */
  public static String ELEMENT_RESOURCE  = "resource";
  
  /**
   * The name of the resource key attribute
   */
  public static String ATTRIBUTE_KEY     = "key";
  
  /**
   * The name of the resource value attribute.
   */
  public static String ATTRIBUTE_VALUE   = "value";
  
  
  /**
   * A Hash map containing all resources.
   */
  private HashMap<String, String> resources      = null;
  
  
  private String key = null;
  private String value = null;
  
  
  /** Hold onto the locator for location information */
  protected Locator locator;
  
  /**
   * Get the resources attached to this content handler.
   * @return the resources attached to this content handler.
   */
  public HashMap<String, String> getResources(){
    return this.resources;
  }
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    
  }

  @Override
  public void endDocument() throws SAXException {
    
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    
    
    if (localName.equals(ELEMENT_RESOURCES)) {
    }
    
    if (localName.equals(ELEMENT_RESOURCE)) {
      
      resources.put(key, value);
      
      key = null;
      value = null;
    }
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {
    
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    
  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
    
  }

  @Override
  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  @Override
  public void skippedEntity(String name) throws SAXException {
    
  }

  @Override
  public void startDocument() throws SAXException {
    
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    
   
    
    if (localName.equals(ELEMENT_RESOURCES)) {
      resources = new HashMap<String, String>();
    }
    
    if (localName.equals(ELEMENT_RESOURCE)) {
      key   = atts.getValue(ATTRIBUTE_KEY);
      value = atts.getValue(ATTRIBUTE_VALUE);
    }
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    
  }
  
}