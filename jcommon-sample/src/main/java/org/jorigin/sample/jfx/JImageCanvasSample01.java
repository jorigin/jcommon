package org.jorigin.sample.jfx;

import org.jorigin.Common;
import org.jorigin.jfx.JImageCanvas;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * A {@link JImageCanvas} sample.
 * @author Julien Seinturier - Universit&eacute; de Toulon / LIS umr CNRS 7020 - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 */
public class JImageCanvasSample01 extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {

		
		// Take a screenshot
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		
		// load image
		Image image = new Image(getClass().getResource("/image/landscape/mountain-lake-320x200.jpg").toExternalForm());
		
		// Create display
		JImageCanvas canvas = new JImageCanvas(image);

        canvas.setBackgroundPaint(Color.DARKGRAY);
		
        canvas.setAutoFit(false);

		BorderPane centerPane = new BorderPane();
		centerPane.setMinSize(0.0d, 0.0d);

		centerPane.setCenter(canvas);
		
		Scene scene = new Scene(centerPane);

		primaryStage.setWidth(screenBounds.getWidth() / 2.0d);
		primaryStage.setHeight(screenBounds.getHeight() / 2.0d);
		primaryStage.setTitle("JImageCanvas Sample");

		primaryStage.setScene(scene);

		primaryStage.show();
	}

	/**
	 * The main method.
	 * @param args the program arguments
	 */
	public static void main(String[] args) {
		Common.init();
		launch(args);
	}

}
