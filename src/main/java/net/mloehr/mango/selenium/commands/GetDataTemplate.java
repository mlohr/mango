package net.mloehr.mango.selenium.commands;

import java.util.List;

import lombok.val;
import net.mloehr.mango.Timer;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;

public abstract class GetDataTemplate {

    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(GetDataTemplate.class);

    public GetDataTemplate() {
        super();
    }

    public void executeGetData(DriveSupport driver, Task task) throws Exception {
        List<WebElement> elements = null;
        val timer = new Timer(Timer.TIMEOUT_IN_SECONDS);

        timer.reset();
        while (timer.isNotExpired()) {
            try {
                elements = driver.forThese(task.getXpath(), true);
                break;
            } catch (StaleElementReferenceException e) {
                logger.warn("Retrying because of stale element {}",
                        task.getXpath());
                Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
            }
        }
        if (timer.isExpired()) {
            logger.warn("Timer expired for xpath {}", task.getXpath());
        }
        if (task.getMapper() == null) {
            appendElementsText(task, elements);
        } else {
            mapElementsText(task, elements);
        }
    }

    public abstract String getData(WebElement elements, Task task);

    protected void appendElementsText(Task task, List<WebElement> elements) {
        StringBuilder data = (StringBuilder) task.getData();
        if (elements.size() == 1) {
            data.append(getData(elements.get(0), task));
        } else {
            for (val element : elements) {
                data.append(getData(element, task) + "\n");
            }
        }
    }

    protected void mapElementsText(Task task, List<WebElement> elements) {
        String text = "";
        for (val element : elements) {
            text += getData(element, task) + "\n";
        }
        task.getMapper().map(text);
    }

    protected Timer waitForElement(List<WebElement> elements, Task task) {
        String text = "";
        val timer = new Timer(10);
        timer.reset();
        while (timer.isNotExpired()) {
            text = getData(elements.get(0), task);
            if (text.length() > 0) {
                break;
            }
            Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
        }
        return timer;
    }

}