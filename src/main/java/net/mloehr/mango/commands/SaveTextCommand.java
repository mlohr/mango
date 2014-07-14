package net.mloehr.mango.commands;

import java.util.Arrays;
import java.util.List;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;
import net.mloehr.mango.Timer;

import org.openqa.selenium.WebElement;

public class SaveTextCommand implements Command {

    private static final String FIND_ALL = "{find-all}";

	@Override
    public void execute(DriveSupport driver, Task task) {    	
    	String text = "";
    	String xpath = task.getXpath();
    	List<WebElement> elements = null;
 
    	elements = getElements(driver, xpath);
    	if (elements == null || elements.size() == 0) {
    		return;
    	}
    	Timer timer = waitForElement(elements);
    	if (timer.isExpired()) {
    		return;
    	}
    	for(val element: elements) {
    		text += element.getText()+"\n";
    	}
    	task.getMapper().map(text);    		    		
    }

	private Timer waitForElement(List<WebElement> elements) {
		String text;
    	val timer = new Timer(2000);
    	timer.reset(); 	
		while (timer.isNotExpired()) {
    		text = elements.get(0).getText();
    		if (text.length() > 0) {
    			break;
    		}
    		Timer.waitFor(200);
    	}
		return timer;
	}

	private List<WebElement> getElements(DriveSupport driver, String xpath) {
		List<WebElement> elements;
		if (xpath.startsWith(FIND_ALL)) {
    		elements = driver.forThese(xpath.replace(FIND_ALL, ""));
    	} else {
    		elements = Arrays.asList(driver.forThis(xpath));
    	}
		return elements;
	}

}
