package org.jorigin.io;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.jorigin.Common;
import org.jorigin.lang.PathUtil;



/**
 * A file management API. This Class provide simple and efficient file management methods.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 *
 */
public class FileUtil {

 
  /**
   * Return the file or directory size in byte
   * @param path the file or directory to check
   * @return the complete size in bytes of the file or directory.
   */
  public static long size(File path) {
    long result = 0;
    // Verification de l'existence du repertoire
    if (path.exists()) {
      // Liste des fichiers (et sous repertoire)
      File[] files = path.listFiles();
      // si on a un fichier simple, on prends en compte sa taille
      if (files == null) {
        return path.length();
      }
      for (int i = 0; i < files.length; i++) {
        // Utilisation de la recursivite pour le calcul de taille
        if (files[i].isDirectory()) {
          result = result + size(files[i]);
        } else {
          result = result + files[i].length();
        }
      }
    }
    return (result);
  }

  /**
   * Delete recursively a directory and its content. After cleaning all the contents, the directory itself is deleted. 
   * @param path path to the directory (or file) you want to delete
   * @return <code>true</code> if the delete was successfull and <code>false</code> if the delete was unable to erase all the directory.
   * @see #cleanDirectory(File)
   */
  static public boolean deleteDirectory(File path) {
    boolean result = true;
    // Verification de l'existence du repertoire
    if (path.exists()) {
      // Liste des fichiers (et sous repertoire)
      File[] files = path.listFiles();
      // si on a un fichier simple, on l'efface
      if (files == null) {
        return path.delete();
      }
      for (int i = 0; i < files.length; i++) {
        // Utilisation de la recursivite pour l'effacage
        if (files[i].isDirectory()) {
          result &= !deleteDirectory(files[i]);
        }
        result &= files[i].delete();
      }
    }
    // effacage du repertoire lui meme
    result &= path.delete();
    return (result);
  }

  /**
   * Delete recursively the content of a directory. Only the directory content is deleted, the directory itself is not deleted.
   * @param path path to the directory (or file) you want to clean
   * @return <code>true</code> if the delete was successfull and <code>false</code> if the delete was unable to erase all the directory
   * @see #deleteDirectory(File)
   */
  static public boolean cleanDirectory(File path) {
    boolean result = true;
    // Verification de l'existence du repertoire
    if (path.exists()) {
      // Liste des fichiers (et sous repertoire)
      File[] files = path.listFiles();
      // si on a un fichier simple, on l'efface
      if (files == null) {
        return path.delete();
      }
      for (int i = 0; i < files.length; i++) {
        // Utilisation de la recursivite pour l'effacage
        if (files[i].isDirectory()) {
          result &= !deleteDirectory(files[i]);
        }
        result &= files[i].delete();
      }
    }
    
    return (result);
  }

  
  /**
   * Simple copy of a source file to a destination file
   * @param source the path of the source file
   * @param destination the path of the destination file
   * @return <code>true</code> if the copy was successfull and <code>false</code> if not
   */
  public static boolean copy(File source, File destination) {
    boolean result = false;
    /* Declaration des flux */
    java.io.FileInputStream sourceFile = null;
    java.io.FileOutputStream destinationFile = null;
    try {
      // Cretion du fichier
      destination.createNewFile();
      // Ouverture des flux
      sourceFile = new java.io.FileInputStream(source);
      destinationFile = new java.io.FileOutputStream(destination);
      // Lecture par segment de 0.5M
      byte buffer[] = new byte[1024];
      int nbLecture;
      while ((nbLecture = sourceFile.read(buffer)) != -1) {
        destinationFile.write(buffer, 0, nbLecture);
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
        sourceFile.close();
      } catch (Exception e) {
      }
      try {
        destinationFile.close();
      } catch (Exception e) {
      }
    }
    return (result);
  }

  /**
   * Copying recursively a directory to another. If the destination directory does not exist, it is created.
   * @param source the source directory (or file to copy)
   * @param destination the destination directory.
   * @return <code>true</code> if the copy was successfull, <code>false</code> if the copy was unsuccessfull
   */
  public static boolean copyDirectory(File source, File destination) {
    boolean result = true;
    // Premiere verification: le source existe t'il
    if (!source.exists()) {
      return false;
    }
    // Cree le repertoire de destination s'il n'existe pas
    if ((destination.mkdirs() == false) && (!destination.isDirectory())) {
      return false;
    }
    // Liste les fichiers du repertoire
    File[] files = source.listFiles();
    // Parcours de la liste de fichiers et copie recursive
    for (int i = 0; i < files.length; i++) {
      // La destination change en fonction du fichier copie
      File newDestination = new File(destination.getPath() + File.separator
          + files[i].getName());
      if (files[i].isDirectory()) {
        result &= copyDirectory(files[i], newDestination);
      } else {
        result &= copy(files[i], newDestination);
      }
    }
    // en cas de probleme, toute la copie doit etre annulee
    //if (result == false)
    //  deleteDirectory(destination);
    return result;
  }

  /**
   * List a directory and select files that are selected by the given {@link java.io.FileFilter file filter}
   * @param dir the directory to list.
   * @param filter the {@link java.io.FileFilter file filter} that accept or not listed files.
   * @param recurse is set to <code>true</code> if the listing process have to enter subdirectory, <code>false</code> otherwise.
   * @return the files that are selected by the given {@link java.io.FileFilter file filter}
   */
  public static List<File> list(File dir, FileFilter filter, boolean recurse) {
    LinkedList<File> list = null;
    // Premiere verification: le source existe t'il
    if (dir == null){
      return null;
    }
    
    if (!dir.exists()) {
      return null;
    } else {
      list = new LinkedList<File>();
    }
    // Liste les fichiers du repertoire
    File[] files = dir.listFiles();
    // Parcours de la liste de fichiers et copie recursive
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory() && recurse) {
        list.addAll(list(files[i], filter, recurse));
      } else {
        if ((filter == null) || (filter.accept(files[i]))) {
          list.add(files[i]);
        }
      }
    }
    return list;
  }
  
  /**
   * List a directory and select files that are selected by the given {@link java.io.FileFilter file filter}
   * @param dir the directory to list.
   * @param filter the {@link java.io.FileFilter file filter} that accept or not listed files.
   * @param recurse is set to <code>true</code> if the listing process have to enter subdirectory, <code>false</code> otherwise.
   * @return the pathes of the files that are selected by the given {@link java.io.FileFilter file filter}
   */
  public static List<String> listPathes(File dir, FileFilter filter, boolean recurse) {
    LinkedList<String> list = null;
    // Premiere verification: le source existe t'il
    if (dir == null){
      return null;
    }
    
    if (!dir.exists()) {
      return null;
    } else {
      list = new LinkedList<String>();
    }
    // Liste les fichiers du repertoire
    File[] files = dir.listFiles();
    // Parcours de la liste de fichiers et copie recursive
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory() && recurse) {
        list.addAll(listPathes(files[i], filter, recurse));
      } else {
        if ((filter == null) || (filter.accept(files[i]))) {
          list.add(PathUtil.URIToPath(files[i].getPath()));
        }
      }
    }
    return list;
  }

  
  /**
   * List recursively a directory and select files that match the given 
   * <a href="http://docs.oracle.com/javase/tutorial/essential/regex/">regular expression.</a>
   * @param source the directory to list.
   * @param regex the regular expression that the tested file path have to match.
   * @param recurse is set to <code>true</code> if the listing process have to enter subdirectory, <code>false</code> otherwise.
   * @return the list of files contained within the given directory and from witch path are matched by te given regular expression.
   */
  public static List<File> list(File source, final String regex, boolean recurse) {
    FileFilter filter = new FileFilter(){
      
      @Override
      public boolean accept(File pathname) {
        if (pathname != null){
          
          String path = pathname.getPath();
          if (path != null){
            return path.matches(regex);
          } else {
            return false;
          }
          
        } else {
          return false;
        }
      }};
      
    return list(source, filter, recurse);
  }
}
