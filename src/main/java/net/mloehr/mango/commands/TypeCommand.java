package net.mloehr.mango.commands;

import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public class TypeCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) {
        driver.forThis(task.getXpath())
                .sendKeys(task.getText());
    }

}