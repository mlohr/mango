package net.mloehr.mango.selenium.commands;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class ExecuteCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		driver.execute(task.getText(), task.getXpath());			
	}

}
