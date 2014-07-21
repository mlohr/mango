package net.mloehr.mango;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface DriveSupport {

    public abstract WebElement forThis(String xpath) throws Exception;

	List<WebElement> forThese(String xpath) throws Exception;

}