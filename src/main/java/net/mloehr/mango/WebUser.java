package net.mloehr.mango;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

@Slf4j
public class WebUser implements DriveSupport {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebUser.class);
	 
    private WebDriver driver;
    private Timer timer;

    public WebUser(String url) {
    	this(url, "");
    }
    
    public WebUser(String url, String options) {
    	Properties preferences = new Properties();
    	parseOptions(preferences, options);
	    driver = new FirefoxDriver(useExtensionsAndAcceptUntrustedCertificates(preferences));
        driver.manage().deleteAllCookies();
        driver.get(url);
        timer = new Timer(Timer.TIMEOUT_IN_SECONDS);
    }

	private void parseOptions(Properties preferences, String options) {
		if (options != null && options != "") {
    		for (val entry: options.split(";")) {
    			val pair = entry.split("=");
    			preferences.setProperty(pair[0], pair[1]);
    		}
    	}
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

    private FirefoxProfile useExtensionsAndAcceptUntrustedCertificates(Properties preferences) {
    	FirefoxProfile profile = new FirefoxProfile();
    	addExtensions(profile, preferences);
    	for (val pref: preferences.entrySet()) {
    		profile.setPreference(pref.getKey().toString(), pref.getValue().toString());
    	}
    	profile.setAcceptUntrustedCertificates(true);
    	return profile;
    }

	private void addExtensions(FirefoxProfile profile, Properties preferences) {
		File resources = new File("./resources");
    	String[] files = resources.list();
    	for (String file : files) {
    		if (! file.endsWith(".xpi")) {
    			continue;
    		}
    		val extension = new File(resources+"/"+file);
			String[] parsed = parseNameAndVersion(extension).split(";");
			final String name = parsed[0];
			final String version = parsed[1];
			try {
				profile.addExtension(extension);
				preferences.setProperty("extensions."+name+".currentVersion", version);					
				logger.info("added extension {} {}", name, version);
			} catch (IOException e) {
				logger.warn("adding extension {}, unexpected: {}",name, e);
			}    		
    	}
		
	}

	private String parseNameAndVersion(File file) {
		String result = "";
		Pattern pattern = Pattern.compile("(\\w+)-([\\d\\.]+)-*\\w*\\.xpi");
    	Matcher matcher = pattern.matcher(file.getName()); 
    	if (matcher.find()) {
    		result = matcher.group(1)+";"+matcher.group(2);
    	}
		return result;
	}

}