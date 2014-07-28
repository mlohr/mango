package net.mloehr.mango.selenium.commands;

import org.openqa.selenium.support.ui.Select;

import lombok.val;
import net.mloehr.mango.Timer;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class SelectCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
    	val select = new Select(driver.forThis(task.getXpath()));
    	select.selectByVisibleText(task.getText());
		Timer.waitFor(200); // give javascript some time to execute
	}

}
