package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class CheckCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        for(val element: driver.forThese(task.getXpath(), true)) {
        	if (element.isSelected()) {
        		if(task.getText().equals("uncheck")) {
        			element.click();
        		}        		
        	} else {
        		if(task.getText().equals("check")) {
        			element.click();
        		}        		        		
        	}
        }
    }

}