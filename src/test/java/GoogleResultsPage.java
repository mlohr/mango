import net.mloehr.mango.Action;
import net.mloehr.mango.Mapper;

public class GoogleResultsPage {

    private static final String RESULT_HEADLINES = "{find-all}.//*[@id='res']//h3";

    public Action getResults(Object results) {
        return Action.withTasks()
                .saveText(RESULT_HEADLINES, results, new Mapper(results, "items", "(.*)"));
    }
}