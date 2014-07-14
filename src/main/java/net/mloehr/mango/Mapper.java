package net.mloehr.mango;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Data
@RequiredArgsConstructor
public class Mapper {

    @NonNull
    private Object target;
    @NonNull
    private String fieldName;
    @NonNull
    private String pattern;

    private String filter(String source) {
        val r = Pattern.compile(pattern);
        val m = r.matcher(source);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public void map(String text) {
        try {
            val field = target.getClass()
                .getDeclaredField(fieldName);
            field.setAccessible(true);
            if (isList(field)) {
                field.set(target, filteredLines(text));
            } else {
                field.set(target, filter(text));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isList(final java.lang.reflect.Field field) {
        return "List".equals(field.getType()
            .getSimpleName());
    }

    private List<String> filteredLines(String text) {
        val result = new ArrayList<String>();
        for (val line : text.split("\\n")) {
            val filtered = filter(line);
            if (filtered.length() > 0) {
                result.add(filtered);
            }
        }
        return result;
    }
}
