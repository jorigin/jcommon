package org.jorigin.swing.thumbnail;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jorigin.Common;

/**
 * A thumbnail that display an image as content.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @since 1.0.8
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @param <T> the type of the objects that are displayed within the thumbnail.
 */
public class JThumbnailImage<T> extends JThumbnail<T> {

  private static final long serialVersionUID = 1L;

  private JLabel imageLB = null;
  
  private Image image = null;
  
  private ImageIcon icon = null;
  
  /**
   * Create a new thumbnail that use an image as displayable content.
   * @param name the name of the thumbnail.
   * @param width the width of the thumbnail.
   * @param height the height of the thumbnail.
   * @param margin the margin size of the thumbnail.
   * @param ID the identifier of the thumbnail.
   * @param image the image to display.
   * @param content the content of the thumbnail.
   */
  public JThumbnailImage(String name, int width, int height, int margin, int ID, Image image, T content) {
    super(name, width, height, margin, ID);

    imageLB = new JLabel();
    
    this.image = image;
    
    icon = new ImageIcon(image);

    super.thumbnailComponent = imageLB;
    super.thumbnailPN.removeAll();
    super.thumbnailPN.add(thumbnailComponent, BorderLayout.CENTER);
    
    this.content = content;
    
    refreshGUI();
  }

  
  @Override
  public void validate(){
    super.validate();
  }
  
  @Override
  protected void refreshGUI(){
    
    super.refreshGUI();
    
    if (icon != null){
            
      if (icon.getImage() != null){
        if ((thumbnailPN.getSize().getWidth() > 0) && (thumbnailPN.getSize().getWidth() != icon.getIconWidth())){
          if (image != null){
            icon = new ImageIcon(image.getScaledInstance((int)thumbnailPN.getSize().getWidth(), (int)thumbnailPN.getSize().getHeight(), Image.SCALE_FAST));
            
            imageLB.setIcon(icon);
            imageLB.setSize(thumbnailPN.getSize());
            imageLB.setPreferredSize(thumbnailPN.getSize());
            imageLB.setMinimumSize(thumbnailPN.getSize());
            imageLB.setMaximumSize(thumbnailPN.getSize());
          }
        }
      }
    }

  }
}

