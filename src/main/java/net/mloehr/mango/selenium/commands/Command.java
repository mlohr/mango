package net.mloehr.mango.selenium.commands;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public interface Command {

    public void execute(DriveSupport driver, Task task) throws Exception;

}