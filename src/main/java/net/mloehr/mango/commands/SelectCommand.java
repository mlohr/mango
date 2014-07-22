package net.mloehr.mango.commands;

import org.openqa.selenium.support.ui.Select;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public class SelectCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
    	val select = new Select(driver.forThis(task.getXpath()));
    	select.selectByVisibleText(task.getText());
	}

}
