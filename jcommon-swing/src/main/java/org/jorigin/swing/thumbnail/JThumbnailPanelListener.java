package org.jorigin.swing.thumbnail;

import java.util.EventListener;
import java.util.List;

import org.jorigin.Common;

/**
 * An event listener that can deal with JThumbnailPanel.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @since 1.0.8
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @param <T> the type of the objects that are displayed within the thumbnails.
 */
public interface JThumbnailPanelListener<T> extends EventListener{

	/**
	 * The given <code>thumbnail</code> has been added to the <code>panel</code>.
	 * @param panel the source of the event.
	 * @param thumbnail the added thumbnail.
	 */
  public void thumbnailAdded(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);
  
  /**
   * The given <code>thumbnail</code> has been removed from the <code>panel</code>.
   * @param panel the source of the event.
   * @param thumbnail the removed thumbnail.
   */
  public void thumbnailRemoved(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);
 
  /**
   * The given <code>thumbnail</code> has been selected within the <code>panel</code>.
   * @param panel the source of the event.
   * @param thumbnail the selected thumbnail.
   */
  public void thumbnailSelected(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);
  
  /**
   * The given <code>thumbnails</code> have been selected within the <code>panel</code>.
   * @param panel the source of the event.
   * @param thumbnails the selected thumbnails.
   */
  public void thumbnailSelected(JThumbnailPanel<T> panel, List<JThumbnail<T>> thumbnails);
  
  /**
   * The given <code>thumbnail</code> has been activated within the <code>panel</code>.
   * @param panel the source of the event.
   * @param thumbnail the activated thumbnail.
   */
  public void thumbnailActivated(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);

  /**
   * The given <code>thumbnails</code> have been activated within the <code>panel</code>.
   * @param panel the source of the event.
   * @param thumbnail the activated thumbnail.
   */
  public void thumbnailEntered(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);

  /**
   * The given <code>thumbnail</code> has been exited.
   * @param panel the source of the event. 
   * @param thumbnail the exited thumbnail.
   */
  public void thumbnailExited(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);
  
  /**
   * The given <code>thumbnail</code> need to be refreshed.
   * @param panel panel the source of the event. 
   * @param thumbnail the thumbnail that need to be refreshed.
   */
  public void thumbnailNeedRefresh(JThumbnailPanel<T> panel, JThumbnail<T> thumbnail);

}