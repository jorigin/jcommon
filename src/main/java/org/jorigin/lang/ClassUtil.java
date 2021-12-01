package org.jorigin.lang;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jorigin.Common;


/**
 * This class contains method for managing Java classes.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.3
 */
public class ClassUtil {
  
  /**
   * Check if the given <code>class</code> implements the given <code>interface</code>. 
   * The interface implementation can come for the <code>class</code> hierarchy. This method return <code>false</code> 
   * if the parameter <code>theInterface</code> is a Java Class (ie if {@link java.lang.Class#isInterface() theInterface.isInterface()} return <code>false</code>).<br><br>
   * This method delegate computation to {@link #distanceToInterface(Class, Class) distanceToInterface(Class, Class)} and return as result <code>distanceToInterface(theClass, theInterface) &gt; -1</code>
   * @param theClass the class to check.
   * @param theInterface the interface.
   * @return <code>true</code> if the given class implements the interface anf <code>false</code> otherwise.
   * @see #isSubClass(Class, Class)
   * @see #isSubEntity(Class, Class)
   */
  public static  boolean isImplements(Class<?> theClass, Class<?> theInterface){
    return distanceToInterface(theClass, theInterface) > 0;
  }
  
  /**
   * Check if the class denoted <code>theClass</code> is a sub class of the class denoted by <code>theAncestor</code>. 
   * The class relation can come for the <code>class</code> hierarchy.
   * This method return <code>false</code> 
   * if the parameter <code>theAncestor</code> is a Java Interface (ie if {@link java.lang.Class#isInterface() theInterface.isInterface()} return <code>true</code>)<br><br>
   * This method delegate computation to {@link #distanceToClass(Class, Class) distanceToClass(Class, Class)} and return as result <code>distanceToClass(theClass, theAncestor) &gt; -1</code>
   * @param theClass the class to check.
   * @param theAncestor the potential ancestor.
   * @return <code>true</code> if the given class implements the interface anf <code>false</code> otherwise.
   * @see #isImplements(Class, Class)
   * @see #isSubEntity(Class, Class)
   * @see #distanceToClass(Class, Class)
   */
  public static  boolean isSubClass(Class<?> theClass, Class<?> theAncestor){
    return distanceToClass(theClass, theAncestor) > -1;
  }
 
  /**
   * Check if the given <code>entity</code> is a sub entity of <code>ancestor</code>. This method is a convenience method 
   * that can handle both classes and interfaces. 
   * If the given <code>ancestor</code> is an interface, this method delegates to {@link #isImplements(Class, Class) isImplements(Class, Class)} method the computation. 
   * On the other hand, if the given <code>ancestor</code> is a class, this method delegates to {@link #isSubClass(Class, Class) isSubClass(Class, Class)} method the computation.
   * @param entity the entity to check.
   * @param ancestor the potential ancestor.
   * @return <code>true</code> if the given <code>entity</code> is a sub class or an implementation of the given <code>ancestor</code> and <code>false</code> otherwise.
   * @see #isImplements(Class, Class)
   * @see #isSubClass(Class, Class)
   */
  public static boolean isSubEntity(Class<?> entity, Class<?> ancestor){
    if ((entity != null)&&(ancestor != null)){
      if (ancestor.isInterface()){
        return isImplements(entity, ancestor);
      } else {
        return isSubClass(entity, ancestor);
      }
    }
    
    return false;
  }
  
  /**
   * Compute the hierarchical distance between the given class and the given interface. 
   * The hierarchical distance is equals to <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly implements the interface
   * and is incremented by <code>1</code> for each ancestor between the class and the interface.<br>
   * If the given class do not implements the given interface, the result is <code>-1</code>.
   * @param theClass the class to check.
   * @param theInterface the interface.
   * @return <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly implements the interface
   * and is incremented by <code>1</code> for each ancestor between the class and the interface.
   * @see #isImplements(Class, Class)
   */
  public static int distanceToInterface(Class<?> theClass, Class<?> theInterface){
    
    int tmpDistance = 0;
    
    Class<?>[] interfaces = null;
        
    if ((theClass != null)&&(theInterface != null)&&(theInterface.isInterface())){
    
      // If the given class and the interface are the same.
      if (theInterface.equals(theClass)){
        return 0;
      } else {
        
        // Search if the interface is directly declared on the class itself. In this case,
        // the method return true. If the interface is not declared within the given class, 
        // its interface hierarchy is recursively processed.
        interfaces = theClass.getInterfaces();
        if ((interfaces != null)&&(interfaces.length > 0)){  
          for(int j = 0; j < interfaces.length; j++){
            // The given interface is declared directly by the class
            if (theInterface.equals(interfaces[j])){
              return 1;  
            }
          }
          
          // The interface was not found within the class hierarchy, we now look at the implementation hierarchy.
          for(int j = 0; j < interfaces.length; j++){
              
            tmpDistance = distanceToInterface(interfaces[j], theInterface);
            if (tmpDistance > -1){
              return 1 + tmpDistance;
            }
          }
          
          interfaces = null;
        }
        
        // the interface was not found within the class declared interface,
        // the search is done following the class hierarchy
        tmpDistance = distanceToInterface(theClass.getSuperclass(), theInterface);
        
        if (tmpDistance > -1){
          return 1 + tmpDistance;
        }
      }
    } 
    
    return -1;
  }
  
  /**
   * Compute the hierarchical distance between the given class and an ancestor class. 
   * The hierarchical distance is equals to <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly extends the ancestor
   * and is incremented by <code>1</code> for each ancestor between the class and its ancestor.<br>
   * If the given class is not a sub class of the ancestor, the result is <code>-1</code>.
   * @param theClass the class to check.
   * @param theAncestor the ancestor class.
   * @return <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly extends the ancestor
   * and is incremented by <code>1</code> for each ancestor between the class and its ancestor.
   * @see #isSubClass(Class, Class)
   */
  public static int distanceToClass(Class<?> theClass, Class<?> theAncestor){

    Class<?> superClass = null;
    
    if ((theClass != null)&&(theAncestor != null)&&(!theAncestor.isInterface())){
      
      // If the ancestor and the current class are equals, the distance is 0
      if (theAncestor.equals(theClass)){
        return 0;
      }
      
      // Search if the ancestor is directly declared on the class itself as super class. In this case,
      // the method return true. If the ancestor is not declared within the given class, 
      // its class hierarchy is recursively processed.
      superClass = theClass.getSuperclass();
      
      if (superClass != null){  
        
        if (superClass.equals(theAncestor)){
          return 1;
        } else {
          // the ancestor was not the direct super class,
          // the search is done following the class hierarchy
          int tmp = distanceToClass(theClass.getSuperclass(), theAncestor);
          
          if (tmp > -1){
            return 1 + tmp;
          }
        }
      }
      superClass = null;
    } 
    
    return -1;
  }
  
  /**
   * Compute the hierarchical distance between the given class and an ancestor. 
   * The hierarchical distance is equals to <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly extends or implements the ancestor
   * and is incremented by <code>1</code> for each ancestor between the class and its ancestor.<br>
   * If the given class is not a sub class nor an implementation of the ancestor, the result is <code>-1</code>.
   * @param entity the entity to check.
   * @param ancestor the ancestor.
   * @return The hierarchical distance is equals to <code>0</code> if the two parameters are equals, 
   * to <code>1</code> if the class directly extends or implements the ancestor
   * and is incremented by <code>1</code> for each ancestor between the class and its ancestor.<br>
   * If the given class is not a sub class nor an implementation of the ancestor, the result is <code>-1</code>.
   * @see #distanceToClass(Class, Class)
   * @see #distanceToInterface(Class, Class)
   */
  public static int distanceToAncestor(Class<?> entity, Class<?> ancestor){
    if ((entity != null)&&(ancestor != null)){
      if (ancestor.isInterface()){
        return distanceToInterface(entity, ancestor);
      } else {
        return distanceToClass(entity, ancestor);
      }
    }
    return -1;
  }
  
  /**
   * Search for classes available within the given <code>directory</code>.
   * 
   * @param directory The directory to start with
   * @param pckgname The package name to search for. Will be needed for getting the Class object.
   * @param classes if a file isn't loaded but still is in the directory
   * @throws ClassNotFoundException if an error occurs.
   */
  private static void checkDirectory(File directory, String pckgname, List<Class<?>> classes) throws ClassNotFoundException {
    File tmpDirectory;

    if (directory.exists() && directory.isDirectory()) {
      final String[] files = directory.list();

      for (final String file : files) {
        if (file.endsWith(".class")) {
          try {
            classes.add(Class.forName(pckgname + '.'
                + file.substring(0, file.length() - 6)));
          } catch (final NoClassDefFoundError e) {
            // do nothing. this class hasn't been found by the
            // loader, and we don't care.
          }
        } else if ((tmpDirectory = new File(directory, file))
            .isDirectory()) {
          checkDirectory(tmpDirectory, pckgname + "." + file, classes);
        }
      }
    }
  }

  /**
   * Search for classes available within the Jar accessed by the given <code>connection</code>.
   * @param connection the connection to the jar
   * @param pckgname the package name to search for
   * @param classes the current ArrayList of all classes. This method will simply add new classes.
   * @throws ClassNotFoundException if a file isn't loaded but still is in the jar file
   * @throws IOException if it can't correctly read from the jar file.
   */
  private static void checkJarFile(JarURLConnection connection, String pckgname, List<Class<?>> classes)
      throws ClassNotFoundException, IOException {
    final JarFile jarFile = connection.getJarFile();
    final Enumeration<JarEntry> entries = jarFile.entries();
    String name;

    for (JarEntry jarEntry = null; entries.hasMoreElements()
        && ((jarEntry = entries.nextElement()) != null);) {
      name = jarEntry.getName();

      if (name.contains(".class")) {
        name = name.substring(0, name.length() - 6).replace('/', '.');

        if (name.contains(pckgname)) {
          classes.add(Class.forName(name));
        }
      }
    }
  }

  /**
   * List all the classes in the specified package as determined by the context class loader.
   * @param pckgname the package name to search
   * @return a list of classes that exist within that package
   * @throws ClassNotFoundException if something went wrong
   */
  public static List<Class<?>> listClasses(String pckgname) throws ClassNotFoundException {
    final List<Class<?>> classes = new ArrayList<Class<?>>();

    try {
      final ClassLoader cld = Thread.currentThread().getContextClassLoader();

      if (cld == null)
        throw new ClassNotFoundException("Can't get class loader.");

      final Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
      URLConnection connection;

      for (URL url = null; resources.hasMoreElements()
          && ((url = resources.nextElement()) != null);) {
        try {
          connection = url.openConnection();

          if (connection instanceof JarURLConnection) {
            checkJarFile((JarURLConnection) connection, pckgname,
                classes);
          } else if (connection instanceof URLConnection) {
            try {
              checkDirectory(
                  new File(URLDecoder.decode(url.getPath(),
                      "UTF-8")), pckgname, classes);
            } catch (final UnsupportedEncodingException ex) {
              throw new ClassNotFoundException(
                  pckgname
                  + " does not appear to be a valid package (Unsupported encoding)",
                  ex);
            }
          } else
            throw new ClassNotFoundException(pckgname + " ("
                + url.getPath()
                + ") does not appear to be a valid package");
        } catch (final IOException ioex) {
          throw new ClassNotFoundException(
              "IOException was thrown when trying to get all resources for "
                  + pckgname, ioex);
        }
      }
    } catch (final NullPointerException ex) {
      throw new ClassNotFoundException(
          pckgname
          + " does not appear to be a valid package (Null pointer exception)",
          ex);
    } catch (final IOException ioex) {
      throw new ClassNotFoundException(
          "IOException was thrown when trying to get all resources for "
              + pckgname, ioex);
    }

    return classes;
  }

}
