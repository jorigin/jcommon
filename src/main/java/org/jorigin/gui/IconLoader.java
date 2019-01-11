package org.jorigin.gui;

import static org.jorigin.Common.logger;

import java.awt.Dimension;
import java.awt.Image;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import org.jorigin.Common;
import org.jorigin.lang.PathUtil;

/**
 * A class dedicated to the load of icons. This class enable to seek icons from a given directory and enable to scale icons in metric units.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.3
 */
public class IconLoader {

  private static String resourcesPath = "";
  
  /**
   * Get an {@link javax.swing.ImageIcon icon} from the given <code>path</code>. If the icon <code>path</code> is relative, 
   * a complete path is created by concatenating the {@link #getIconPath() icon root path} and the given one. 
   * If the icon <code>path</code> is absolute, the icon is directly loaded.
   * @param path the path to the icon. Can be relative or absolute.
   * @return the loaded icon or <code>null</code> if no icon was found.
   */
  public static ImageIcon getIcon(String path){
    ImageIcon i = null;
    
    if (!PathUtil.isAbsolutePath(path)){
      try {
        i = new ImageIcon(PathUtil.URIToPath(resourcesPath+"/"+path));
        if (i.getIconWidth() < 1){
          i = null;
        }
      } catch (Exception ex) {
        logger.log(Level.WARNING, "Icon "+resourcesPath+"/"+path+" cannot be loaded: "+ex.getMessage(), ex);
        i = null;
      }
      
      if (i == null){
        try {
          i = new ImageIcon(IconLoader.class.getResource(path));
          if (i.getIconWidth() < 1){
            i = null;
          }
        } catch (Exception e) {
          logger.log(Level.WARNING, "Icon "+path+" cannot be loaded: "+e.getMessage(), e);
          i = null;
        }
      } 
    } else {
      try {
        i = new ImageIcon(PathUtil.URIToPath(path));
        if (i.getIconWidth() < 1){
          i = null;
        }
      } catch (Exception ex) {
        logger.log(Level.WARNING, "Icon "+path+" cannot be loaded: "+ex.getMessage(), ex);
        i = null;
      }
    }
    
    return i;
  }

  /**
   * Get an {@link java.awt.Image image} from the given <code>path</code>. If the image <code>path</code> is relative, 
   * a complete path is created by concatenating the {@link #getIconPath() image root path} and the given one. 
   * If the image <code>path</code> is absolute, the image is directly loaded.
   * @param path the path to the image. Can be relative or absolute.
   * @return the loaded image or <code>null</code> if no image was found.
   */
  public static Image getImage(String path){
    ImageIcon i = getIcon(path);
    
    if (i != null){
      return i.getImage();
    } else {
      return null;
    }
  }
  
  /**
   * Get an icon and scale it to the desired size given in millimeters.
   * @param name the name of the icon to load.
   * @param dimension the dimension in millimeters of the icon.
   * @return a scaled image that represents the desired icon.
   */
  public static ImageIcon getScaledIcon(String name, Dimension dimension){
    ImageIcon icon = getIcon(name);
    
    int dpi        = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

    if (icon != null){
      icon.setImage(icon.getImage().getScaledInstance((int)(dimension.getWidth()*dpi/25.4), (int)(dimension.getHeight()*dpi/25.4), Image.SCALE_SMOOTH));
    }
 
    return icon;
  }
  
  /**
   * Get the path to use as root for the icon search.
   * @return the path to use as root for the icon search.
   * @see #setIconPath(String)
   */
  public static String getIconPath(){
    return resourcesPath;
  }
  
  /**
   * Set  the path to use as root for the icon search.
   * @param path the path to use as root for the icon search.
   * @see #getIconPath()
   */
  public static void setIconPath(String path){
    resourcesPath = path;
  }
}
