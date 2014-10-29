package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.Timer;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

public class ClickCommand implements Command {

	@Override
	public void execute(DriveSupport driver, Task task) throws Exception {
		val elements = driver.forThese(task.getXpath(), true);
		int index = 0;
		if (task.getText() != null && task.getText() != "") {
			index = Integer.valueOf(task.getText()).intValue();
		}
		final int size = elements.size();
		if (Math.abs(index) >= size) {
			throw new IndexOutOfBoundsException("Invalid index " + String.valueOf(index)
					+ ", should be in range: 0 <= abs(index) <= " + String.valueOf(size - 1));
		}
		if (index < 0) {
			index = size + index;
		}
		val element = elements.get(index);
		driver.focusOnElement(element); // make sure element is visible
		click(element, driver, task.getXpath());
	}

	private void click(final org.openqa.selenium.WebElement element, DriveSupport driver,
			String xpath) throws Exception {
		String onClick = element.getAttribute("onclick");
		element.click();
		if (onClick != "") { // give the javascript some time to execute
			Timer.waitFor(200);
		}
	}

}
