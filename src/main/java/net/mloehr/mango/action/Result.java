package net.mloehr.mango.action;

import lombok.Data;

@Data
public class Result {

    Object value;

    public int getIntValue() {
        return Integer.parseInt((String) value);
    }

    public boolean getBooleanValue() {
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        } else {
            throw new RuntimeException("Type not supported: "
                    + value.toString());
        }
    }

}
