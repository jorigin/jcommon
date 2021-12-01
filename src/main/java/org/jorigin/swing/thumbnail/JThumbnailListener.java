package org.jorigin.swing.thumbnail;

import java.util.EventListener;

import org.jorigin.Common;

/**
 * An {@link EventListener event listener} that enables to monitor a thumbnail.
 * @param <T> the type of the object managed by the thumbnail.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
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