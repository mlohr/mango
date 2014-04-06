package net.mloehr.mango;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Builder;

@Data
@Builder
public class Task {

    @NonNull
    private String action;

    @NonNull
    private String id;

    private String xpath;

    private String text;

    private Object result;

}
