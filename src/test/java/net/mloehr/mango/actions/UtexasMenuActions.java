package net.mloehr.mango.actions;

import net.mloehr.mango.action.Action;

public class UtexasMenuActions {

    public static final String DEPARTMENT1 = ".//*[@id='Department-1']";

    public Action getSelected(String xpath, Object data) {
        return Action.withTasks().getSelected(xpath, data);
    }

    public Action select(String xpath, String option) {
        return Action.withTasks().select(xpath, option);
    }
}
