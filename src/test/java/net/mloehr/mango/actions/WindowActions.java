package net.mloehr.mango.actions;

import net.mloehr.mango.action.Action;

public class WindowActions {

    public static final String WINDOW_BUTTON1 = ".//*[@id='main_content']/form[1]/input";
    public static final String WINDOW_TEXT = "html/body/center[1]/h1";
  
    public Action openWindow() {
        return Action.withTasks().click(WINDOW_BUTTON1).switchTo("jex5");
    }

    public Action getNewWindowText(StringBuilder text) {
    	return Action.withTasks().getText(WINDOW_TEXT, text).switchTo();
    }
}
