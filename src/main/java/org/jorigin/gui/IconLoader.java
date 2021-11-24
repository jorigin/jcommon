package org.jorigin.gui;

import static org.jorigin.Common.logger;

import java.awt.Dimension;
import java.awt.Image;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import org.jorigin.lang.PathUtil;

/**
 * A class dedicated to SWING icon loading.
 * @author Julien SEINTURIER, IVM Technologies / j.seinturier@ivm-technologies.com
 */
public class IconLoader {

	/**
	 * This flag identifies an unknown type for the resource path.
	 */
	public static int PATH_TYPE_UNKNOWN      = 0;

	/**
	 * This flag identifies an filesystem type (absolute) for the resource path.
	 */
	public static int PATH_TYPE_FILESYSTEM   = 1;

	/**
	 * This flag identifies an URL type (HTTP, HTTPS, FTP, SFTP) for the resource path.
	 */
	public static int PATH_TYPE_URL          = 2;

	/**
	 * This flag identifies a jar type for the resource path.
	 */
	public static int PATH_TYPE_EMBEDDED_JAR = 4;

	/**
	 * This flag identifies a zup type for the resource path.
	 */
	public static int PATH_TYPE_EMBEDDED_ZIP = 4;

	private static String iconDirectory = "";

	private static int pathType = 0;

	/**
	 * Get an {@link ImageIcon icon} from the given <code>path</code>. If the icon <code>path</code> is relative, 
	 * a complete path is created by concatenating the {@link #getIconDirectory() icon root path} and the given one. 
	 * If the icon <code>path</code> is absolute, the icon is directly loaded.
	 * @param path the path to the icon. Can be relative or absolute.
	 * @return the loaded icon or <code>null</code> if no icon was found.
	 */
	public static ImageIcon getIcon(String path){
		ImageIcon image = null;

		// The given path is absolute
		if (PathUtil.isAbsolutePath(path)) {

			String processedPath = PathUtil.URIToPath(path);

			// Direct load from filesystem
			try {
				image = new ImageIcon(processedPath);
				if (image.getIconWidth() < 1){
					image = null;
					logger.log(Level.WARNING, "Icon "+processedPath+" is not a valid image file.");
				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Icon "+processedPath+" cannot be loaded: "+ex.getMessage(), ex);
				image = null;
			}

			if (image != null) {
				return image;
			}

			// Load from the class loader resources
			ClassLoader urlClassLoader = MethodHandles.lookup().lookupClass().getClassLoader();
			URL url = urlClassLoader.getResource(path);

			try {
				image = new ImageIcon(url.toExternalForm());
				if (image.getIconWidth() < 1){
					image = null;
					logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" is not a valid image file.");
				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" cannot be loaded: "+ex.getMessage(), ex);
				image = null;
			}

		} else {
			// The icon directory is specified.
			if ((iconDirectory != null) && (!iconDirectory.isEmpty())) {

				if (pathType == PATH_TYPE_EMBEDDED_JAR) {

					String inputFile = iconDirectory+path;

					try {
						URL inputURL = new URL(inputFile);

						image = new ImageIcon(inputURL);

						if (image.getIconWidth() < 1){
							image = null;
							logger.log(Level.WARNING, "Icon "+inputURL.toExternalForm()+" is not a valid image file.");
						}
					} catch (Exception ex) {
						logger.log(Level.WARNING, "Icon "+inputFile+" cannot be loaded: "+ex.getMessage(), ex);
						image = null;
					}


				} else if (pathType == PATH_TYPE_EMBEDDED_ZIP) {
					logger.log(Level.WARNING, "Icon "+path+" load from ZIP file is not handled.");
					return null;
				} else {

					String processedPath = PathUtil.URIToPath(iconDirectory+path);

					try {

						image = new ImageIcon(processedPath);

						if (image.getIconWidth() < 1){
							image = null;
							logger.log(Level.WARNING, "Icon "+processedPath+" is not a valid image file.");
						}
					} catch (Exception ex) {
						logger.log(Level.WARNING, "Icon "+iconDirectory+"/"+processedPath+" cannot be loaded: "+ex.getMessage(), ex);
						image = null;
					}
				}
			} else {

				String processedPath = PathUtil.URIToPath(path);

				try {
					image = new ImageIcon(processedPath);

					if (image.getIconWidth() < 1){
						image = null;
						logger.log(Level.WARNING, "Icon "+processedPath+" is not a valid image file.");
					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, "Icon "+processedPath+" cannot be loaded: "+ex.getMessage(), ex);
					image = null;
				}

				if (image != null) {
					return image;
				}

				// Load from the class loader resources
				ClassLoader urlClassLoader = MethodHandles.lookup().lookupClass().getClassLoader();
				URL url = urlClassLoader.getResource(path);

				try {
					image = new ImageIcon(url.toExternalForm());
					if (image.getIconWidth() < 1){
						image = null;
						logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" is not a valid image file.");
					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" cannot be loaded: "+ex.getMessage(), ex);
					image = null;
				}
			}
		}

		return image;
	}

	/**
	 * Get an {@link Image image} from the given <code>path</code>. If the image <code>path</code> is relative, 
	 * a complete path is created by concatenating the {@link #getIconDirectory() icon root path} and the given one. 
	 * If the image <code>path</code> is absolute, the image is directly loaded.
	 * @param path the path to the image. Can be relative or absolute.
	 * @return the loaded image or <code>null</code> if no image was found.
	 */
	public static Image getImage(String path) {
		ImageIcon icon = getIcon(path);
		
		if (icon != null) {
			return icon.getImage();
		}
		
		return null;
	}
	
	/**
	 * Get an icon and scale it to the desired size given in millimeters.
	 * @param name the name of the icon to load.
	 * @param dimension the dimension in millimeters of the icon.
	 * @return a scaled image that represents the desired icon.
	 */
	public static ImageIcon getScaledIcon(String name, Dimension dimension){
		return getScaledIcon(name, dimension.getWidth(), dimension.getHeight());
	}

	/**
	 * Get an icon and scale it to the desired size given in millimeters.
	 * @param name the name of the icon to load.
	 * @param width the expected width in millimeter (mm)
	 * @param height the expected height in millimeter (mm)
	 * @return a scaled image that represents the desired icon
	 * @see #getScaledIcon(String, Dimension)
	 */
	public static ImageIcon getScaledIcon(String name, double width, double height){
		ImageIcon icon = getIcon(name);

		int dpi        = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

		if (icon != null){
			icon.setImage(icon.getImage().getScaledInstance((int)(width*dpi/25.4), (int)(height*dpi/25.4), Image.SCALE_SMOOTH));
		}

		return icon;
	}

	/**
	 * Get the directory to use as root for the icon search. 
	 * The icon path can be a system directory, a string that represents an URL or a string that represents an embedded (zip, jar, ...) directory.
	 * @return the path to use as root for the icon search.
	 * @see #setIconDirectory(String)
	 */
	public static String getIconDirectory(){
		return iconDirectory;
	}

	/**
	 * Set  the path to use as root for the icon search.
	 * The icon path can be a system directory, a string that represents an URL or a string that represents an embedded (zip, jar, ...) directory.
	 * @param path the path to use as root for the icon search.
	 * @see #getIconDirectory()
	 */
	public static void setIconDirectory(String path){


		if (path != null) {

			String lowerPath = path.toLowerCase();

			if ((lowerPath.startsWith("file:")) || lowerPath.startsWith("http:") || lowerPath.startsWith("https:") || lowerPath.startsWith("ftp:") || lowerPath.startsWith("sftp:")) {
				pathType = PATH_TYPE_URL;

				if (!path.endsWith("/")) {
					iconDirectory = path+"/";
				} else {
					iconDirectory = path;
				}

			} else if (lowerPath.startsWith("jar:")) {
				pathType = PATH_TYPE_EMBEDDED_JAR;

				if (!path.endsWith("/")) {
					iconDirectory = path+"/";
				} else {
					iconDirectory = path;
				}

			} else if (PathUtil.isAbsolutePath(lowerPath)){
				pathType = PATH_TYPE_FILESYSTEM;

				if (!path.endsWith("/")) {
					iconDirectory = path+"/";
				} else {
					iconDirectory = path;
				}

			} else if (lowerPath.startsWith("zip:")) {
				pathType = PATH_TYPE_EMBEDDED_ZIP;

				if (!path.endsWith("/")) {
					iconDirectory = path+"/";
				} else {
					iconDirectory = path;
				}

			} else {
				pathType = PATH_TYPE_UNKNOWN;
				iconDirectory = path;
			}

			logger.log(Level.INFO, "Using icon path "+path+" ("+pathType+")");

		} else {
			iconDirectory = null;
			logger.log(Level.INFO, "No icon path set "+path);
		}
	}
}
