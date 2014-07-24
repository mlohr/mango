package net.mloehr.mango.commands;

import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public class ExecuteCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		driver.execute(task.getText(), task.getXpath());			
	}

}
