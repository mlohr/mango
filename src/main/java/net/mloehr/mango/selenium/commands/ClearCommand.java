package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class ClearCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        for (val element : driver.forThese(task.getXpath(), true)) {
            element.clear();
        }
    }

}
