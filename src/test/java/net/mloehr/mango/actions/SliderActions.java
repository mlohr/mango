/**
 * SliderActions.java
 *
 * Created on 07.08.2014
 *
 * Copyright 2014 Volkswagen AG, All Rights Reserved.
 *
 */
package net.mloehr.mango.actions;

import lombok.val;
import net.mloehr.mango.action.Action;
import net.mloehr.mango.action.Result;

/**
 * @author Matthias
 *
 */
public class SliderActions {
    
    public static final String SLIDER = ".//*[@id='makeMeDraggable']";

    public Action slide(int xCoordinate, int yCoordinate) {
        return Action.withTasks().slide(SLIDER, xCoordinate, yCoordinate);
    }
    
}
