package net.mloehr.mango;

import org.openqa.selenium.WebElement;

public interface DriveSupport {

    public abstract WebElement forThis(String xpath);

}