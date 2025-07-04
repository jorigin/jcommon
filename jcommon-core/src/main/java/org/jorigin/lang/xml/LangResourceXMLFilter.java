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

import javax.swing.event.EventListenerList;

import org.jorigin.Common;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * A simple override of the {@link org.xml.sax.helpers.XMLFilterImpl XMLFilterImpl} dedicated to the lang XML resource file parsing. 
 * Most of the methods simply delegate to super methods.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class LangResourceXMLFilter extends XMLFilterImpl{

	/**
	 * Create a new XML resource filter.
	 */
	public LangResourceXMLFilter() {
		super();
	}

	/**
	 * The attached listeners.
	 */
	protected EventListenerList idListenerList = new EventListenerList();

	/**
	 * Create a new XML resource filter with an input parser.
	 * @param parser the parser to use.
	 */
	public LangResourceXMLFilter(XMLReader parser) {
		super(parser);
	}


	//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	//SS SURCHARGE                                                              SS
	//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	@Override
	public void parse (InputSource input) throws SAXException, IOException{
		super.parse(input);
	}

	@Override
	public void parse(String systemId) throws SAXException, IOException{
		try {
			parse(new InputSource(systemId));
		} catch (IOException ex) {
			throw new IOException("[LangResourceXMLFilter] [parse(String)] Unable to parse "+systemId, ex);
		} catch (SAXException ex) {
			throw new IOException("[LangResourceXMLFilter] [parse(String)] Unable to parse "+systemId, ex);
		}
	}


	@Override
	public void startDocument () throws SAXException{
		super.startDocument();
	}


	@Override
	public void endDocument () throws SAXException{
		super.endDocument();
	}


	@Override
	public void startElement (String uri, String localName, String qName,
			Attributes atts)
					throws SAXException{
		super.startElement(uri, localName, qName, atts);
	}


	@Override
	public void endElement (String uri, String localName, String qName)
			throws SAXException{
		super.endElement(uri, localName, qName);
	}


	@Override
	public void warning (SAXParseException e) throws SAXException{
		super.warning(e);
	}


	@Override
	public void error (SAXParseException e) throws SAXException {
		super.error(e);
	}


	@Override
	public void fatalError (SAXParseException e) throws SAXException {
		super.fatalError(e);
	}

	//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
	//SS FIN SURCHARGE                                                          SS
	//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS


}
