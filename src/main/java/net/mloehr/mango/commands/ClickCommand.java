package net.mloehr.mango.commands;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;
import net.mloehr.mango.Timer;

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
