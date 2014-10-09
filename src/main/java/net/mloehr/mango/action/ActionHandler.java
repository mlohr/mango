package net.mloehr.mango.action;

import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.util.proxy.MethodHandler;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.selenium.DriveSupport;
import net.mloehr.mango.selenium.commands.AttributeCommand;
import net.mloehr.mango.selenium.commands.CheckCommand;
import net.mloehr.mango.selenium.commands.ClearCommand;
import net.mloehr.mango.selenium.commands.ClickCommand;
import net.mloehr.mango.selenium.commands.Command;
import net.mloehr.mango.selenium.commands.CountCommand;
import net.mloehr.mango.selenium.commands.ExecuteCommand;
import net.mloehr.mango.selenium.commands.IsCheckedCommand;
import net.mloehr.mango.selenium.commands.SelectCommand;
import net.mloehr.mango.selenium.commands.SelectedCommand;
import net.mloehr.mango.selenium.commands.SlideCommand;
import net.mloehr.mango.selenium.commands.TextCommand;
import net.mloehr.mango.selenium.commands.TypeCommand;
import net.mloehr.mango.selenium.commands.VisibilityCommand;

@Slf4j
public class ActionHandler implements MethodHandler {
    private final DriveSupport driver;
    private final HashMap<String, Command> availableCommands;

    public ActionHandler(DriveSupport driver) {
        this.driver = driver;
        availableCommands = new HashMap<String, Command>();
        availableCommands.put("check", new CheckCommand());
        availableCommands.put("clear", new ClearCommand());
        availableCommands.put("click", new ClickCommand());
        availableCommands.put("count", new CountCommand());
        availableCommands.put("eval", new ExecuteCommand());
        availableCommands.put("execute", new ExecuteCommand());
        availableCommands.put("executeOnElement", new ExecuteCommand());
        availableCommands.put("getAttribute", new AttributeCommand());
        availableCommands.put("getSelected", new SelectedCommand());
        availableCommands.put("getText", new TextCommand());
        availableCommands.put("isChecked", new IsCheckedCommand());
        availableCommands.put("mapText", new TextCommand());
        availableCommands.put("select", new SelectCommand());
        availableCommands.put("testVisibility", new VisibilityCommand());
        availableCommands.put("type", new TypeCommand());
        availableCommands.put("uncheck", new CheckCommand());
        availableCommands.put("slide", new SlideCommand());
    }

    @Override
    public Object invoke(Object proxy, Method proxyMethod, Method pageMethod,
            Object[] args) throws Throwable {
        Class<?> returnType = (pageMethod.getReturnType());
        if (returnType.equals(Action.class)) {
            Action action = (Action) pageMethod.invoke(proxy, args);
            for (Task task : action.getTasks()) {
                execute(task);
            }
            return action;
        } else {
            Object result = null;
            try {
                result = pageMethod.invoke(proxy, args);
            } catch (Exception e) {
                throw e.getCause();
            }
            return result;
        }
    }

    private void execute(Task task) throws Exception {
        Command command = availableCommands.get(task.getId());
        if (command != null) {
            log.debug("{}.{} ({})", task.getAction(), task.getId(), task);
            command.execute(driver, task);
            driver.pause();
        } else {
            log.error("{}.{} NOT implemented! ({})", task.getAction(),
                    task.getId(), task);
            throw new RuntimeException("Command not implemented!");
        }
    }

}