package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.Timer;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class ClickCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
    	val element = driver.forThis(task.getXpath());
		String onClick = element.getAttribute("onclick");
		element.click();
		if (onClick != "") { // give the javascript some time to execute
			Timer.waitFor(200);
		}    		
    }
    
}
