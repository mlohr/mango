package net.mloehr.mango.action;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import net.mloehr.mango.Mapper;

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
     * checks checkbox identified by 'xpath' unless checked already
     */
    public Action check(String xpath) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text("check")
                .build());
        return this;
    }

    /**
     * unchecks checkbox identified by 'xpath' if checked
     */
    public Action uncheck(String xpath) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text("uncheck")
                .build());
        return this;
    }

    /**
     * checkes if checkbox identified by 'xpath' if checked
     */
    public Action isChecked(String xpath, Result result) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .result(result)
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
     * clicks on the one web-element identified by 'xpath', when an array
     * can be selected and the web-element is identified by 'index'
     * -> index of 0 clicks on the first element.
     * -> index of 1 clicks on the second element, etc..
     * Negative values start from last element:
     * -> index of -1 clicks on the last element.
     * -> index of -2 clicks on the second-last element, etc..
     */
    public Action click(String xpath, int index) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(String.valueOf(index))
                .build());
        return this;
    }

    /**
     * counts the number on web-elements identified by 'xpath'
     */
    public Action count(String xpath, Result result) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .result(result)
                .build());
        return this;
    }

    /**
     * evalutes javascript 'script' into 'result'
     */
    public Action eval(String script, Result result) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath("")
                .text(script)
                .result(result)
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
     * gets the 'attribute' into 'data', of web-element identified by 'xpath'
     */
    public Action getAttribute(String xpath, String attribute, StringBuilder data) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(attribute)
                .data(data)
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
    
    public Action slide(String xpath, int xCoordinate, int yCoordinate) {
        tasks.add(Task.builder()
                .id(getTaskName())
                .action(name)
                .xpath(xpath)
                .text(String.valueOf(xCoordinate)+","+String.valueOf(yCoordinate))
                .build());
        return this;
    }

}