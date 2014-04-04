package net.mloehr.mango.commands;
import net.mloehr.mango.DriveSupport;
import net.mloehr.mango.Task;
import net.mloehr.mango.Timer;
import lombok.val;

public class ClickCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) {
        val e = driver.forThis(task.getXpath());
        val onClick = e.getAttribute("onclick");
        e.click();
        if (onClick != "") { // give the javascript some time to execute
            Timer.waitFor(200);
        }
    }

}
