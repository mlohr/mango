import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.WebUser;

import org.junit.Test;

//@Slf4j
public class BareBoneTest extends BaseTest {
    private final static String GOOGLE = "https://www.google.de/";

    @Test
    public void shouldSearchResults() throws Exception {
        webUser = new WebUser(GOOGLE);
        assertThat(webUser.getCurrentUrl(), is(GOOGLE));
        on(googleSearchPage()).search("hello");
        int i = 1;
    }

    public static Class<GoogleSearchPage.Actions> googleSearchPage() {
        return GoogleSearchPage.Actions.class;
    }
}
