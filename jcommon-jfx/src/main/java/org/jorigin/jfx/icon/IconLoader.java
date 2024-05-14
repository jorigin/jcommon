package org.jorigin.jfx.icon;

import java.lang.invoke.MethodHandles;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jorigin.io.IOStreamUtil;
import org.jorigin.lang.PathUtil;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

/**
 * A class dedicated to JavaFX icon loading.
 * @author Julien SEINTURIER, IVM Technologies / j.seinturier@ivm-technologies.com
 *
 */
public final class IconLoader {

	/** The logger to use */
	private static Logger logger = Logger.getLogger(IconLoader.class.getName());
	
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
	 * This flag identifies a zip type for the resource path.
	 */
	public static int PATH_TYPE_EMBEDDED_ZIP = 4;

	private static String iconDirectory = "/";

	private static int pathType = 0;

	/**
	 * Get an {@link Image icon} from the given <code>path</code>. If the icon <code>path</code> is relative, 
	 * a complete path is created by concatenating the {@link #getIconDirectory() icon root path} and the given one. 
	 * If the icon <code>path</code> is absolute, the icon is directly loaded.
	 * @param path the path to the icon. Can be relative or absolute.
	 * @return the loaded icon or <code>null</code> if no icon was found.
	 */
	public static Image getIcon(String path){
		Image image = null;

		// The given path is absolute
		if (PathUtil.isAbsolutePath(path)) {

			String processedPath = PathUtil.URIToPath(path);

			// Direct load from filesystem
			try {
				image = new Image(processedPath);
				if (image.getWidth() < 1){
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
			Class<?> c = MethodHandles.lookup().lookupClass();
			URL url = c.getResource(path);

			if (url == null) {
				logger.log(Level.WARNING, "Icon "+path+" does not match a resource.");
				return null;
			}

			try {
				image = new Image(url.toExternalForm());
				if (image.getWidth() < 1){
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
						URL inputURL = new URI(inputFile).toURL();

						JarURLConnection conn = (JarURLConnection)inputURL.openConnection();

						image = new Image(conn.getInputStream());

						if (image.getWidth() < 1){
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

					String processedPath = iconDirectory+path;

					try {

						image = new Image(IOStreamUtil.getInputStream(processedPath));

						if (image.getWidth() < 1){
							image = null;
							logger.log(Level.WARNING, "Icon "+processedPath+" is not a valid image file.");
						}
					} catch (Exception ex) {
						//logger.log(Level.WARNING, "Icon "+iconDirectory+"/"+processedPath+" cannot be loaded: "+ex.getMessage(), ex);
						image = null;
					}

					if (image == null) {
						// Load from the class loader resources
						Class<?> c = MethodHandles.lookup().lookupClass();
						URL url = c.getResource(iconDirectory+path);

						if (url == null) {
							logger.log(Level.WARNING, "Icon "+path+" does not match a resource.");
							return null;
						}

						try {
							image = new Image(url.toExternalForm());
							if (image.getWidth() < 1){
								image = null;
								logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" is not a valid image file.");
							}
						} catch (Exception ex) {
							logger.log(Level.WARNING, "Icon "+url.toExternalForm()+" cannot be loaded: "+ex.getMessage(), ex);
							image = null;
						}
					}
				}
			} else {

				String processedPath = PathUtil.URIToPath(path);

				try {
					image = new Image(IOStreamUtil.getInputStream(processedPath));

					if (image.getWidth() < 1){
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
				Class<?> c = MethodHandles.lookup().lookupClass();
				URL url = c.getResource(path);

				if (url == null) {
					logger.log(Level.WARNING, "Icon "+path+" does not match a resource.");
					return null;
				}

				try {
					image = new Image(url.toExternalForm());
					if (image.getWidth() < 1){
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
	 * Get an icon as an {@link ImageView image view} and scale it to the desired size given in millimeter (mm)
	 * @param name the name of the icon to load
	 * @param dimension the dimension in millimeter (mm) of the icon
	 * @return a scaled image view that represents the desired icon
	 * @see #getScaledIcon(String, double, double)
	 */
	public static ImageView getScaledIconView(String name, Dimension2D dimension){
		Image icon = getIcon(name);

		if (icon != null){

			// Get the DPI in point / mm
			double dpi        = Screen.getPrimary().getDpi()/25.4d;

			ImageView imageView = new ImageView(icon);

			imageView.setPreserveRatio(false);
			imageView.setSmooth(true);
			imageView.setFitWidth(dimension.getWidth()*dpi);
			imageView.setFitHeight(dimension.getHeight()*dpi);
			
			return imageView;
		}

		return null;
	}
	
	/**
	 * Get an icon and scale it to the desired size given in millimeter (mm)
	 * @param name the name of the icon to load
	 * @param dimension the dimension in millimeter (mm) of the icon
	 * @return a scaled image that represents the desired icon
	 * @see #getScaledIcon(String, double, double)
	 */
	public static Image getScaledIcon(String name, Dimension2D dimension){
		Image icon = getIcon(name);

		if (icon != null){

			// Get the DPI in point / mm
			double dpi        = Screen.getPrimary().getDpi()/25.4d;

			ImageView imageView = new ImageView(icon);

			imageView.setPreserveRatio(false);
			imageView.setFitWidth(dimension.getWidth()*dpi);
			imageView.setFitHeight(dimension.getHeight()*dpi);

			imageView.setSmooth(true);

			return imageView.snapshot(null, null);
		}

		return null;
	}

	/**
	 * Get an icon and scale it to the desired size given in millimeters.
	 * @param name the name of the icon to load.
	 * @param width the expected width in millimeter (mm)
	 * @param height the expected height in millimeter (mm)
	 * @return a scaled image that represents the desired icon
	 * @see #getScaledIcon(String, Dimension2D)
	 */
	public static Image getScaledIcon(String name, double width, double height){
		return getScaledIcon(name, new Dimension2D(width, height));
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
