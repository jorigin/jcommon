package org.jorigin.swing.thumbnail;

import java.util.EventListener;
import java.util.List;

/**
 * An event listener that can deal with JThumbnailPanel.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.8
 * @version 1.0.8
 *
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