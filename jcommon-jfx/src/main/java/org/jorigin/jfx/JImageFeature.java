package org.jorigin.jfx;

import org.jorigin.state.HandleDisplay;
import org.jorigin.state.HandleSelection;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;

/**
 * An image feature that can be displayed within a {@link JImageCanvas JImageCanvas}.
 * @author Julien Seinturier - IVM Technologies - http://www.seinturier.fr
 * @version 1.0.0
 */
public interface JImageFeature extends HandleSelection, HandleDisplay{

	/**
	 * Draw this feature on the given {@link GraphicsContext graphic context}. The given graphic context is related to the original image space. 
	 * The transformation applied to the current display is given as <code>transform</code> and can be used to 
	 * avoid rotation or scale for feature that have constant size or orientation (for example text features). 
	 * @param g2d a graphic context expressed within image space.
	 * @param transform the transform applied to the view within the {@link JImageCanvas JImageCanvas}.
	 */
	public void draw(GraphicsContext g2d, Affine transform);

	/**
	 * Check if the point described by the given coordinates (<code>x</code>, <code>y</code>) is inside the feature. 
	 * The coordinates have to be expressed within original image space.
	 * @param x the x coordinate of the point to check.
	 * @param y the y coordinate of the point to check.
	 * @return <code>true</code> if the feature is containing the point and <code>false</code> otherwise.
	 * @see #contains(Shape)
	 * @see #intersects(Shape)
	 * @see JImageFeature#inside(Shape)
	 */
	public boolean contains(double x, double y);

	/**
	 * Check if the {@link Shape shape} given in parameter is inside the feature. 
	 * The shape have to be expressed within original image space.
	 * @param s the {@link Shape shape} to check.
	 * @return <code>true</code> if the feature is containing the shape and <code>false</code> otherwise.
	 * @see #contains(double, double)
	 * @see #intersects(Shape)
	 * @see #inside(Shape)
	 */
	public boolean contains(Shape s);

	/**
	 * Check if the {@link Shape shape} given in parameter is intersecting the feature. 
	 * The shape have to be expressed within original image space.
	 * @param s the {@link Shape shape} to check.
	 * @return <code>true</code> if the feature is intersecting the shape and <code>false</code> otherwise.
	 * @see #contains(double, double)
	 * @see #contains(Shape)
	 * @see #inside(Shape)
	 */
	public boolean intersects(Shape s);

	/**
	 * Check if the {@link Shape shape} given in parameter is inside the feature.
	 * The shape have to be expressed within original image space.
	 * @param s the {@link Shape shape} to check.
	 * @return <code>true</code> if the feature is inside the shape and <code>false</code> otherwise.
	 * @see #contains(Shape)
	 * @see #contains(double, double)
	 * @see #intersects(Shape)
	 */
	public boolean inside(Shape s);
	
	/**
	 * Get the object that is represented by this image feature.
	 * @return the object that is represented by this image feature
	 * @see #setUserData(Object)
	 */
	public Object getUserData();
	
	/**
	 * Set the object that is represented by this image feature.
	 * @param data object that is represented by this image feature
	 */
	public void setUserData(Object data);
	
	/**
	 * Get the {@link JImageFeatureLayer image feature layer} attached to this feature.
	 * @return the {@link JImageFeatureLayer image feature layer} attached to this feature
	 * @see #setImageFeatureLayer(JImageFeatureLayer)
	 */
	public JImageFeatureLayer getImageFeatureLayer();
	
	/**
	 * Set the {@link JImageFeatureLayer image feature layer} to attach to this feature.
	 * @param layer the {@link JImageFeatureLayer image feature layer} to attach to this feature
	 * @see #getImageFeatureLayer()
	 */
	public void setImageFeatureLayer(JImageFeatureLayer layer);
}
