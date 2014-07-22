package net.mloehr.mango.commands;

import java.util.List;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;
import net.mloehr.mango.Timer;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;

public class TextCommand implements Command {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TextCommand.class);

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        List<WebElement> elements = null;
        
		boolean notReady= true;
		do {
			try {
				elements = driver.forThese(task.getXpath());
				waitForElement(elements);
				notReady = false;
			} catch (StaleElementReferenceException e) {
				logger.warn("Stale element...");
				Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
			}    		
		} while (notReady);

        
		if (task.getMapper() == null) {
			appendElementsText(task, elements);
		} else {
			mapElementsText(task, elements);    		    		    		
		}
    }

	private void appendElementsText(Task task, List<WebElement> elements) {
		StringBuilder data = (StringBuilder) task.getData();
		if (elements.size() == 1) {
			data.append(elements.get(0).getText());
		} else {        		
			for(val element: elements) {
				data.append(element.getText()+"\n");
			}    		
		}
	}
	
	private void mapElementsText(Task task, List<WebElement> elements) {
		String text = "";
		for(val element: elements) {
			text += element.getText()+"\n";
		}
		task.getMapper().map(text);
	}

	private Timer waitForElement(List<WebElement> elements) {
		String text = "";
		val timer = new Timer(10);
		timer.reset(); 	
		while (timer.isNotExpired()) {
//			if (elements.get(0).isDisplayed()) {
//				break;
//			}
			text = elements.get(0).getText();
			if (text.length() > 0) {
				break;
			}
			Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
		}
		return timer;
	}

}
