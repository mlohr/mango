package net.mloehr.mango.actions;

import net.mloehr.mango.action.Action;
import net.mloehr.mango.action.Result;

public class UtexasCbActions {

	public static final String FIRST_CHECKBOX = ".//*[@id='programming']";
	public static final String GRAPHIC_CHECKBOX = ".//*[@id='graphics']";
	public static final String LAST_CHECKBOX = ".//*[@id='page']";
	public static final String ALL_CHECKBOXES = ".//input[@type='checkbox']";
	
	public Action checkValue(String xpath, Result result) {
		return Action.withTasks()
				.check(xpath)
				.isChecked(xpath, result);
	}

	public Action UncheckGraphic(Result result) {
		return Action.withTasks()
				.uncheck(GRAPHIC_CHECKBOX)
				.isChecked(GRAPHIC_CHECKBOX, result);		
	}

	public Action clickCheckbox(int index) {
		return Action.withTasks()
				.click(ALL_CHECKBOXES, index);
	}

	public Action Uncheck(String xpath) {
		return Action.withTasks()
				.uncheck(xpath);
	}

	public Action count(String xpath, Result result) {
		return Action.withTasks()
				.count(xpath, result);
	}
}
