package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class CountCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
    	val elements = driver.forThese(task.getXpath());
		if (task.getResult() != null) {
			task.setResultValue(String.valueOf(elements.size()));			
		}
	}

}
