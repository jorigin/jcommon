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
package org.jorigin.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.File;

import org.jorigin.Common;
import org.jorigin.lang.PathUtil;


/**
 * This class is an helper class designed for the handling of I/O streams. This class provide methods for creating and managing streams from various sources.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class IOStreamUtil {

  /**
   * Open an {@link java.io.InputStream} from the resource located by the given <code>uri</code>. The returned stream can be:<br>
   * <ul>
   * <li> a {@link java.io.FileInputStream} if the resource is a file on a local system;
   * <li> a {@link java.io.BufferedInputStream} if the resource is an <code>url</code> or a location on a remote system;
   * </ul>
   * @param uri the location of the resource
   * @return the input stream opened to the resource
   * @throws IOException if an error occurs.
   */
  public static InputStream getInputStream(String uri) throws IOException {

    InputStream is = null;
    URL url = null;
    File file = null;

    switch (PathUtil.getProtocol(uri)) {
      case PathUtil.SYSTEM:
        file = new File(uri);
        if (file.exists()) {
          try {
            is = new FileInputStream(file);
          } catch (FileNotFoundException ex) {
            is = null;
            throw new IOException(ex.getMessage());
          }
        } else {
          is = null;
          throw new IOException("File " + file.getPath() + " does not exist");
        }
        break;

      case PathUtil.URL_FILE:
        file = new File(PathUtil.URIToPath(uri));

        if (file.exists()) {
          try {
            file = new File(PathUtil.URIToPath(uri));
            is = new FileInputStream(file);
          } catch (FileNotFoundException ex) {
            is = null;
            throw new IOException(ex.getMessage());
          }
        } else {
          is = null;
          throw new IOException("File " + file.getPath() + " does not exist");
        }
        break;

      case PathUtil.URL_FTP:
        try {
          url = new URL(uri);
          is = new BufferedInputStream(url.openStream());
        } catch (MalformedURLException ex) {
          is = null;
          throw new IOException(ex.getMessage());
        } catch (IOException ex) {
          is = null;
          throw new IOException(ex.getMessage());
        }
        break;

      case PathUtil.URL_HTTP:
        try {
          url = new URL(uri);
          
          //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          
          is =url.openStream();
          
        } catch (MalformedURLException ex) {
          is = null;
          throw new IOException(ex.getMessage());
        } catch (IOException ex) {
          is = null;
          throw new IOException(ex.getMessage());
        }
        break;

      default:
        is = null;
        throw new IOException("Cannot determine protocol used by "+uri);
    }

    return is;
  }

  /**
   * Get a buffered input stream from the <code>uri</code> given in parameter.
   * @param uri the uri source of the input stream.
   * @return a buffered input stream.
   * @throws IOException if the stream cannot be set up.
   */
  public static BufferedInputStream getBufferedInputStream(String uri)
      throws IOException {

    
    return getBufferedInputStream(uri, -1);
  }

  
  /**
   * Get a buffered input stream from the <code>uri</code> given in parameter. The input stream
   * buffer size is given by the <code>bufferSize</code> parameter. If the buffer size is less than 1, 
   * the buffered input stream created have a default buffer size.
   * @param uri the uri source of the input stream.
   * @param bufferSize the size of the buffer
   * @return a buffered input stream.
   * @throws IOException if the stream cannot be set up.
   */
  public static BufferedInputStream getBufferedInputStream(String uri, int bufferSize)
  throws IOException {

    BufferedInputStream bis = null;

    try {
      if (bufferSize > 0){
        bis = new BufferedInputStream(getInputStream(uri), bufferSize);
      } else {
        bis = new BufferedInputStream(getInputStream(uri));
      }
    } catch (IOException e) {
      bis = null;
    }
    if (bis == null) {
      throw new IOException("Cannot create a buffered input stream for " + uri); 
    }
    return bis;
  }
  
  
  /**
   * Get an output stream to the <code>uri</code> given in parameter.
   * @param uri the uri of the resource outputed
   * @return an output stream to the resource.
   * @throws IOException if the output stream cannot be set up.
   */
  public static OutputStream getOutputStream(String uri) throws IOException {

    OutputStream os = null;
    File file = null;

    switch (PathUtil.getProtocol(uri)) {

      // L'uri pointe une ressource système
      case PathUtil.SYSTEM:
        try {

          file = new File(uri);

          if (!file.exists()) {
            if (file.getParentFile() != null){
              file.getParentFile().mkdirs(); 
            }
            file.createNewFile();

          }

          os = new FileOutputStream(file);

        } catch (FileNotFoundException ex) {
          os = null;

          throw new IOException(ex.getMessage());
        }
        break;

      // L'uri pointe vers un url locale
      case PathUtil.URL_FILE:

        try {
          file = new File(PathUtil.URIToPath(uri));
          // System.err.println("[IOStreamUtil] getOutputStream():
          // "+PathUtil.URIToPath(uri));
          if (!file.exists()) {
            if (file.getParentFile() != null){
              file.getParentFile().mkdirs(); 
            }
            file.createNewFile();
          }
          os = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
          os = null;
          throw new IOException(ex.getMessage());
        }
        break;

      // L'uri pointe une ressource FTP
      case PathUtil.URL_FTP:
        System.err.println("[IOStreamUtil] FTP output not yet implemented");
        os = null;
        throw new IOException("Protocol FTP not implemented "+uri);

      // L'uri pointe une ressource HTTP
      case PathUtil.URL_HTTP:
        System.err.println("[IOStreamUtil] HTTP output not yet implemented");
        os = null;
        throw new IOException("Protocol HTTP not implemented "+uri);
      
      default:
        os = null;
        throw new IOException("Cannot determine protocol used by "+uri);
    }

    return os;
  }

  /**
   * Get a buffered output stream to the <code>uri</code> given in parameter.
   * @param uri the uri source of the output stream.
   * @return a buffered output stream.
   * @throws IOException if the stream cannot be set up.
   * @see #getBufferedOutputStream(String, int)
   */
  public static BufferedOutputStream getBufferedOutputStream(String uri)
      throws IOException {

    return getBufferedOutputStream(uri, -1);
  }

  
  /**
   * Get a buffered output stream to the <code>uri</code> given in parameter. The output stream
   * buffer size is given by the <code>bufferSize</code> parameter. If the buffer size is less than 1, 
   * the buffered output stream created have a default buffer size.
   * @param uri the uri source of the output stream.
   * @param bufferSize the size of the buffer
   * @return a buffered output stream.
   * @throws IOException if the stream cannot be set up.
   */
  public static BufferedOutputStream getBufferedOutputStream(String uri, int bufferSize)
  throws IOException {

    BufferedOutputStream bos = null;
    try {
      
      if (bufferSize > 0){
        bos = new BufferedOutputStream(getOutputStream(uri), bufferSize);
      } else {
        bos = new BufferedOutputStream(getOutputStream(uri));
      }
    } catch (IOException e) {
      bos = null;
    }

    if (bos == null) {
      throw new IOException("Cannot create a buffered output stream for " + uri);
    }

    return bos;
  }
  
  
  /**
   * Simple copy of an input stream to an output stream.
   * 
   * @param source
   *          Input stream to the source
   * @param destination
   *          Output stream to the destination
   * @return <code>true</code> if the copy was successfull, <code>false</code> if not
   */
  public static boolean copy(InputStream source, OutputStream destination) {
    boolean result = false;

    try {

      // Lecture par segment de 0.5M
      byte buffer[] = new byte[1024];
      int nbLecture;

      while ((nbLecture = source.read(buffer)) != -1) {
        destination.write(buffer, 0, nbLecture);
      }

      // Copie reussie
      result = true;
    } catch (java.io.FileNotFoundException f) {
      System.err.println(f);
      result = false;
    } catch (java.io.IOException e) {
      System.err.println(e);
      result = false;
    } finally {
      /* Quoi qu'il arrive, on ferme les flux */
      try {
        source.close();
      } catch (Exception e) {
      }
      try {
        destination.close();
      } catch (Exception e) {
      }
    }
    return (result);
  }

}
