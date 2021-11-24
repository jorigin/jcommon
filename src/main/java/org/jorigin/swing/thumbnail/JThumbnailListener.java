package org.jorigin.swing.thumbnail;

import java.util.EventListener;

/**
 * An {@link EventListener event listener} that enables to monitor a thumbnail.
 * @param <T> the type of the object managed by the thumbnail.
 * @author Julien Seinturier - COMEX S.A. - http://www.seinturier.fr
 * @version 1.0.8
 * @since 1.0.8
 */
public interface JThumbnailListener<T> extends EventListener {

  /**
   * The thumbnail has been focused.
   * @param thumbnail the thumbnail that is the source of the event.
   */
  public void thumbnailFocused(JThumbnail<T> thumbnail);
  
  /**
   * The thumbnail has been unfocused.
   * @param thumbnail the thumbnail that is the source of the event.
   */
  public void thumbnailUnfocused(JThumbnail<T> thumbnail);
  
  /**
   * The thumbnail has been selected.
   * @param thumbnail the thumbnail that is the source of the event.
   */
  public void thumbnailSelected(JThumbnail<T> thumbnail);

  /**
   * The thumbnail has been activated.
   * @param thumbnail the thumbnail that is the source of the event.
   */
  public void thumbnailActivated(JThumbnail<T> thumbnail);

}