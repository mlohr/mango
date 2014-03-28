package net.mloehr.mango;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.mloehr.mango.commands.ClickCommand;
import net.mloehr.mango.commands.Command;
import net.mloehr.mango.commands.TypeCommand;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionHandler implements InvocationHandler {
    /**
     * 
     */
    private final DriveSupport driver;
    private Object             target;
    HashMap<String, Command>   availableCommands;

    public ActionHandler(DriveSupport driver, Object target) {
        this.driver = driver;
        this.target = target;

        availableCommands = new HashMap<String, Command>();
        availableCommands.put("type", new TypeCommand());
        availableCommands.put("click", new ClickCommand());
    }

    private void execute(Task task) {
        val command = availableCommands.get(task.getId());
        if (command != null) {
            log.info("{}.{} ({})", task.getAction(), task.getId(), task);
            command.execute(driver, task);
        } else {
            log.warn("{}.{} NOT implemented! ({})", task.getAction(), task.getId(), task);
        }

    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int argumentsCount = 0;
        if (args != null) {
            argumentsCount = args.length;
        }
        val found = findTargetMethod(method, argumentsCount);
        // perform(action);
        log.info("{}:", method.getName());
        Action action = (Action) found.invoke(target, args);
        for (val task : action.getTasks()) {
            execute(task);
        }
        return action;
    }

    private Method findTargetMethod(Method method, int argumentsCount) {
        for (val m : target.getClass()
            .getMethods()) {

            if (m.getName()
                .equals(method.getName()) && m.getParameterTypes().length == argumentsCount) {
                return m;
            }
        }
        throw new RuntimeException("Method " + method.getName() + " not found for class "
                + target.getClass()
                    .getName());
    }
}