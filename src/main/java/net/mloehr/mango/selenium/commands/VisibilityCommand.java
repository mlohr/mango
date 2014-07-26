package net.mloehr.mango.selenium.commands;

import java.util.List;

import net.mloehr.mango.XPathNotFoundException;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;

public class VisibilityCommand implements Command {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VisibilityCommand.class);

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        List<WebElement> elements;        
        StringBuilder result = (StringBuilder) task.getData();
        try {
			elements = driver.forThese(task.getXpath());
		} catch (XPathNotFoundException e) {
			result.append("false");
			return;
		}
        if (elements.size() > 1) {
        	logger.warn("More then one element found for {}", task.getXpath());
        }
        if (elements.get(0).isDisplayed()) {
			result.append("true");
        } else {
			result.append("false");
        }
    }
    
}
