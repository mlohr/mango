package net.mloehr.mango.actions;

import net.mloehr.mango.action.Action;
import net.mloehr.mango.action.Result;

public class UtexasCbActions {

	private static final String GRAPHIC_CHECKBOX = ".//*[@id='graphics']";

	public Action checkGraphic(Result result) {
		return Action.withTasks()
				.check(GRAPHIC_CHECKBOX)
				.isChecked(GRAPHIC_CHECKBOX, result);
	}

	public Action UncheckGraphic(Result result) {
		return Action.withTasks()
				.uncheck(GRAPHIC_CHECKBOX)
				.isChecked(GRAPHIC_CHECKBOX, result);		
	}

}
