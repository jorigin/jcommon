package org.jorigin.gui;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.jorigin.Common;

/**
 * An image feature that can be displayed within a {@link JImagePanel JImagePanel}.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.4
 */
public interface JImageFeature {

  /**
   * Draw this feature on the given {@link Graphics2D graphic context}. The given graphic context is related to the original image space. 
   * The transformation applied to the current display is given as <code>transform</code> and can be used to 
   * avoid rotation or scale for feature that have constant size or orientation (for example text features). 
   * @param g2d a graphic context expressed within image space.
   * @param transform the transform applied to the view within the {@link JImagePanel JImagePanel}.
   */
  public void draw(Graphics2D g2d, AffineTransform transform);
  
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
}
