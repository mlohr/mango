import net.mloehr.mango.Action;

public class GoogleSearchPage {

    private static final String SEARCH_INPUT = "//tbody//input[1]";
    private static final String SEARCH_BUTTON = "//button[@name='btnG']";

    public Action search(String text) {
        return Action.withTasks()
                .type(SEARCH_INPUT, text)
                .click(SEARCH_BUTTON);
    }

}