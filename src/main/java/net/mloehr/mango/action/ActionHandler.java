package net.mloehr.mango.action;

import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.util.proxy.MethodHandler;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.selenium.DriveSupport;
import net.mloehr.mango.selenium.commands.AttributeCommand;
import net.mloehr.mango.selenium.commands.ClickCommand;
import net.mloehr.mango.selenium.commands.Command;
import net.mloehr.mango.selenium.commands.ExecuteCommand;
import net.mloehr.mango.selenium.commands.SelectCommand;
import net.mloehr.mango.selenium.commands.TextCommand;
import net.mloehr.mango.selenium.commands.TypeCommand;
import net.mloehr.mango.selenium.commands.VisibilityCommand;

@Slf4j
public class ActionHandler implements MethodHandler {
    private final DriveSupport driver;
    private final HashMap<String, Command> availableCommands;

    public ActionHandler(DriveSupport driver) {
        this.driver = driver;
        availableCommands = new HashMap<>();
        availableCommands.put("type", new TypeCommand());
        availableCommands.put("click", new ClickCommand());
        availableCommands.put("mapText", new TextCommand());
        availableCommands.put("getAttribute", new AttributeCommand());
        availableCommands.put("getText", new TextCommand());
        availableCommands.put("testVisibility", new VisibilityCommand());
        availableCommands.put("select", new SelectCommand());
        availableCommands.put("execute", new ExecuteCommand());
        availableCommands.put("executeOnElement", new ExecuteCommand());
    }

    @Override
    public Object invoke(Object proxy, Method proxyMethod, Method pageMethod, Object[] args) throws Throwable {
    	Class<?> returnType = (pageMethod.getReturnType());
    	if( returnType.equals(Action.class)) {
    		Action action = (Action) pageMethod.invoke(proxy, args);
    		for (Task task : action.getTasks()) {
    			execute(task);
    		}
    		return action;    		
    	} else {
    		return (Object) pageMethod.invoke(proxy, args);
    	}
    }

    private void execute(Task task) throws Exception {
        Command command = availableCommands.get(task.getId());
        if (command != null) {
            log.info("{}.{} ({})", task.getAction(), task.getId(), task);
            command.execute(driver, task);
        } else {
            log.warn("{}.{} NOT implemented! ({})", task.getAction(), task.getId(), task);
        }
    }

}