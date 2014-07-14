package net.mloehr.mango;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Delegate;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

@Slf4j
public class WebUser implements DriveSupport {

    private WebDriver driver;
    private Timer timer;

    public WebUser(String url) {
    	this(url, "");
    }
    
    public WebUser(String url, String options) {
    	Properties preferences = new Properties();
    	if (options != null && options != "") {
    		for (val entry: options.split(";")) {
    			val pair = entry.split("=");
    			preferences.setProperty(pair[0], pair[1]);
    		}
    	}
	    driver = new FirefoxDriver(useFireBugAndAcceptUntrustedCertificates(preferences));
        driver.manage().deleteAllCookies();
        driver.get(url);
        timer = new Timer(Timer.TIMEOUT_IN_SECONDS);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void quit() {
        driver.quit();
    }

    @Override
    public WebElement forThis(String xpath) {
        waitForThis(xpath);
        return driver.findElement(By.xpath(xpath));
    }

    @Override
    public List<WebElement> forThese(String xpath) {
        waitForThis(xpath);
        return driver.findElements(By.xpath(xpath));
    }
    
    private void waitForThis(String xpath) {
        timer.reset();
        while (timer.isNotExpired()) {
            if (currentPageHas(xpath)) {
                return;
            } else {
                Timer.waitFor(Timer.MILLISECONDS_BETWEEN_ELEMENT_CHECK);
            }
        }
    }

    private boolean currentPageHas(String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } catch (UnhandledAlertException e) {
            log.warn("Ignored alert: " + e.getAlertText());
            return false;
        }
    }

    private FirefoxProfile useFireBugAndAcceptUntrustedCertificates(Properties preferences) {
    	FirefoxProfile profile = new FirefoxProfile();
    	File firebug = getFireBug();
    	String version = "";
    	try {
    		profile.addExtension(firebug);
    	} catch (IOException e) {
    		version = getVersion(firebug);
    	}
    	preferences.setProperty("extensions.firebug.currentVersion", version);
    	for (val pref: preferences.entrySet()) {
    		profile.setPreference(pref.getKey().toString(), pref.getValue().toString());
    	}
    	profile.setAcceptUntrustedCertificates(true);
    	return profile;
    }

	private String getVersion(File firebug) {
		String version = "";
		Pattern pattern = Pattern.compile("firebug-([\\d\\.]+)\\.xpi");
    	Matcher matcher = pattern.matcher(firebug.getName()); 
    	if (matcher.find()) {
    		version = matcher.group(1);
    	}
		return version;
	}

	private File getFireBug() {
		File firebug = null;
		File resources = new File("./resources");
    	String[] files = resources.list();
    	for (String file : files) {
    		if (file.startsWith("firebug")) {
    			firebug = new File(resources+"/"+file);
    			break;
    		}
		}
		return firebug;
	}
}