package net.mloehr.mango.selenium;

import java.awt.image.BufferedImage;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public interface DriveSupport {

    public abstract WebElement forThis(String xpath) throws Exception;

    List<WebElement> forThese(String xpath, boolean wait) throws Exception;

    public abstract Object execute(String script, String xpath)
            throws Exception;

    public abstract void pause();

    public abstract Actions getActions();

	public abstract void selectMenuItem(String xpath, String item) throws Exception;
	
	public abstract void focusOnElement(WebElement element);

	public void saveElementImage(String xpath, String path) throws Exception;		

	public BufferedImage getElementImage(String xpath) throws Exception;

	public abstract void switchTo(String text);

	public abstract void waitForPageReady();

}