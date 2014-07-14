mango
=====

Java testing DSL for web driven testing using Selenium, utilising the page object pattern, loose coupling and code completion for superb maintainability of your Web-UI tests.

Google search example code:

        webUser = new WebUser(GOOGLE);
        assertThat(webUser.getCurrentUrl(), is(GOOGLE));
        on(googleSearchPage()).search("hello");
        on(googleResultsPage()).getResults(results);
        assertThat(results.getItems(), everyItem(containsMatch("(?i)hello")));

With GoogleSearchPage code:

    private static final String SEARCH_INPUT = "//tbody//input[1]";
    private static final String SEARCH_BUTTON = "//button[@name='btnG']";
    
    public Action search(String text) {
        return Action.withTasks()
                .type(SEARCH_INPUT, text)
                .click(SEARCH_BUTTON);
    }

And GoogleResultsPage code:

    private static final String RESULT_HEADLINES = "{find-all}.//*[@id='res']//h3";
    
    public Action getResults(Object results) {
        return Action.withTasks()
                .saveText(RESULT_HEADLINES, results, new Mapper(results, "items", "(.*)"));
    }

Where Results is just a simple POJO (using the Project Lombok @Data annotation):

    @Data
    public class Results {
        private List<String>  items = new ArrayList<String>();
    }

Firebug support
---------------

If you put a firebug-{version}.xpi in your ./resources directory, it will be available in the running firefox profile.
