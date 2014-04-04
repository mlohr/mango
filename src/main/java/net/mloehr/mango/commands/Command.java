package net.mloehr.mango.commands;

import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;

public interface Command {
    public void execute(DriveSupport driver, Task task);
}