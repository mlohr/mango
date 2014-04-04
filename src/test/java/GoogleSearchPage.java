import net.mloehr.mango.Action;
import net.mloehr.mango.Page;

public class GoogleSearchPage implements Page {
    public interface Actions extends Page {
        public Action search(String text);
    }

    private static final String SEARCH_INPUT  = "//tbody//input[1]";
    // private static final String SEARCH_COMPLETION_TABLE = "//table[@class = 'gssb_m']";
    private static final String SEARCH_BUTTON = "//button[@id = 'gbqfb']";

    public Action search(String text) {
        return Action.withTasks()
            .type(SEARCH_INPUT, text)
            .click(SEARCH_BUTTON);
    }
}