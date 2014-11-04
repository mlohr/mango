package net.mloehr.mango.tests;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.actions.WindowActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class WindowTest extends BaseTest {

	private final static String URL = "http://www.pageresource.com/jscript/jwinopen.htm";

	@Test
	public void testNewWindow() throws Exception {
		webUser = new WebUser(URL);
		StringBuilder headline = new StringBuilder();
		on(window()).openWindow();
		on(window()).getNewWindowText(headline);
		assertThat(headline.toString(), is("This is a new window!"));
		headline = new StringBuilder();
		on(window()).openWindow();
		on(window()).getNewWindowText(headline);
		assertThat(headline.toString(), is("This is a new window!"));		
		assertThat(webUser.getCurrentUrl(), endsWith("jwinopen.htm"));
	}

	public static Class<WindowActions> window() {
		return WindowActions.class;
	}

}
