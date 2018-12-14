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

import java.io.IOException;
import java.util.HashMap;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;



import org.jorigin.lang.LangResource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A XML reader used for handling XML language resource files. This reader delegates processing to 
 * {@link org.jorigin.lang.xml.LangResourceContentHandler LangResourceContentHandler}, {@link org.jorigin.lang.xml.LangResourceErrorHandler LangResourceErrorHandler}
 * and {@link org.jorigin.lang.xml.LangResourceXMLFilter LangResourceXMLFilter}
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 */
public class LangResourceXMLReader{

  //Liste des écouteurs informés des evenements du panneau
  protected EventListenerList idListenerList = new EventListenerList();

  protected LangResource resource = null;

  String uri = null;

  boolean working = false;

  /**
   * Create a new XML language resource reader.
   */
  public LangResourceXMLReader(){
    super();
    uri = null;
  }

  /**
   * Create a new XML language resource reader.
   * @param uri the uri of the XML document to read.
   */
  public LangResourceXMLReader(String uri){
    super();
    this.uri = uri;
  }

  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA ACCESSEURS                                                             AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  /**
   * Set the URI of the the XML document to read.
   * @param uri the URI of the the XML document to read.
   * @see #getUri()
   */
  public void setUri(String uri){
    this.uri = uri;
  }

  /**
   * Get the URI of the the XML document to read.
   * @return the URI of the the XML document to read.
   * @see #setUri(String)
   */
  public String getUri(){
    return this.uri;
  }

  /**
   * Get the {@link org.jorigin.lang.LangResource language resource} read by this object.
   * @return the {@link org.jorigin.lang.LangResource language resource} read by this object.
   */
  public LangResource getResource(){
    return this.resource;
  }

  /**
   * Get if the reader is currently working.
   * @return <code>true</code> if the reader is currently working and <code>false</code> otherwise.
   */
  public boolean isWorking(){
    return working;
  }
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  //AA FIN ACCESSEURS                                                         AA
  //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  
  /**
   * Get the language resources read from the XML document where URI is given in parameter.
   * @param uri the URI of the XML document than contains the language resources.
   * @return the language resources.
   * @throws IOException if an error occurs.
   */
  public HashMap<String, String> getParsedLangResource(String uri) throws
  IOException {

    LangResourceContentHandler contentHandler = new LangResourceContentHandler();
    ErrorHandler errorHandler = new LangResourceErrorHandler();
    XMLReader parser = null;
    LangResourceXMLFilter xmlFilterImpl = null;

    working = true;

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setValidating(true);
      
      parser = factory.newSAXParser().getXMLReader();
      xmlFilterImpl = new LangResourceXMLFilter(parser);

      // Enregistrement du gestionnaire de contenu auprès du parseur
      xmlFilterImpl.setContentHandler(contentHandler);

      // Enregistrement du gestionnaire d'erreur auprès du parseur
      xmlFilterImpl.setErrorHandler(errorHandler);

      // Parcour le document
      xmlFilterImpl.parse(uri);
      
    } catch (IOException ex) {
      throw new IOException("No such file " + uri, ex);
    } catch (SAXException ex){
      throw new IOException(ex.getMessage() + "\nParsing " + uri, ex);
    } catch (ParserConfigurationException ex) {
      throw new IOException(ex.getMessage() + "\nUnable to configure parser for " + uri, ex);
    }

    working = false;
    
    return contentHandler.getResources();
  }

}

