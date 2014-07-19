package net.mloehr.mango.commands;

import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;


public class MapTextCommand extends TextCommand implements Command {

	@Override
    public void execute(DriveSupport driver, Task task) throws Exception {  
		executeText(driver, task);
	}
	
}
