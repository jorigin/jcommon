package org.jorigin.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jorigin.Common;
import org.junit.jupiter.api.Test;

/**
 * A test dedicated to {@link PathUtil}
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.13
 */
public class PathUtilTest {

	/**
	 * Testing {@link PathUtil#getDirectory(String)}
	 */
	@Test
	public void getDirectoryTest() {
		
		String directory = null;
		String path = null;
		String returned = null;
		
		
		directory = null;
		path = "nodir";
		returned = PathUtil.getDirectory(path);
		// assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "dir";
		path = "dir/";
		returned = PathUtil.getDirectory(path);
		// assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "dir";
		path = "dir\\";
		returned = PathUtil.getDirectory(path);
		// assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "/home";
		path = "/home/one";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "C:\\home";
		path = "C:\\home\\one";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "/home/two";
		path = "/home/two/file.ext";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "C:\\home\\two";
		path = "C:\\home\\two\\file.ext";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "home";
		path = "home/tree";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "home";
		path = "home\\three";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "scheme:/home";
		path = "scheme:/home/four";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
		
		directory = "scheme://home";
		path = "scheme://home/four";
		returned = PathUtil.getDirectory(path);
		assertNotNull(returned, "No directory returned");
		assertEquals(directory, returned, "Returned \""+returned+"\" but expected \""+directory+"\"");
	}
	
}
