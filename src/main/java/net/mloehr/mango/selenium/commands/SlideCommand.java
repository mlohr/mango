/**
 * SlideCommand.java
 *
 * Created on 07.08.2014
 *
 * Copyright 2014 Volkswagen AG, All Rights Reserved.
 *
 */
package net.mloehr.mango.selenium.commands;

import lombok.val;
import net.mloehr.mango.action.Task;
import net.mloehr.mango.selenium.DriveSupport;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Matthias Britten
 *
 */
public class SlideCommand implements Command {

    @Override
    public void execute(DriveSupport driver, Task task) throws Exception {
        
        val element = driver.forThis(task.getXpath());
        int xCoordinate = 0, yCoordinate = 0;
        
        if(task.getText() != null && task.getText() != "") {
            
            String[] coordinate =  task.getText().split(",");
            xCoordinate = Integer.valueOf(coordinate[0]).intValue();
            yCoordinate = Integer.valueOf(coordinate[1]).intValue();
        }
        
        
        Actions slide = driver.getActions();
        Action action = slide.dragAndDropBy(element, xCoordinate, yCoordinate).build();
        action.perform();
        
    }

}
