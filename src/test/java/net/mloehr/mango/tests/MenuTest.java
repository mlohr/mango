package net.mloehr.mango.tests;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertThat;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.actions.MenuJsActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class MenuTest extends BaseTest {

	private final static String URL = "http://www.menujs.net/example_horizontal.html";

	@Test
	public void testSelection() throws Exception {
		webUser = new WebUser(URL);
		on(menu()).selectMenu(".//*[@id='menu']/li[3]/a", ".//*[@id='menu']/li[3]/ul/li[3]/a");
		assertThat(webUser.getCurrentUrl(), endsWith("javascript.html"));
	}

	public static Class<MenuJsActions> menu() {
		return MenuJsActions.class;
	}

}
