package net.mloehr.mango.actions;

import net.mloehr.mango.action.Action;

public class MenuJsActions {

    public static final String DEPARTMENT1 = ".//*[@id='Department-1']";

    public Action selectMenu(String xpath, String item) {
        return Action.withTasks().selectMenuItem(xpath, item);
    }

}
