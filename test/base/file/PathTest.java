package base.file;

import org.junit.Test;
import org.junit.Assert;

import base.exception.DataException;
import base.file.Path;

public class PathTest {
	
	// Test

	// good absolute paths, windows style
	@Test public void testParseWindows() throws Exception {
		
		new Path("C:\\folder\\subfolder"); // escaped backslashes
		new Path("C:/folder/subfolder");   // forward slashes
		new Path("/C:/folder/subfolder");  // forward slashes including root slash
		
		new Path("C:\\folder");
		new Path("C:/folder");
		new Path("/C:/folder");
		
		new Path("C:\\");
		new Path("C:/");
		new Path("/C:/");
		
		new Path("C:"); // it took extra code to make these work
		new Path("C:");
		new Path("/C:");
	}
	
	// good absolute paths, windows lan style
	@Test public void testParseWindowsLan() throws Exception {

		new Path("\\\\computer\\share");
		new Path("\\\\computer\\share\\file.ext");
		new Path("\\\\computer\\share\\folder\\file.ext");
		new Path("\\\\computer\\share\\folder\\folder\\file.ext");
	}

	// good absolute paths, mac and linux style
	// TODO this test fails on windows, maybe it only works on mac
	@Test public void testParseUnix() throws Exception {
		
		new Path("/");
		new Path("/file.ext");
		new Path("/folder/file.ext");
		new Path("/folder/folder/file.ext");
	}
	
	// bad because they are relative
	@Test public void testRelative() throws Exception {
		
		confirmBad("");
		confirmBad("hello");
		confirmBad("hello/you");
		
		confirmBad("./"); // here
		confirmBad("../"); // up one
		confirmBad("../../");
		
		confirmBad("./hello");
		confirmBad("../hello");
		confirmBad("../../hello");

		confirmBad("./hello/you");
		confirmBad("../hello/you");
		confirmBad("../../hello/you");
	}
	
	// nonsense
	@Test public void testNonsense() throws Exception {

		confirmBad(" ");
		confirmBad("*");
		confirmBad(":");
	}
	
	// Help

	private void confirmBad(String s) {
		try {
			new Path(s);
			Assert.fail("expected message exception");
		} catch (DataException e) {}
	}
}
