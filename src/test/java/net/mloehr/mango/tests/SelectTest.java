package net.mloehr.mango.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import lombok.val;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.actions.UtexasMenuActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class SelectTest extends BaseTest {

	private final static String URL = "http://www.utexas.edu/learn/forms/menus.html";

	@Test
	public void testSelection() throws Exception {
		val selected = new StringBuilder();
		webUser = new WebUser(URL);
		on(uTexasMenus()).getSelected(UtexasMenuActions.DEPARTMENT1, selected);
		on(uTexasMenus()).select(UtexasMenuActions.DEPARTMENT1, "Engineering");
		on(uTexasMenus()).getSelected(UtexasMenuActions.DEPARTMENT1, selected);
		assertThat(selected.toString(), is("ArchitectureEngineering"));
	}

	public static Class<UtexasMenuActions> uTexasMenus() {
		return UtexasMenuActions.class;
	}

}
