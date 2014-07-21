package net.mloehr.mango.commands;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public class TypeCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        for(val element: driver.forThese(task.getXpath())) {
        	element.sendKeys(task.getText());
        }
    }

}