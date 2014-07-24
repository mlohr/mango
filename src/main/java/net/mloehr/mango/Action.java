package net.mloehr.mango;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Action {

    @NonNull
    private String name;

    @Getter
    private List<Task> tasks = new ArrayList<Task>();

    /**
     * types 'text' into web-element identified by 'xpath'
     */
    public Action type(String xpath, String text) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(text)
                .build());
        return this;
    }

    /**
     * clicks on web-element identified by 'xpath'
     */
    public Action click(String xpath) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .build());
        return this;
    }

    /**
     * execute javascript 'script'
     */
    public Action execute(String script) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath("")
                .text(script)
                .build());
        return this;
    }


    /**
     * execute javascript 'script' on web-element identified by 'xpath'
     */
    public Action executeOnElement(String xpath, String script) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(script)
                .build());
        return this;
    }

    /**
     * selects 'item' on drop-down web-element identified by 'xpath'
     */
    public Action select(String xpath, String item) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(item)
                .build());
        return this;
    }

    /**
     * test if the web-element identified by 'xpath', is visible
     */          
    public Action testVisibility(String xpath, Object visible) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .data(visible)
                .build());
        return this;
    }
    
    /**
     * maps the text into 'data', using the {@link Mapper}, of web-element identified by 'xpath'
     */
    public Action mapText(String xpath, Object data, Mapper mapper) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .data(data)
                .mapper(mapper)
                .build());
        return this;
    }

    
    /**
     * gets the text into 'data', of web-element identified by 'xpath'
     */
    public Action getText(String xpath, Object data) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .data(data)
                .build());
        return this;
    }

    public static Action withTasks() {
        return new Action(new Exception().getStackTrace()[1].getMethodName());
    }

    private String getTaskName() {
        return new Exception().getStackTrace()[1].getMethodName();
    }

}