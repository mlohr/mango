package net.mloehr.mango.selenium.commands;

import java.awt.image.BufferedImage;
import java.util.List;

import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class GetImageCommand implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		BufferedImage image = driver.getElementImage(task.getXpath());
		List<BufferedImage> list = (List<BufferedImage>) task.getData();
		list.add(image);
	}

}
