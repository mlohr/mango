package net.mloehr.mango;

import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.util.proxy.MethodHandler;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.commands.ClickCommand;
import net.mloehr.mango.commands.Command;
import net.mloehr.mango.commands.SelectCommand;
import net.mloehr.mango.commands.TextCommand;
import net.mloehr.mango.commands.TypeCommand;
import net.mloehr.mango.commands.VisibilityCommand;

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
        availableCommands.put("getText", new TextCommand());
        availableCommands.put("testVisibility", new VisibilityCommand());
        availableCommands.put("select", new SelectCommand());
    }

    @Override
    public Object invoke(Object proxy, Method proxyMethod, Method pageMethod, Object[] args) throws Throwable {
        Action action = (Action) pageMethod.invoke(proxy, args);
        for (Task task : action.getTasks()) {
            execute(task);
        }
        return action;
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