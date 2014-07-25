package net.mloehr.mango.commands;

import org.openqa.selenium.WebElement;

import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public class AttributeCommand extends GetDataTemplate implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
    	executeGetData(driver, task);
    }
    
	public String getData(WebElement elements, Task task) {
		return elements.getAttribute(task.getText());
	}

}
