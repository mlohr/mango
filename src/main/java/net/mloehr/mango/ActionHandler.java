package net.mloehr.mango;

import javassist.util.proxy.MethodHandler;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.commands.ClickCommand;
import net.mloehr.mango.commands.Command;
import net.mloehr.mango.commands.TypeCommand;

import java.lang.reflect.Method;
import java.util.HashMap;

@Slf4j
public class ActionHandler implements MethodHandler {

    private final DriveSupport driver;
    private final HashMap<String, Command> availableCommands;

    public ActionHandler(DriveSupport driver) {
        this.driver = driver;
        availableCommands = new HashMap<>();
        availableCommands.put("type", new TypeCommand());
        availableCommands.put("click", new ClickCommand());
    }

    @Override
    public Object invoke(Object proxy, Method proxyMethod, Method pageMethod, Object[] args) throws Throwable {
        Action action = (Action) pageMethod.invoke(proxy, args);
        for (Task task : action.getTasks()) {
            execute(task);
        }
        return action;
    }

    private void execute(Task task) {
        Command command = availableCommands.get(task.getId());
        if (command != null) {
            log.info("{}.{} ({})", task.getAction(), task.getId(), task);
            command.execute(driver, task);
        } else {
            log.warn("{}.{} NOT implemented! ({})", task.getAction(), task.getId(), task);
        }
    }

}