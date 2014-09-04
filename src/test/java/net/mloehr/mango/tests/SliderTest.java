/**
 * SliderTest.java
 *
 * Created on 07.08.2014
 *
 * Copyright 2014 Volkswagen AG, All Rights Reserved.
 *
 */
package net.mloehr.mango.tests;

import net.mloehr.mango.BaseTest;
import net.mloehr.mango.Timer;
import net.mloehr.mango.actions.SliderActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;

/**
 * @author Matthias Britten
 *
 */
public class SliderTest extends BaseTest {

    private final static String URL = "http://www.elated.com/res/File/articles/development/javascript/jquery/drag-and-drop-with-jquery-your-essential-guide/draggable.html";
    
    @Test
    public void testCheck() throws Exception {
        webUser = new WebUser(URL);
        
        on(slider()).slide(100, 800);
    }
    
    public static Class<SliderActions> slider() {
        return SliderActions.class;
    }
    
}
