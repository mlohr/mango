package net.mloehr.mango.tests;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.actions.GoogleSearchActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

public class BrowserSizingTests extends BaseTest {

    private final static String GOOGLE = "https://www.google.de/";

    @Test
    public void testHeight() throws Exception {
        webUser = new WebUser(GOOGLE, "mango.browser-height=300;");
        final int browserHeight = on(googleSearch()).getBrowserHeight();
        assertThat(browserHeight, is(300));
    }

    @Test
    public void testMinHeight() throws Exception {
        webUser = new WebUser(GOOGLE, "mango.browser-min-height=100;");
        final int browserHeight = on(googleSearch()).getBrowserHeight();
        assertThat(browserHeight, greaterThanOrEqualTo(100));
    }

    @Test
    public void testWidth() throws Exception {
        webUser = new WebUser(GOOGLE, "mango.browser-width=400;");
        final int browserWidth = on(googleSearch()).getBrowserWidth();
        assertThat(browserWidth, is(400));
    }

    @Test
    public void testMinWidth() throws Exception {
        webUser = new WebUser(GOOGLE, "mango.browser-min_width=200;");
        final int browserWidth = on(googleSearch()).getBrowserWidth();
        assertThat(browserWidth, greaterThanOrEqualTo(200));
    }

    public static Class<GoogleSearchActions> googleSearch() {
        return GoogleSearchActions.class;
    }

}
