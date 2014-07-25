import net.mloehr.mango.Action;

public class GoogleSearchPage {

    private static final String SEARCH_INPUT = "//tbody//input[1]";
    private static final String SEARCH_BUTTON = "//button[@name='btnK']";    
    private static final String SEARCHLOOP_BUTTON = "//button[@name='btnG']";    
	private static final String SEARCH_BUTTON_DIV = ".//*[@id='gbqfbw']";

    public Action search(String text) {
        return Action.withTasks()
                .type(SEARCH_INPUT, text)
                .click(SEARCHLOOP_BUTTON);
    }
    
    public Action executeOnButtons(String script) {
        return Action.withTasks()
                .executeOnElement(SEARCH_BUTTON_DIV, script);
    }

    public Action clickOn(String xpath) {
        return Action.withTasks()
                .click(xpath);
    }

    public Action testButtonDisplayed(Object displayed) {
        return Action.withTasks()
        		.testVisibility(SEARCHLOOP_BUTTON, displayed);
    }

	public Action getSearchButtonClass(StringBuilder data) {
        return Action.withTasks()
        		.getAttribute(SEARCH_BUTTON, "class", data);
	}

}