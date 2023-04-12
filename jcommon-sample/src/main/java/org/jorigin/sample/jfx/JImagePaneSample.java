package org.jorigin.sample.jfx;

import org.jorigin.jfx.JImageCanvas;
import org.jorigin.jfx.JImageFeature;
import org.jorigin.jfx.JImageFeatureLayer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * A {@link JImageCanvas} sample.
 * @author Julien Seinturier - Universit&eacute; de Toulon / LIS umr CNRS 7020 - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 *
 */
public class JImagePaneSample extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {

		
		// Take a screenshot
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		Robot robot = new Robot();
		WritableImage img = robot.getScreenCapture(null, screenBounds);

		// Create display
		JImageCanvas canvas = new JImageCanvas(img);

        canvas.setBackgroundPaint(Color.DARKGRAY);
		
		BorderPane centerPane = new BorderPane();
		centerPane.setMinSize(0.0d, 0.0d);
		
		HBox bottomPane = new HBox();
		//bottomPane.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Label cursorPositionLB = new Label("Cursor position: -");
		Label imageSizeLB = new Label("Image size: "+img.getWidth()+" x "+img.getHeight()+" px");
		
		bottomPane.getChildren().add(imageSizeLB);
		bottomPane.getChildren().add(new Separator());
		bottomPane.getChildren().add(cursorPositionLB);
		
		centerPane.setCenter(canvas);
		centerPane.setBottom(bottomPane);
		
		Scene scene = new Scene(centerPane);

		primaryStage.setWidth(512);
		primaryStage.setHeight(512);
		primaryStage.setTitle("JImageCanvas Sample");

		primaryStage.setScene(scene);

		canvas.getCursorPositionProperty().addListener((ChangeListener<Point2D>)(observable, oldValue, newValue) ->{
			Point2D imagePoint = canvas.getImageCoordinate(newValue);
			
			if (imagePoint != null) {
				cursorPositionLB.setText("Cursor position: "+String.format("%8.3f", newValue.getX())+", "+String.format("%8.3f", newValue.getY())+" (view) / "+String.format("%8.3f", imagePoint.getX())+", "+String.format("%8.3f", imagePoint.getY())+" (image)");
			} else {
				cursorPositionLB.setText("Cursor position: "+String.format("%8.3f", newValue.getX())+", "+String.format("%8.3f", newValue.getY())+" (view) / Out of image.");
			}
			
			
		});
		
		primaryStage.show();

		System.out.println("Adding layers");
		
		// Create grid layer
		JImageFeatureLayer gridLayer = new JImageFeatureLayer("Grid");
		for(int line = 50; line < screenBounds.getWidth() - 50; line = line + 50) {
			for(int column = 50; column < screenBounds.getHeight() - 50; column = column + 50) {
				gridLayer.addImageFeature(new SquareImageFeature(line, column, Color.color(Math.random(), Math.random(), Math.random())));
			}
		}

		gridLayer.setStateDisplaying(true);

		canvas.addImageFeatureLayer(gridLayer);
		
		canvas.setSelectionMode(JImageCanvas.MODE_SELECTION_RECTANGLE);
	}

	/**
	 * The main method.
	 * @param args the program arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

class SquareImageFeature implements JImageFeature {

	private double line = 0;

	private double column = 0;

	private Color color = Color.AZURE; 

	private boolean selectable = true;

	private boolean selected = false;

	private boolean displayable = true;

	private boolean displaying = true;

	private Object userData = null;

	private JImageFeatureLayer layer = null;

	@Override
	public boolean isStateSelectable() {
		return selectable;
	}

	@Override
	public void setStateSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public boolean isStateSelected() {
		return selected;
	}

	@Override
	public void setStateSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isStateDisplaying() {
		return displaying;
	}

	@Override
	public void setStateDisplaying(boolean displaying) {
		this.displaying = displaying;
	}

	@Override
	public boolean isStateDisplayable() {
		return displayable;
	}

	@Override
	public void setStateDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

	@Override
	public void draw(GraphicsContext g2d, Affine transform) {
		if (g2d != null) {
			if (transform != null) {
				Paint paint = g2d.getFill();
				g2d.setFill(color);
				g2d.fillRect(line - 10, column - 10, 20, 20);
				g2d.setFill(paint);
			}
		}
	}

	@Override
	public boolean contains(double x, double y) {
		return (x >= line - 10) && (x <= line +10) && (y >= column - 10) && (y <= column + 10);
	}

	@Override
	public boolean contains(Shape s) {
		return false;
	}

	@Override
	public boolean intersects(Shape s) {
		return false;
	}

	@Override
	public boolean inside(Shape s) {
		return false;
	}

	@Override
	public Object getUserData() {
		return userData;
	}

	@Override
	public void setUserData(Object data) {
		this.userData = data;
	}

	@Override
	public JImageFeatureLayer getImageFeatureLayer() {
		return layer;
	}

	@Override
	public void setImageFeatureLayer(JImageFeatureLayer layer) {
		this.layer = layer;
	}

	/**
	 * Create a new simple image feature.
	 * @param line the line of the feature
	 * @param column the column of the feature
	 * @param color the color of the feature
	 */
	public SquareImageFeature(double line, double column, Color color) {
		this.line   = line;
		this.column = column;
		this.color  = color;
	}

}
