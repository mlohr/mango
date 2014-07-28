package net.mloehr.mango.action;

import net.mloehr.mango.Mapper;
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

    private Object data;
    
    private Mapper mapper;
    
    private Result result = new Result();

	public void setResultValue(Object result) {
		this.result.setValue(result);
	}

}
