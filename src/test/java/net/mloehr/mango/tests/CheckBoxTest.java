package net.mloehr.mango.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.action.Result;
import net.mloehr.mango.actions.UtexasCbActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class CheckBoxTest extends BaseTest {

    private final static String URL = "http://www.utexas.edu/learn/forms/checkboxes.html";

	@Test
	public void testCheck() {
        webUser = new WebUser(URL);
        Result result = new Result();
        on(uTexasCb()).checkGraphic(result);
        assertThat(result.getValue().toString(), is("true"));
        on(uTexasCb()).checkGraphic(result);      
        assertThat(result.getValue().toString(), is("true"));
        on(uTexasCb()).UncheckGraphic(result);      
        assertThat(result.getValue().toString(), is("false"));
        on(uTexasCb()).UncheckGraphic(result);      
        assertThat(result.getValue().toString(), is("false"));
	}

    public static Class<UtexasCbActions> uTexasCb() {
        return UtexasCbActions.class;
    }

}
