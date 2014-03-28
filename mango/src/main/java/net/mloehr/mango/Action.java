package net.mloehr.mango;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Action {

    @NonNull
    private String     name;

    @Getter
    private List<Task> tasks = new ArrayList<Task>();

    public Action type(String xpath, String text) {
        tasks.add(Task.builder()
            .id(getTaskName())
            .action(name)
            .xpath(xpath)
            .text(text)
            .build());
        return this;
    }

    public Action click(String xpath) {
        tasks.add(Task.builder()
            .id(getTaskName())
            .action(name)
            .xpath(xpath)
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