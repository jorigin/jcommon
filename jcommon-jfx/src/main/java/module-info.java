
/**
 * The module description of the JCommon package
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @since 1.0.12
 */
module org.jcommon.jfx {

	exports org.jorigin.jfx;
	exports org.jorigin.jfx.control;
	exports org.jorigin.jfx.icon;
	exports org.jorigin.jfx.thumbnail;
	
	requires transitive org.jcommon.core;
	
	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	
	requires transitive java.desktop;
	requires transitive java.logging;
}