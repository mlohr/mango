package net.mloehr.mango.selenium.commands;

import org.openqa.selenium.WebElement;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class AttributeCommand extends GetDataTemplate implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
    	executeGetData(driver, task);
    }
    
	public String getData(WebElement elements, Task task) {
		return elements.getAttribute(task.getText());
	}

}
