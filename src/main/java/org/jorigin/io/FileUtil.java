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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;


/**
 * A file management API. This Class provide simple and efficient file management methods.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
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
   * Delete recursively a directory. 
   * @param path path to the directory (or file) you want to delete
   * @return <code>true</code> if the delete was successfull and <code>false</code> if the delete was unable to erase all the directory
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
   * List recursively a directory and its sub-directories.
   * @param source the source directory (or file to copy).
   * @param filter a {@link java.io.FileFilter filter} used for accepting files within the list.
   * @return the list of files presents in the directory.
   */
  public static ArrayList<File> listFiles(File source, FileFilter filter) {
    ArrayList<File> list = null;
    // Premiere verification: le source existe t'il
    if (!source.exists()) {
      return null;
    } else {
      list = new ArrayList<File>();
    }
    // Liste les fichiers du repertoire
    File[] files = source.listFiles();
    // Parcours de la liste de fichiers et copie recursive
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        list.addAll(listFiles(files[i], filter));
      } else {
        if ((filter == null) || (filter.accept(files[i]))) {
          list.add(files[i]);
        }
      }
    }
    return list;
  }

}
