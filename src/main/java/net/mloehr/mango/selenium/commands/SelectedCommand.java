package net.mloehr.mango.selenium.commands;

import org.openqa.selenium.support.ui.Select;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class SelectedCommand extends SelectTemplate implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		executeSelectCommand(driver, task);
	}

	@Override
	public void selectAction(Select select, Task task) {
        StringBuilder data = (StringBuilder) task.getData();
        data.append(select.getFirstSelectedOption().getText());
	}

}
