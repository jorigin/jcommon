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

import java.net.MalformedURLException;
import java.net.URL;

import org.jorigin.Common;

import java.net.URI;
import java.io.File;
import java.net.URISyntaxException;

/**
 * This class provide methods for processing resource pathes (files, URL, URI, ...).
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class PathUtil {

  /** A constant symbolizing a system path. A system path is
   * a absolute or relative exploitation system expressed path
   */
  public static final int SYSTEM     = 1;

  /** A constant symbolizing a URL path followin the file scheme */
  public static final int URL_FILE   = 2;

  /** A constant symbolizing a URL path followin the ftp scheme */
  public static final int URL_HTTP   = 3;

  /** A constant symbolizing a URL path followin the http scheme */
  public static final int URL_FTP    = 4;

  /** A constant symbolizing a URL path followin the sftp scheme */
  public static final int URL_SFTP   = 5;
  
  /** A constant symbolizing a samba path */
  public static final int SMB        = 6;
  
  /** A constant symbolizing a mysql path. */
  public static final int MYSQL      = 7;
  
  /** A constant symbolizing an unknown path */
  public static final int UNKNOWN    = 1000;

  /**
   * Create a well formed URI from a path. The path can be an absolute or relative
   * system path, an URL (ftp, http, ...). The head of the uri is:
   * file:// for a local uri, http:// and ftp:// for distants URI.
   * @param path String the path to convert into URI.
   * @return URI the uri created from the path
   */
  public static URI pathToURI(String path){

    // URI retournée
    URI uri = null;

    // Chaine de caracère à retourner
    String str = new String(path);

    // Variable pour utiliser les expressions régulières
    java.util.regex.Pattern p;
    java.util.regex.Matcher m;

    if (path == null){
      return null;
    }

    // Traitement du path pour remplacer les caractères problématiques
    str = str.replace("\\","/");

    // Enlevement du dernier / si le path en contient un
    if (str.endsWith("/")){
      str = str.substring(0, str.length() - 1);
    }

    // Remplace les occurences de / contigus par un seul /
    str = str.replaceAll("/{1,}+", "/");

    // Path etant des adresses systèmes absolues Windows
    p = java.util.regex.Pattern.compile("[a-zA-Z]:.*");
    m = p.matcher(str);
    if(m.matches()){
      str = "file://"+str;
    }

    // Cas particulier sous windows ou le path commence par file:<lecteur>:
    p = java.util.regex.Pattern.compile("file:[a-zA-Z]:.*");
    m = p.matcher(str);
    if(m.matches()){
      p = java.util.regex.Pattern.compile("file:");
      m = p.matcher(str);
      str = m.replaceFirst("file://");
    }


    // Path etant des URL locale absolues pour un système Windows
    p = java.util.regex.Pattern.compile("file:/{1,}[a-zA-Z]:.*");
    m = p.matcher(str);
    if(m.matches()){
      p = java.util.regex.Pattern.compile("file:/{1,}");
      m = p.matcher(str);
      str = m.replaceFirst("file://");
    } else {

      // Path etant des URL locale absolues pour un système Linux
      // Il ne doit pas contenir de point après le scheme
      p = java.util.regex.Pattern.compile("file:/{1,}[^\\.].*");
      m = p.matcher(str);
      if(m.matches()){
        p = java.util.regex.Pattern.compile("file:/{1,}");
        m = p.matcher(str);
        str = m.replaceFirst("file:///");
      } else{
        
        // Cas d'une URL locale relative (independante du systeme)
        p = java.util.regex.Pattern.compile("file:/{1,}\\.{1,2}.*");
        m = p.matcher(str);
 
        if (m.matches()){
          p = java.util.regex.Pattern.compile("file:/{1,}");
          m = p.matcher(str);
          str = m.replaceFirst("file://");    
        } else {
        
          // Mettre ici les autre cas d'uri
          //p = java.util.regex.Pattern.compile("/.*");
          //m = p.matcher(path);
          //path = "file://"+path;

          // Path etant des URL web (http:)
          p = java.util.regex.Pattern.compile("http:/{1,}.*");
          m = p.matcher(str);
          if(m.matches()){
            p = java.util.regex.Pattern.compile("http:/{1,}");
            m = p.matcher(str);
            str = m.replaceFirst("http://");
          } else {

            // Path etant des URL web (FTP)
            p = java.util.regex.Pattern.compile("ftp:/{1,}.*");
            m = p.matcher(str);
            if(m.matches()){
              p = java.util.regex.Pattern.compile("ftp:/{1,}");
              m = p.matcher(str);
              str = m.replaceFirst("ftp://");
          
            // Path etant des chemins systeme absolu linux
            } else {
          
              p = java.util.regex.Pattern.compile("/{1,}.*");
              m = p.matcher(path);
              if (m.matches()){
                str = "file://"+str;
      
              // Cas par defaut (le plus souvent des chemin locaux relatifs)
              } else {
                str = "file://"+str;
              }
            }
          }
        }
      }
    }

    try {
      str = str.replaceAll(" ", "%20");
      uri = new URI(str);
    } catch (URISyntaxException ex) {
      uri = null;
    }

    return uri;
  }

  /**
   * Convert a uri into a valid system path or a distant url path (http, ftp, ...)
   * @param uri URI uri the uri to convert
   * @return String path corresponding to the uri
   */
  public static String URIToPath(URI uri){

    String path = null;

    if (uri == null){
      return null;
    }

    // Traitement en fonction du type d'URI
    //  - URI absolue
    //  - URI relative
    //  - URI avec authorité (URL)
    try {

      // Si l'uri est en réalité une URL distante, elle est retournée sans
      // modification
      if ((uri.getScheme().compareTo("http") == 0) || (uri.getScheme().compareTo("ftp") == 0)){
        return uri.toString();
      }

      // Cas l'URI est locale

      // Si elle est absolue (commencant par file:)
      if (uri.isAbsolute()){
        if (uri.getAuthority() != null){
          path = new File(uri.getAuthority()+uri.getPath()).getPath();
        } else{
          path = uri.getPath();
        }

      // Si l'URI est un chemin système
      } else{
        path = new File(uri.toString()).getPath();
      }
      
      // Remplacement des %20 codant un espace dans le nom de fichier
      // par un vrai espace.
      if (path != null){
        path = path.replace("%20", " ");
      }
      
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace(System.err);
      path = null;
    }

    // Si l'uri est bien une uri système (et non une distante) alors il faut
    // mettre à jour les séparateurs de chemins.
    if (getProtocol(path) == SYSTEM){
      path = path.replace("/", File.separator);
    }

    return path;
  }


  /**
   * Return a system path from a non constrained path (uri, url, relative system path, ...)
   * @param uri String the path to trasform in system path
   * @return String the system path (absolute or relative)
   */
  public static String URIToPath(String uri){

    URI tmpuri = null;

    if (uri == null){
      return null;
    }

    // Creation d'une uri correcte en fonction du path quelquonque
    tmpuri = pathToURI(uri);
    
    if (tmpuri == null){
      return null;
    }
    
    tmpuri = tmpuri.normalize();

    // Transformation de l'uri en path système (si l'uri est distante absolue, pas de
    // transformation)
    return URIToPath(tmpuri);
  }

  
  /**
   * Return the protocol of a path. A protocol can be:<br>
   * <b>PathUtil.SYSTEM</b> if the path is a system dependent path.<br>
   * <b>PathUtil.URL_FILE</b> if the path is a system url (file:/...).<br>
   * <b>PathUtil.URL_HTTP</b> if the path is a web path (http://...).<br>
   * <b>PathUtil.URL_FTP</b> if the path is a web file path (ftp://...).<br>
   * @param path String the path to check.
   * @return int one of the integer value described above.
   */
  public static int getProtocol(String path){
    

    java.util.regex.Pattern p;
    java.util.regex.Matcher m;

    if (path == null){
      return 0;
    }

    // Path etant des URL locale absolues pour un système Windows
    p = java.util.regex.Pattern.compile("file:.*");
    m = p.matcher(path);
    if(m.matches()){
      return URL_FILE;
    } 
    
    
    // Path etant des URL web (http:)
    p = java.util.regex.Pattern.compile("http:.*");
    m = p.matcher(path);
    if(m.matches()){
      return URL_HTTP;
    }
    
    
    // Path etant des URL web (FTP)
    p = java.util.regex.Pattern.compile("ftp:.*");
    m = p.matcher(path);
    if(m.matches()){
      return URL_FTP;
    }
    
    
    // Path etant des URL web (SFTP)
    p = java.util.regex.Pattern.compile("sftp:.*");
    m = p.matcher(path);
    if(m.matches()){
      return URL_SFTP;
    }
    
    // Path etant des ressources samba (SMB)
    p = java.util.regex.Pattern.compile("smb:.*");
    m = p.matcher(path);
    if(m.matches()){
      return SMB;
    }
    
    // Path etant des bases de données mysql (MYSQL)
    p = java.util.regex.Pattern.compile("mysql:.*");
    m = p.matcher(path);
    if(m.matches()){
      return MYSQL;
    }
    
    
    // Path etant des chemins systeme linux
    p = java.util.regex.Pattern.compile("/{1,}.*");
    m = p.matcher(path);
    if(m.matches()){
      return SYSTEM;
    }
    
    // Path etant des chemins systeme windows
    p = java.util.regex.Pattern.compile("[a-zA-Z]:.*");
    m = p.matcher(path);
    if (m.matches()){
      return SYSTEM;
    }
    
    // Chemins ayant des cheme inconnus (commence par une succession de lettres
    // puis :
    p = java.util.regex.Pattern.compile("[a-zA-Z]{2,}:.*");
    m = p.matcher(path);
    
    if (m.matches()){
      return UNKNOWN;
      
    }
    
    
    // Si aucun scheme n'est present, on considère que le path est un chemin 
    // systeme
    return SYSTEM;
    
  }

  /**
   * Return true if the URL given in parameter is absolute
   * @param url String the url string to test
   * @return boolean true if the url is absolute, false otherwise
   */
  public static boolean isAbsoluteURL(String url){
    if (url.startsWith("file:/") || (url.startsWith("http:/"))) {
      return true;
    }
    return false;
  }


  /**
   * Return true if the path given in parameter is absolute. A path is absolute if:<br>
   * <b>On linux system</b><br>
   * The path start with <code>/</code><br>
   * <b>On windows system</b><br>
   * The path start with <code>[A-Z]</code>:
   * @param path String the path to test
   * @return boolean true if the path is absolute, false otherwise.
   */
  public static boolean isAbsolutePath(String path){

    java.util.regex.Pattern p;
    java.util.regex.Matcher m;

    String tmp = null;
    
    boolean isAbsolute = false;
    
    switch (getProtocol(path)){
      case PathUtil.SYSTEM:
        
        // Path etant des adresses systèmes absolues Windows
        p = java.util.regex.Pattern.compile("[a-zA-Z]:.*");
        m = p.matcher(path);
        if(m.matches()){
          isAbsolute = true;
        } 

        // Path absolu pour des adresses Linux
        else if(path.startsWith("/")){
          isAbsolute = true;
          
        // Cas par defaut: Ce n'est pas une adresse absolue
        } else {
          isAbsolute = false;
        }
        break;
        
      case PathUtil.URL_FILE:
        // On enleve le scheme
        tmp = pathToURI(path).toString();
        tmp = tmp.replaceFirst("[a-zA-Z]{1,}://", "");

        // Path etant des adresses systèmes absolues Windows
        p = java.util.regex.Pattern.compile("[a-zA-Z]:.*");
        m = p.matcher(tmp);
        if(m.matches()){
          isAbsolute = true;
        } 

        // Path absolu pour des adresses Linux
        else if(tmp.startsWith("/")){
          isAbsolute = true;
          
        // Cas par defaut: Ce n'est pas une adresse absolue
        } else {
          isAbsolute = false;
        }
        break;
        
      case PathUtil.URL_HTTP:
        return true;
        
      case PathUtil.URL_FTP:
        return true;
        
      case PathUtil.URL_SFTP:
        return true;
    }
    
 
    
    return isAbsolute;
  }



  /**
   * Relativize given uri from the given root.
   * @param uri URI the uri to relativize
   * @param root URI the root against the relativization is done
   * @return String the relative path computed
   */
  public static String relativize(URI uri, URI root){
    return root.relativize(uri).toString();
  }


  /**
   * Resolve the path given in parameter against the root path given. The resolution produce a well formed
   * uri. If a path is needed, the call <code>PathUtil.URIToPath(resolve(uri, root))</code> return the system path result
   * of the resolution.
   * @param path String The path to resolve
   * @param root String The root path used in the resolution
   * @return URI the well formed uri, result of the resolution.
   */
  public static URI resolve(String path, String root){
      URI uri = null;
      URI uriRoot = null;
      String str = null;

      // Chaine de caracère temporaires
      String strPath = new String(path);
      String strRoot = new String(root);


     try{
        //uriRoot = new URI(root);
        uriRoot = pathToURI(strRoot);

        // Creation d'une URI en fonction du path
        uri = pathToURI(strPath);


        // Si les URI sont des URI distantes, il n'y a qu'a les normaliser
        if ((uri.getScheme().compareTo("http") == 0) || (uri.getScheme().compareTo("ftp") == 0)){
          // rien à faire, la normalisation est faite d'office plus bas
        } else {

          // On ne resoud l'URI que si elle est locale
          if (uri.getScheme().compareTo("file") == 0){

            // Il faut oter le scheme:// de l'uri pour pouvoir la résoudre
            str = uri.toString();
            str = str.replaceFirst("file://", "");
            uri = new URI(str);

            // Obligation de passer en URI système car resolve est buggé sous windows
            // et perd les : après les lettres de lecteur si l'URI à un scheme autre
            // que la lettre du lecteur.
            strRoot = strRoot.replaceFirst("file://", "");
            uriRoot = new URI(strRoot);
            uriRoot.normalize();
          }

          uri = uri.normalize();

          // Resoud le nom d'uri par rapport à la racine et recrée une URI correcte.
          // Si l'on utilise simplement uri = uriRoot.resolve(uri) il n'y a qu'un
          // seul / àpres le scheme.

          //uri = uriRoot.resolve(uri);
          uri = pathToURI(uriRoot.resolve(uri).toString());

        }
      } catch (URISyntaxException ex){
        System.err.println(ex.getMessage());
        ex.printStackTrace(System.err);
      }

      uri = uri.normalize();

      // Si l'uri est déjà absolue, il faut verifier que la forme du chemin est
      // commence bien par //.
      // une uri est absolue si et seulement si elle possède un scheme (http:, file:, ...)
      if (uri.isAbsolute()){
        // Si l'URI est une URL en http elle est absolue et bien formée, il n'y a
        // rien à faire
        if (uri.getScheme().compareTo("http") == 0){
          return uri;
        }
        else if (uri.getScheme().compareTo("file") == 0){
          return uri;
      } else if (uri.getScheme().compareTo("ftp") == 0){
          return uri;
      } else {
          //System.err.println("Unknown protocol uri: "+uri);
          return uri;
        }
      } else {
          System.err.println(uri);
      }

      return uri;
  }

  /**
   * Relativize given path against the given root
   * @param path String the path to relativize
   * @param root String the root against the relativization is done
   * @return String the relative path computed
   */
  public static String relativize(String path, String root){
    URI uri     = null;
    URI uriRoot = null;
    String str  = null;

    try{
      uriRoot = pathToURI(root);

      // Creation d'une URI en fonction du path
      uri = pathToURI(path);

      // Si les URI sont des URI distantes, il n'y a qu'a les normaliser
      if ((uri.getScheme().compareTo("http") == 0) || (uri.getScheme().compareTo("ftp") == 0)){
        // rien à faire, la normalisation est faite d'office plus bas
      } else {

        // On ne resoud l'URI que si elle est locale
        if (uri.getScheme().compareTo("file") == 0){

          // Il faut oter le scheme:// de l'uri pour pouvoir la résoudre
          str = uri.toString();
          str = str.replaceFirst("file://", "");
          uri = new URI(str);

          // Obligation de passer en URI système car resolve est buggé sous windows
          // et perd les : après les lettres de lecteur si l'URI à un scheme autre
          // que la lettre du lecteur.
          root = root.replaceFirst("file://", "");
          uriRoot = new URI(root);
          uriRoot.normalize();
        }

        uri = uri.normalize();

        // Resoud le nom d'uri par rapport à la racine et recrée une URI correcte.
        // Si l'on utilise simplement uri = uriRoot.resolve(uri) il n'y a qu'un
        // seul / àpres le scheme.

        uri = pathToURI(uriRoot.relativize(uri).toString());

      }
    } catch (URISyntaxException ex){
      System.err.println(ex.getMessage());
      ex.printStackTrace(System.err);
    }

    uri = uri.normalize();

    // Si l'uri est déjà absolue, il faut verifier que la forme du chemin est
    // commence bien par //.
    // une uri est absolue si et seulement si elle possède un scheme (http:, file:, ...)
    if (uri.isAbsolute()){
      // Si l'URI est une URL en http elle est absolue et bien formée, il n'y a
      // rien à faire
      if (uri.getScheme().compareTo("http") == 0){
        return uri.toString();
      }
      else if (uri.getScheme().compareTo("file") == 0){
        return uri.toString();
      } else if (uri.getScheme().compareTo("ftp") == 0){
        return uri.toString();
      } else {
        //System.err.println("Unknown protocol uri: "+uri);
        return uri.toString();
      }
    } else {
      System.err.println(uri);
    }

    return uri.toString();
  }

  
  /**
   * Return the file name pointed by the <code>path</code> given in parameter. The file 
   * name is part of the path folowing the last separator. 
   * @param path the path from which the file name have to be extracted.
   * @return the file name.
   */
  public static String getFileName(String path){
    String fileName = null;
    
    String uri      = null;

    if (path == null){
      return null;
    } else {
      
      // Mise sous forme d'uri standard du chemin
      uri = pathToURI(path).toString();
      
      // Si l'uri est correcte
      if (uri != null){
        
        // Si l'uri est un chemin à plusieurs niveaux (présence d'au moins un séparateur)
        // alors le nom de fichier est la dernière partie du chemin.
        if (uri.indexOf("/") != -1){
          fileName = uri.substring(uri.lastIndexOf("/")+1);
          
        // Si l'uri n'a pas plusieurs nineaux, elle spécifie déjà un nom de chemin.
        } else {
          fileName = new String(uri);
        }
      
      // Si l'uri est incorrecte, il n'est pas possible de retourner un nom de 
      // fichier
      } else {
        fileName = null;
      }
    }
    
    return fileName;
  }
  
  /**
   * Get the extension of the file located by the path given in parameter. The extension of the file is the
   * set of digits behind the last "." in the path.
   * @param path the path of the file
   * @return the extension of the file.
   */
  public static String getExtension(String path){
    String extension = null;
    
    if (path != null){
      
      if (path.contains(".") && (path.lastIndexOf(".") < path.length() - 1)){
	extension = path.substring(path.lastIndexOf(".")+1);
      }
    }
    
    return extension;
  }
  
  /**
   * Get the directory of the path given in parameter. If the path point a file or a resource, this method return the
   * directory of the file or resource.
   * @param path the path which the directory has to be extracted. 
   * @return the directory of the path, or <code>null</code> if the path is invalid or have no directory.
   */
  public static String getDirectory(String path){
    String directory = null;
    
    String uri      = null;

    if (path == null){
      return null;
    } else {
      
      // Mise sous forme d'uri standard du chemin
      uri = pathToURI(path).toString();
      
      // Si l'uri est correcte
      if (uri != null){
        
        // Si l'uri est un chemin à plusieurs niveaux (présence d'au moins un séparateur)
        // alors le nom de fichier est la dernière partie du chemin.
        if (uri.indexOf("/") != -1){
          directory = uri.substring(0, uri.lastIndexOf("/"));
          
        // Si l'uri n'a pas plusieurs nineaux, elle spécifie déjà un nom de chemin.
        } else {
          directory = new String(uri);
        }
      
      // Si l'uri est incorrecte, il n'est pas possible de retourner un nom de 
      // fichier
      } else {
        directory = null;
      }
    }
    
    return directory;
  }
  
  /**
   * Return the file name pointed by the path without its extension. A file name
   * is the part of tje path folowing the last separator. This method compute the
   * file name of the path using the method {@link #getFileName(String)} and remove
   * the extension of the file name if it exists.
   * @param path the path from which the file name have to be extracted without extension.
   * @return the strict file name
   * @see #getFileName(String)
   */
  public static String getStrictFileName(String path){
    String strictFileName = null;
    
    String tmp            = null; 
    
    // Recuperation du nom de fichier en fonction du path
    tmp = getFileName(path);
    
    if (tmp != null){
      
      // Si le nom de fichier contient une extension, elle est retirée du nom de
      // fichier final
      if (tmp.indexOf(".") != -1){
        
        strictFileName = tmp.substring(0, tmp.lastIndexOf("."));
        
      // Sinon, le nom de fichier obtenu est déjà correct
      } else {
        strictFileName = new String(tmp);
      }
        
    } else {
      strictFileName = null;
    }
    
    return strictFileName;
  }

  /**
   * Return a Java URL representing the path given in parameter. The <code>path</code> given
   * is normalized to match URL prerequisite (simple slash after scheme, ...). If the path
   * is not normalized, the URL may involve errors in resource processing.
   * @param path the path used to build a valid URL
   * @return a valid URL
   */
  public static URL pathToURL(String path){
    String str = new String(path);

    str = pathToURI(path).toString();

    if (!str.startsWith("http")){
      // Remplace les occurences de / contigus par un seul /
      str = str.replaceAll("/{1,}+", "/");
    }

    URL url = null;

    try {
      url = new URL(str);
    } catch (MalformedURLException ex) {
      url = null;
    }
    return url;
  }
  
  
  /**
   * Remove the root of a Path. For example, the method <code>removeRoot("C:\path")</code> return the
   * string <code>"path"</code>. Under linux, the method <code>removeRoot("/path")</code> return the
   * string <code>"path"</code>. If the parameter got a scheme (file://, http://, ...) it is removed.
   * @param path the path with a root to remove.
   * @return the path without a root.
   */
  public static String removeRoot(String path){
    String str = "";
    java.util.regex.Pattern p;
    java.util.regex.Matcher m;
    
    str = PathUtil.pathToURI(path).toString();
    
    // Retrait du scheme
    str = str.replaceFirst("[a-zA-Z]{1,}://", "");
    
    
    // Retrait de la racine sous linux
    if (str.startsWith("/")){
	str = str.substring(1);
    }
    
    // Retrait de la racine sous linux
    p = java.util.regex.Pattern.compile("[a-zA-Z]:.*");
    m = p.matcher(str);
    if(m.matches()){
      str = str.substring(2);
    } 
    
    return str;
  }
}
