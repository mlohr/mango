package net.mloehr.mango.selenium.commands;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.XPathNotFoundException;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.WebElement;

@Slf4j
public class VisibilityCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        List<WebElement> elements;
        StringBuilder result = (StringBuilder) task.getData();
        try {
            elements = driver.forThese(task.getXpath(), false);
        } catch (XPathNotFoundException e) {
            result.append("false");
            return;
        }
        if (elements.size() > 1) {
            log.warn("More then one element found for {}", task.getXpath());
        }
        if (elements.get(0).isDisplayed()) {
            result.append("true");
        } else {
            result.append("false");
        }
    }

}
