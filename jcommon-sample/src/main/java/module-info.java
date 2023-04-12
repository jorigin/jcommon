
/**
 * The module description of the JCommon package
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @since 1.0.12
 */
module org.jcommon.samples {
	
	exports org.jorigin.sample.jfx;
	exports org.jorigin.sample.jfx.thumbnail;
	
	requires transitive org.jcommon.core;
	requires transitive org.jcommon.jfx;
	
	requires transitive javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	
	requires transitive java.desktop;
	requires transitive java.logging;

}