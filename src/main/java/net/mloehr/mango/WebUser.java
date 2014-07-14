package net.mloehr.mango;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

@Slf4j
public class WebUser implements DriveSupport {

    private WebDriver driver;
    private Timer timer;

    public WebUser(String url) {
	    driver = new FirefoxDriver(useFireBugAndAcceptUntrustedCertificates());
        driver.manage().deleteAllCookies();
        driver.get(url);
        timer = new Timer(Timer.TIMEOUT_IN_SECONDS);
    }

	public String getVersion(File firebug) {
		String version = "";
		Pattern pattern = Pattern.compile("firebug-([\\d\\.]+)\\.xpi");
    	Matcher matcher = pattern.matcher(firebug.getName()); 
    	if (matcher.find()) {
    		version = matcher.group(1);
    	}
		return version;
	}

	public File getFireBug() {
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

    private FirefoxProfile useFireBugAndAcceptUntrustedCertificates() {
    	FirefoxProfile profile = new FirefoxProfile();
    	File firebug = getFireBug();
    	if (firebug != null) {
    		try {
				profile.addExtension(firebug);
			} catch (IOException e) {
			}
    		profile.setPreference("extensions.firebug.currentVersion", getVersion(firebug));
    	}
        profile.setAcceptUntrustedCertificates(true);
        return profile;
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

}