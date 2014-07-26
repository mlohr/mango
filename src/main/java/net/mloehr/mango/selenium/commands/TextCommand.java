package net.mloehr.mango.selenium.commands;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.WebElement;

public class TextCommand extends GetDataTemplate implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
    	executeGetData(driver, task);
    }
    
	public String getData(WebElement elements, Task task) {
		return elements.getText();
	}

}
