import net.mloehr.mango.Mapper;
import net.mloehr.mango.action.Action;

public class GoogleResultsPage {

    private static final String RESULT_HEADLINES = ".//*[@id='res']//h3";
    private static final String BILDER_LINK = ".//*[@id='hdtb_s']/div/div[2]/a";
    
    public Action getResults(Object results) {
        return Action.withTasks()
                .mapText(RESULT_HEADLINES, results, new Mapper(results, "items", "(.*)"));
    }
    
	public Action getBilderText(StringBuilder text) {
        return Action.withTasks()
        		.getText(BILDER_LINK, text);
	}

}