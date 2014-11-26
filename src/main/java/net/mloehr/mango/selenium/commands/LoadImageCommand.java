package net.mloehr.mango.selenium.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class LoadImageCommand implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		val url = new File(task.getText());
		val image = ImageIO.read(url);
		List<BufferedImage> list = (List<BufferedImage>) task.getData();
		list.add(image);
	}

}
