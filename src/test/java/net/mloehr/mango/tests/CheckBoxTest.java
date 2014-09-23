package net.mloehr.mango.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.action.Result;
import net.mloehr.mango.actions.UtexasCbActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class CheckBoxTest extends BaseTest {

    private final static String URL = "http://www.utexas.edu/learn/forms/checkboxes.html";

    @Test
    public void testCheck() throws Exception {
        webUser = new WebUser(URL);
        Result result = new Result();
        on(uTexasCb()).checkValue(UtexasCbActions.GRAPHIC_CHECKBOX, result);
        assertThat(result.getValue().toString(), is("true"));
        on(uTexasCb()).checkValue(UtexasCbActions.GRAPHIC_CHECKBOX, result);
        assertThat(result.getBooleanValue(), is(true));
        on(uTexasCb()).UncheckGraphic(result);
        assertThat(result.getBooleanValue(), is(false));
        on(uTexasCb()).UncheckGraphic(result);
        assertThat(result.getBooleanValue(), is(false));
    }

    @Test
    public void testClickingXPathSelectingMultipleElements() throws Exception {
        webUser = new WebUser(URL);
        Result result = new Result();
        on(uTexasCb()).Uncheck(UtexasCbActions.FIRST_CHECKBOX);
        on(uTexasCb()).Uncheck(UtexasCbActions.GRAPHIC_CHECKBOX);
        on(uTexasCb()).Uncheck(UtexasCbActions.LAST_CHECKBOX);

        on(uTexasCb()).clickCheckbox(-1);
        on(uTexasCb()).checkValue(UtexasCbActions.LAST_CHECKBOX, result);
        assertThat(result.getBooleanValue(), is(true));

        on(uTexasCb()).clickCheckbox(0);
        on(uTexasCb()).checkValue(UtexasCbActions.FIRST_CHECKBOX, result);
        assertThat(result.getBooleanValue(), is(true));

        on(uTexasCb()).clickCheckbox(1);
        on(uTexasCb()).checkValue(UtexasCbActions.GRAPHIC_CHECKBOX, result);
        assertThat(result.getBooleanValue(), is(true));

        try {
            on(uTexasCb()).clickCheckbox(1000);
            fail("Should catch index out of bounds.");
        } catch (Exception e) {
            assertThat(e, instanceOf(IndexOutOfBoundsException.class));
        }

        on(uTexasCb()).count(UtexasCbActions.ALL_CHECKBOXES, result);
        assertThat(result.getIntValue(), is(8));
    }

    public static Class<UtexasCbActions> uTexasCb() {
        return UtexasCbActions.class;
    }

}
