package net.mloehr.mango.selenium;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public interface DriveSupport {

    public abstract WebElement forThis(String xpath) throws Exception;

	List<WebElement> forThese(String xpath) throws Exception;

	public abstract Object execute(String script, String xpath) throws Exception;

	public abstract void pause();

    public abstract Actions getActions();

}