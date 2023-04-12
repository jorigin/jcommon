package org.jorigin.jfx.thumbnail;

/**
 * A handler that enables to process {@link JThumbnail thumbnail} activation.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 *
 * @param <T> the type of underlying {@link JThumbnail thumbnail} content
 */
public interface JThumbnailActivationHandler<T> {

	/**
	 * Handle the given {@link JThumbnail thumbnail} that has been activated.
	 * @param pane the thumbnail pane that is the source of the activation
	 * @param thumbnail the thumbnail that is activated
	 */
	void handle(JThumbnailPane<T> pane, JThumbnail<T> thumbnail);
	
}
