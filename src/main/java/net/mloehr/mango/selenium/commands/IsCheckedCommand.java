package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class IsCheckedCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
    	val element = driver.forThis(task.getXpath());
    	if (element.isSelected()) {
    		task.setResultValue("true");		
    	} else {
    		task.setResultValue("false");		    		
    	}
    }

}