package org.jorigin.sample.jfx.thumbnail;

import java.util.List;

import org.jorigin.jfx.thumbnail.JThumbnail;
import org.jorigin.jfx.thumbnail.JThumbnailPane;
import org.jorigin.jfx.thumbnail.JThumbnailStyle;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A sample class that shows {@link JThumbnail thumbnails} and {@link JThumbnailPane thumbnail pane} capabilities.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 *
 */
public class JThumbnailPaneSampleApplication extends Application{

	/**
	 * The thumbnail image width.
	 * @see #imageHeight
	 */
	private int imageWidth = 100;
	
	/**
	 * The thumbnail image height.
	 * @see #imageWidth
	 */
	private int imageHeight = 100;
	
	/**
	 * The number of thumbnails to show.
	 * <p>
	 * This is used to create a sample with a lot of thumbnails.
	 * </p>
	 */
	private int thumbnailsCount = 15;

	/**
	 * Default constructor.
	 */
	public JThumbnailPaneSampleApplication() {
		super();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		JThumbnailStyle parameters = new JThumbnailStyle();
		
		parameters.setThumbnailWidth(200);
		parameters.setThumbnailHeight(170);

		parameters.setThumbnailHGap(5);
		parameters.setThumbnailVGap(5);
		
		parameters.setThumbnailNameVisible(true);

		// Thumbnail style when focused
		parameters.setThumbnailFocusedBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		// Thumbnail style when selected
		parameters.setThumbnailSelectedImageBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		//parameters.setThumbnailSelectedNameBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		parameters.setThumbnailSelectedBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

		parameters.setThumbnailPaneBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		JThumbnailPane<String> thumbnailPane = new JThumbnailPane<String>(parameters);
		
		thumbnailPane.addThumbnailActivationHandler((p, t) -> processActivation(p, t));
		
		thumbnailPane.addThumbnailSelectionHandler((p, t) -> processSelection(p, t));
		
		Scene scene = new Scene(thumbnailPane, 1000, 600);
		primaryStage.setScene(scene);
		    
		// Start showing the UI before taking time to load any images
		primaryStage.show();

		// Load images in the background so the UI stays responsive.
		for(int i = 0; i < this.thumbnailsCount; i++) {
			thumbnailPane.addThumbnail(new JThumbnail<String>("name "+i, ""+i, createImage(this.imageWidth, this.imageHeight), parameters));
		}
		
		System.out.println("Applying filter (keeping thumbnails with pair names");
		thumbnailPane.setFilter((i) -> {return Double.parseDouble(i)%2 == 0;});
		
		List<String> items = thumbnailPane.getItems();
		
		System.out.println("Items:");
		for(String s : items) {
			System.out.println(" - "+s);
		}
	}

	/**
	 * Processa thumbnail activation.
	 * @param pane the thumbnail pane
	 * @param thumbnail the activated thumbnail
	 */
	private void processActivation(JThumbnailPane<String> pane, JThumbnail<String> thumbnail) {
		System.out.println("Activated "+thumbnail.getThumbnailContent());
	}
	
	/**
	 * Process a thumbnail selection
	 * @param pane the thumbnail pane
	 * @param thumbnails the thumbnails selected
	 */
    private void processSelection(JThumbnailPane<String> pane, List<JThumbnail<String>> thumbnails) {
    	if ((thumbnails == null) || (thumbnails.size() < 1)){
    		System.out.println("Selection empty.");
    	} else {
    		System.out.print("Selected "+thumbnails.get(0).getThumbnailContent());
    		for(int i = 1; i < thumbnails.size(); i++) {
    			System.out.print(", "+thumbnails.get(i).getThumbnailContent());
    		}
    		System.out.println();
    	}
    }
	
    /**
	 * Create a random image.
	 * @param width the image width
	 * @param height the image height
	 * @return the created image
	 */
    private Image createImage(int width, int height) {
    	
    	WritableImage image = new WritableImage(width, height);
    	
    	PixelWriter writer = image.getPixelWriter();
    	
    	double r = Math.random();
    	double g = Math.random();
    	double b = Math.random();
    	
    	for(int x = 0; x < width ; x++){
            for(int y = 0; y < height; y++){
            	writer.setColor(x, y, new Color(r, g, b, 1.0));
            }
        }

    	return image;
    }
}
