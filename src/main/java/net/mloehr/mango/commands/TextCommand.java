package net.mloehr.mango.commands;

import java.util.Arrays;
import java.util.List;

import lombok.val;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;
import net.mloehr.mango.Timer;

import org.openqa.selenium.WebElement;

public class TextCommand {

    private static final String FIND_ALL = "{find-all}";

    public void executeText(DriveSupport driver, Task task) throws Exception {    	
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
    	if (task.getMapper() == null) {
    		StringBuilder data = (StringBuilder) task.getData();
        	if (elements.size() == 1) {
        		data.append(elements.get(0).getText());
        	} else {        		
        		for(val element: elements) {
        			data.append(element.getText()+"\n");
        		}    		
        	}
    	} else {
			for(val element: elements) {
				text += element.getText()+"\n";
			}
			task.getMapper().map(text);    		    		    		
    	}
//    	if (elements.size() == 1) {
//    		StringBuilder data = (StringBuilder) task.getData();
//    		data.append(elements.get(0).getText());
//    	} else {    		
//    		for(val element: elements) {
//    			text += element.getText()+"\n";
//    		}
//    		task.getMapper().map(text);    		    		
//    	}
    }

	private Timer waitForElement(List<WebElement> elements) {
		String text;
    	val timer = new Timer(10);
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

	private List<WebElement> getElements(DriveSupport driver, String xpath) throws Exception {
		List<WebElement> elements;
		if (xpath.startsWith(FIND_ALL)) {
    		elements = driver.forThese(xpath.replace(FIND_ALL, ""));
    	} else {
    		elements = Arrays.asList(driver.forThis(xpath));
    	}
		return elements;
	}

}
