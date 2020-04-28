
/**
 * The module description of the JCommon package
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @since 1.0.13
 */
module org.jcommon {

	exports org.jorigin;
	exports org.jorigin.event;
	exports org.jorigin.gui;
	exports org.jorigin.gui.layout;
	exports org.jorigin.gui.thumbnail;
	exports org.jorigin.identification;
	exports org.jorigin.io;
	exports org.jorigin.lang;
	exports org.jorigin.lang.xml;
	exports org.jorigin.logging;
	exports org.jorigin.plugin;
	exports org.jorigin.property;
	exports org.jorigin.state;
	exports org.jorigin.system.gui;
	exports org.jorigin.task;
	exports org.jorigin.task.gui;
	
	requires transitive java.desktop;
	requires transitive java.logging;
}