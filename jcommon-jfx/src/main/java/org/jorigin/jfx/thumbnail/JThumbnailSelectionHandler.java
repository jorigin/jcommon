package org.jorigin.jfx.thumbnail;

import java.util.List;

/**
 * A handler that enables to process {@link JThumbnail thumbnail} selection.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 *
 * @param <T> the type of underlying {@link JThumbnail thumbnail} content
 */
public interface JThumbnailSelectionHandler<T> {

	/**
	 * Handle the given {@link JThumbnail thumbnails} that have been selected (can be empty).
	 * @param pane the thumbnail pane that is the source of the activation
	 * @param thumbnails the thumbnails that have been selected
	 */
	void handle(JThumbnailPane<T> pane, List<JThumbnail<T>> thumbnails);
}
