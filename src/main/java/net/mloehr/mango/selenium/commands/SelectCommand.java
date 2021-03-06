package net.mloehr.mango.selenium.commands;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.support.ui.Select;

public class SelectCommand extends SelectTemplate implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		executeSelectCommand(driver, task);
	}

	@Override
	public void selectAction(Select select, Task task) {
		select.selectByVisibleText(task.getText());
	}

}
