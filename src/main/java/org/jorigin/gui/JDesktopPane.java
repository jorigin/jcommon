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
package org.jorigin.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import org.jorigin.Common;

/**
 * This class extends a {@link javax.swing.JDesktopPane JDesktopPane} and provide organization methods for the internal frames.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.1
 */
public class JDesktopPane extends javax.swing.JDesktopPane {

  private static final long serialVersionUID = Common.BUILD;
  
  /**
   * This flag represent a no organization method for the internal frames.
   * @see #MOSAIC
   * @see #CASCADE
   */
  public static final int NONE       = 0;
  
  /**
   * This flag represents a mosaic organization for the internal frames.
   * @see #NONE
   * @see #CASCADE
   */
  public static final int MOSAIC     = 1;
  
  /**
   * This flag represents a cascade organization for the internal frames.
   * @see #NONE
   * @see #MOSAIC
   */
  public static final int CASCADE    = 2;

  /** No fit is applied to the added components */
  public static final int FIT_NONE             = 0;
  
  /** Added component are centered */
  public static final int FIT_CENTER           = 1;
  
  /** 
   * Added component are centered and resized to fit
   * the default. The fit ratio is given by method <code>getFrameDimensionRatio()</code>
   */
  public static final int FIT_CENTER_RESIZE    = 2;
  
  /**
   * Added component are centered then switched in x and y by a delta. The delta is
   * available with methods <code>setFitDeltaMax(double delta)</code> and 
   * <code>getFitDeltaMax</code>
   */
  public static final int FIT_CENTER_DELTA        = 3;
  
  /**
   * Added component are centered, resized and switched by a delta. 
   * The fit ratio is given by method <code>getFrameDimensionRatio()</code>. 
   * The delta is
   * available with methods <code>setFitDeltaMax(double delta)</code> and 
   * <code>getFitDeltaMax</code>
   */
  public static final int FIT_CENTER_RESIZE_DELTA = 4;
  
  /**
   * The current dimension ratio used. By default this ratio is 0.5 of the 
   * desktop size. 
   */
  private double frameDimensionRatio = 0.5d;
  
  /**
   * The current fit method. By default this method is set to 
   * <code>FIT_CENTER_DELTA</code>.
   */
  private int fitMethod = FIT_CENTER_DELTA;
  
  /**
   * The fit delta max used. By default this delta is set to <code>50</code>
   */
  private double fitDeltaMax = 50;
  
  /**
   * The organization method used. By default this is set to <code>NONE</code>.
   */
  private int organizeMethod = NONE;

  /**
   * Create a new default desktop pane.
   */
  public JDesktopPane() {
    super();
  }


//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                                               AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  
  /**
   * Set the organization method used by the desktop to organize components.
   * @param method the method to use, can be {@link #CASCADE}, {@link #MOSAIC} or {@link #NONE}.
   */
  public void setOrganizeMethod(int method){
    this.organizeMethod = method;
  }

  /**
   * Get the organization method used by the desktop to organize components.
   * @return the organization method.
   */
  public int getOrganizeMethod(){
    return this.organizeMethod;
  }
  
  /**
   * Set the frame dimension ratio for internal frame. All added internal frame
   * will be redimensionned to fit the ratio of descktop dimension.
   * @param ratio the ratio of desktop dimension.
   */
  public void setFrameDimensionRatio(double ratio){
    this.frameDimensionRatio = ratio;
  }
  
  /**
   * Get the frame dimension ratio for internal frame. All added internal frame
   * will be redimensionned to fit the ratio of descktop dimension.
   * @return ratio the ratio of desktop dimension.
   */
  public double getFrameDimensionRatio(){
    return this.frameDimensionRatio;
  }
  
  /**
   * Set the fit method used to initialize space occupation
   * of new components.
   * @param fitMethod the fit method to use (FIT_CENTER, FIT_CENTER_RESIZE, ...)
   */
  public void setFitMethod(int fitMethod){
    this.fitMethod = fitMethod;
  }
  
  /**
   * Get the fit method used to initialize space occupation
   * of new components.
   * @return the fit method to use (FIT_CENTER, FIT_CENTER_RESIZE, ...)
   */
  public int getFitMethod(){
    return this.fitMethod;
  }
  
  /**
   * Set the fit delta maximum value. The delta is used when
   * an organization with random delta is processed.
   * @param delta the max delta value.
   */
  public void setFitDeltaMax(double delta){
    this.fitDeltaMax = delta;
  }
  
  /**
   * Get the fit delta maximum value. The delta is used when
   * an organization with random delta is processed.
   * @return the max delta value.
   */
  public double getFitDeltaMax(){
    return this.fitDeltaMax;
  }
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                                           AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

  /**
   * Organize the Internal frames following the default method
   */
  public void organize(){
    organize(organizeMethod);
  }

  /**
   * Organize the Internal frames following the method given in parameter.
   * {@link #MOSAIC}
   * @param method the method to use (can be {@link #MOSAIC} or {@link #CASCADE})  
   */
  public void organize(int method){

     switch(method){
       case MOSAIC:
           mosaic();
           break;
       case CASCADE:
           cascade();
           break;
       default:
     }
  }

  /**
   * Reorganize the frames of the desktop by using cascading
   */
  public void cascade(){

    // Recuperation des frames du desktop
    JInternalFrame[] frames = getAllFrames();

    // calcul du nombre de frame internes non icones
    int nbInternalFrame = frames.length;

    int n = nbInternalFrame;

    for( int i = 0 ; i < nbInternalFrame; ++i){
      if(frames[i].isIcon()){
        n--;
      }
    }

    for(int i = 0, j = n-1 ; i<nbInternalFrame; i++, j--){
      if (!frames[i].isIcon()){
        frames[i].setBounds(j * 20, j * 20, getWidth() / 3, getHeight() / 3);
      }
    }


  }

  /**
   * Reorganize the frames of the desktop by using mosaic
   */
  public void mosaic(){

     JInternalFrame [] frames = getAllFrames();

     // calcul du nombre de frame internes non icones
     int nbInternalFrame = frames.length;

     // Nombre de fenetres non iconifiées
     int n = nbInternalFrame;

     // Espace laissé libre pour permettre de voir les fenetres iconifiées
     int bas = 0;

     // Nombre de colonnes et de lignes de la mosaique
     int column = 0;
     int line   = 0;

     for( int i = 0 ; i < nbInternalFrame; ++i){
       if (frames[i].isIcon()){
         --n;
       }
     }


     if(n != nbInternalFrame){
       bas = 30;
     }

     // mise en mosaïque

     // Si toutes les fenetres sont iconifiées, il n'y a rien à faire
     if( n == 0) return;

     // Calcul du nombre de colonnes de la mosaique
     column = (int)Math.sqrt(n);

     if( n!=column*column){
       column++;
     }

     // calcul du nombre de lignes de la mosaique
     if((n-1)/column+1 < column){
       line = column-1;
     } else{
       line = column;
     }


     int dx = getWidth()/column;
     int dy = getHeight()/line - bas;

     int k = 0;

     for( int i = 0 ; i <column; i++){
       for (int j = 0; j < column && k < n; j++, k++) {
         frames[i * column + j].setBounds(j * dx, i * dy, dx, dy);
       }
     }
  }

  /**
   * Fit the given {@link javax.swing.JInternalFrame frame} to take all the available space within the desktop pane.
   * @param frame the {@link javax.swing.JInternalFrame frame} to fit.
   */
  public void fit(JInternalFrame frame){
    
    Dimension desktopDimension = null;
    Dimension frameDimension   = null;
    
    double xDelta = 0.0;
    double yDelta = 0.0;
    
    int locationx = 0;
    int locationy = 0;
    
    frame.pack();
    
    desktopDimension = new Dimension((int)this.getSize().getWidth(),
        (int)this.getSize().getHeight());
    
    frameDimension   = frame.getSize();
    
//    System.out.println("frame dimension: "+frameDimension);
//    System.out.println("desk dimension: "+desktopDimension);
    
    switch(fitMethod){
      case FIT_NONE:
        break;
      case FIT_CENTER:

        frame.setPreferredSize(frameDimension);
        
        locationx = desktopDimension.width / 2 - (frameDimension.width / 2);
        locationy = desktopDimension.height / 2 - (frameDimension.height / 2);
       
        break;
      case FIT_CENTER_RESIZE:
        frameDimension   = new Dimension(((int)(desktopDimension.getWidth()*0.5d)),
            ((int)(desktopDimension.getHeight()*0.5d)));

        frame.setSize(frameDimension);
        frame.setPreferredSize(frameDimension);
        
        locationx = desktopDimension.width / 2 - (frameDimension.width / 2);
        locationy = desktopDimension.height / 2 - (frameDimension.height / 2);
        
        break;
      case FIT_CENTER_DELTA:

        frame.setPreferredSize(frameDimension);
        
        // Calcul d'un delta pour le positionnement
        xDelta = Math.random()*this.fitDeltaMax;
        yDelta = Math.random()*this.fitDeltaMax;
        
        // Changement de signe du delta
        if (Math.random() > 0.5d){
          xDelta *= -1;
        }
        
        if (Math.random() > 0.5d){
          yDelta *= -1;
        }

        locationx = (int) xDelta + desktopDimension.width / 2 - (frameDimension.width / 2);
        locationy = (int) yDelta + desktopDimension.height / 2 - (frameDimension.height / 2);
        
        break;
        
      case FIT_CENTER_RESIZE_DELTA:
        frameDimension   = new Dimension(((int)(desktopDimension.getWidth()*0.5d)),
            ((int)(desktopDimension.getHeight()*0.5d)));

        frame.setSize(frameDimension);
        frame.setPreferredSize(frameDimension);
        
        // Calcul d'un delta pour le positionnement
        xDelta = Math.random()*this.fitDeltaMax;
        yDelta = Math.random()*this.fitDeltaMax;
        
        // Changement de signe du delta
        if (Math.random() > 0.5d){
          xDelta *= -1;
        }
        
        if (Math.random() > 0.5d){
          yDelta *= -1;
        }
        
        locationx = (int) xDelta + desktopDimension.width / 2 - (frameDimension.width / 2);
        locationy = (int) yDelta + desktopDimension.height / 2 - (frameDimension.height / 2);
        break;
    }
    
    // Verification de la cohérence des tailles de fenêtre et correction
    // en cas d'erreur
    if (frame.getWidth() > this.getWidth()){
      frameDimension.setSize(this.getWidth(), frameDimension.getHeight());
    }
    
    if (frame.getHeight() > this.getHeight()){
      frameDimension.setSize(frameDimension.getWidth(), this.getHeight());
    }
    
    frame.setSize(frameDimension);
    
    if (   (locationx >= 0) && (locationx < this.getSize().getWidth())
        && (locationy >= 0) && (locationy < this.getSize().getHeight())){
      frame.setLocation(locationx, locationy); 
    } else {
      frame.setLocation(0, 0); 
    }
    
    
  }
  

  /**
   * Iconify all frames inb the desktop
   */
  void iconifyAll(){

     JInternalFrame [] frames = getAllFrames();

     try{
       for( int i = 0; i<frames.length; i++){
         frames[i].setIcon(true);
       }
     }catch(PropertyVetoException ex){
       System.err.println(ex.getMessage());
       ex.printStackTrace(System.err);
     }

  }



  /**
   * Close all frames in the desktop pane
   */
  void closeAll(){

    JInternalFrame [] frames = getAllFrames();

      try{
        for( int i = 0; i<frames.length; i++){
          frames[i].setClosed(true);
        }
      }catch(PropertyVetoException ex){
        System.err.println(ex.getMessage());
        ex.printStackTrace(System.err);
      }
  }
  
  
  /**
   * Add a container as internal frame in the current desktop.
   * @param title String the title of the internal frame
   * @param container Container the conainer to add
   * @param fit If this boolean is true, the frame is redimensioned and relocated
   * by the desktop to fit the default placement methods. If false is given, the
   * internal frame is not resized and relocated.
   */
  public void addInternalFrame(String title, Container container, boolean fit){

    JInternalFrame frame = new JInternalFrame(title, true, true, true, true);

    frame.getContentPane().add(container);
    
    addInternalFrame(frame, fit);
     
  }
  
  /**
   * Add a container as internal frame in the current desktop.
   * @param title String the title of the internal frame
   * @param container Container the conainer to add
   */
  public void addInternalFrame(String title, Container container){
    addInternalFrame(title, container, true);  
  }

  
  /**
   * Add an internal frame to the desktop.
   * @param frame the frame to add.
   * @param fit is the frame has to fit within the desktop pane.
   */
  public void addInternalFrame(JInternalFrame frame, boolean fit){
   
    if (fit == true){
     fit(frame);
    }
    
    frame.pack();
    
    this.add(frame);
    this.organize();
    this.validate();
       
    this.repaint();

    frame.setFocusable(true);
    frame.setVisible(true);
    frame.requestFocus();
    frame.repaint();
  }
}

