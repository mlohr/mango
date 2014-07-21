import static net.mloehr.mango.Matchers.containsMatch;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import lombok.val;
import mango.Results;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.WebUser;
import net.mloehr.mango.XPathNotFoundException;

import org.junit.Test;

public class GoogleSearchTest extends BaseTest {

    private final static String GOOGLE = "https://www.google.de/";
    
    @Test
    public void shouldSearchResults() throws Exception {
    	val results = new Results();
    	StringBuilder displayed;
    	
        webUser = new WebUser(GOOGLE, "network.automatic-ntlm-auth.trusted-uris=google.com;");
        assertThat(webUser.getCurrentUrl(), is(GOOGLE));
        
        displayed = new StringBuilder();
        on(googleSearchPage()).testButtonDisplayed(displayed);
        assertThat(displayed.toString(), is("false"));
        
        on(googleSearchPage()).search("hello");
        
        displayed = new StringBuilder();
        on(googleSearchPage()).testButtonDisplayed(displayed);
        assertThat(displayed.toString(), is("true"));

        on(googleResultsPage()).getResults(results);
        assertThat(results.getItems(), everyItem(containsMatch("(?i)hello")));
    }

    @Test
    public void shouldGetText() throws Exception {
    	val text = new StringBuilder();
        webUser = new WebUser(GOOGLE+"?hl=de");
        on(googleSearchPage()).search("hello");
        on(googleResultsPage()).getBilderText(text);
        assertThat(text.toString(), is("Bilder"));
    }
    
    @Test
    public void shouldNotFindNonExistingElement() throws Exception {
        webUser = new WebUser(GOOGLE);
        try {
			on(googleSearchPage()).clickOn("./*[@id='thisIsNotExisting']");
			fail("Missed Exception, should not happen!");
		} catch (Exception e) {
			assertThat(e, instanceOf(XPathNotFoundException.class));
		}
    }
    
    public static Class<GoogleSearchPage> googleSearchPage() {
        return GoogleSearchPage.class;
    }

    public static Class<GoogleResultsPage> googleResultsPage() {
        return GoogleResultsPage.class;
    }

}
