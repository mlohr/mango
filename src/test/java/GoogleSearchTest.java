import static net.mloehr.mango.ext.RegexMatcher.containsMatch;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import lombok.val;
import mango.Results;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.WebUser;

import org.junit.Test;

public class GoogleSearchTest extends BaseTest {

    private final static String GOOGLE = "https://www.google.de/";
    
    @Test
    public void shouldSearchResults() throws Exception {
        webUser = new WebUser(GOOGLE);
        val results = new Results();
        
        assertThat(webUser.getCurrentUrl(), is(GOOGLE));
        on(googleSearchPage()).search("hello");
        on(googleResultsPage()).getResults(results);
        assertThat(results.getItems(), everyItem(containsMatch("(?i)hello")));
    }

    public static Class<GoogleSearchPage> googleSearchPage() {
        return GoogleSearchPage.class;
    }

    public static Class<GoogleResultsPage> googleResultsPage() {
        return GoogleResultsPage.class;
    }

}
