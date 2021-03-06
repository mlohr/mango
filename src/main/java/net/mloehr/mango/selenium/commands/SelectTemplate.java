package net.mloehr.mango.selenium.commands;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.Timer;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

@Slf4j
public abstract class SelectTemplate {

    public abstract void selectAction(Select select, Task task);

	public void executeSelectCommand(DriveSupport driver, Task task) throws Exception {
        Exception catched = null;
        val select = new Select(driver.forThis(task.getXpath()));
        val timer = new Timer(Timer.TIMEOUT_IN_SECONDS);
        while (timer.isNotExpired()) {
            try {
            	selectAction(select, task);
                return;
            } catch (NoSuchElementException e) {
                catched = e;
                Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
                log.warn("Retrying to select {} for xpath {}", task.getText(),
                        task.getXpath());
            }
        }
        throw catched;
	}
}
