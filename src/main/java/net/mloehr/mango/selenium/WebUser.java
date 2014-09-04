package net.mloehr.mango.selenium;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.mloehr.mango.Timer;
import net.mloehr.mango.XPathNotFoundException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public class WebUser implements DriveSupport {
	 
    private static final String MANGO_BROWSER_HEIGHT = "mango.browser-height";
    private static final String MANGO_BROWSER_MIN_HEIGHT = "mango.browser-min-height";
    private static final String MANGO_BROWSER_WIDTH = "mango.browser-width";
    private static final String MANGO_BROWSER_MIN_WIDTH = "mango.browser-min-width";
    private static final String MANGO_EXECUTION_DELAY = "mango.execution-delay";
    private static final String MANGO_EXECUTION_TRACER = "mango.execution-tracer";
    private static final String MANGO_EXECUTION_RELOAD = "mango.execution-reload";
    private static final String MANGO_EXECUTION_REMOTE = "mango.execution-remote";

    private static final String tracerScript = "arguments[0].style='border: 3px dashed red';";
    private static final String scrollIntoViewScript = "arguments[0].scrollIntoView(true);";

    private WebDriver driver;
    private Timer timer;
	private int executionDelay = 0;
	private boolean showTracer = false;
	
    public WebUser(String url) throws Exception {
    	this(url, "");
    }
    
    public WebUser(String url, String options) throws Exception {
    	timer = new Timer(Timer.TIMEOUT_IN_SECONDS);
    	Properties preferences = new Properties();
    	parseOptions(preferences, options);
    	
	    if (preferences.containsKey(MANGO_EXECUTION_REMOTE)) {
	        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	        capabilities.setCapability("version", "26");
	        capabilities.setCapability(FirefoxDriver.PROFILE, useExtensionsAndAcceptUntrustedCertificates(preferences));
	        capabilities.setCapability("platform", Platform.LINUX);
			driver = new RemoteWebDriver(
			        new URL("http://127.0.0.1:5555/wd/hub"),
			        capabilities);
	    } else {
	    	driver = new FirefoxDriver(useExtensionsAndAcceptUntrustedCertificates(preferences));	    	
	    }
        driver.manage().deleteAllCookies();
        driver.get(url);
        handleMangoProperties(preferences, url);
    }
    
    public void takeScreenShot(String file) throws Exception {
    	val augDriver = new Augmenter().augment(driver); 
    	File screenShot = ((FirefoxDriver) augDriver).getScreenshotAs(OutputType.FILE);	
    	FileUtils.copyFile(screenShot, new File(file));
    }

	public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

	public void goTo(String url) {
         driver.get(url);
    }

	public void goBack() {
         driver.navigate().back();
    }

	public void refreshPage() {
         driver.navigate().refresh();
    }

    public void quit() {
        driver.quit();
    }

    @Override
	public Object execute(String script, String xpath) throws Exception {
    	if (xpath == null || xpath.length() == 0) {
			return ((JavascriptExecutor) driver).executeScript(script);    		
    	} 
        waitForThis(xpath);
        if (!currentPageHas(xpath)) {
        	throw new XPathNotFoundException(xpath);	        	
        }
    	val element = driver.findElement(By.xpath(xpath));
		return ((JavascriptExecutor) driver).executeScript(script, element);    	
	}

	@Override
    public WebElement forThis(String xpath) throws Exception {
        waitForThis(xpath);
        if (currentPageHas(xpath)) {
        	val element = driver.findElement(By.xpath(xpath));
        	executeJavaScript(element);
        	return element;
        }
        throw new XPathNotFoundException(xpath);
    }

	public List<WebElement> forThese(String xpath) throws Exception {
		return forThese(xpath, true);
	}

	@Override
	public List<WebElement> forThese(String xpath, boolean wait)
			throws Exception {
		if (wait) {
			waitForThis(xpath);			
		}
		if (currentPageHas(xpath)) {
			val elements = driver.findElements(By.xpath(xpath));
			executeJavaScript(elements);
			return elements;
		}
		throw new XPathNotFoundException(xpath);
	}

	public void pause() {
		Timer.waitFor(executionDelay);
	}

    /* (non-Javadoc)
     * @see net.mloehr.mango.selenium.DriveSupport#getActions()
     */
    public Actions getActions() {
        return new Actions(driver);
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
	    log.warn("Time-out after {} seconds for xpath {}",timer.getTimeOut(), xpath);
	}

	private void executeJavaScript(List<WebElement> elements) {
		for(val element: elements) {
			executeJavaScript(element);
		}
	}

	private void executeJavaScript(final org.openqa.selenium.WebElement element) {
		((JavascriptExecutor) driver).executeScript(scrollIntoViewScript, element);
		if (showTracer) {
			((JavascriptExecutor) driver).executeScript(tracerScript, element);	    		
		}
	}

	private void handleMangoProperties(Properties preferences, String url) {
		Dimension browserSize = driver.manage().window().getSize();
	    int browserWidth = browserSize.width;
	    int browserHeight = browserSize.height;
	    JavascriptExecutor js = (JavascriptExecutor)driver;
	    int screenWidth = ((Long)js.executeScript("return screen.width")).intValue();
	    int screenHeight = ((Long)js.executeScript("return screen.height")).intValue();
	
	    if (preferences.containsKey(MANGO_BROWSER_HEIGHT)) {
	    	int requestedHeigth = Integer.valueOf(preferences.getProperty(MANGO_BROWSER_HEIGHT)).intValue();
	    	if (requestedHeigth <= screenHeight) {
	    		browserHeight = requestedHeigth;
	    	}
	    }
	    if (preferences.containsKey(MANGO_BROWSER_MIN_HEIGHT)) {
	    	int requestedHeigth = Integer.valueOf(preferences.getProperty(MANGO_BROWSER_MIN_HEIGHT)).intValue();
	    	if (requestedHeigth > browserHeight) {
	    		browserHeight = requestedHeigth;
	    	}
	    }
	    if (preferences.containsKey(MANGO_BROWSER_WIDTH)) {
	    	int requestedWidth = Integer.valueOf(preferences.getProperty(MANGO_BROWSER_WIDTH)).intValue();
	    	if (requestedWidth <= screenWidth) {
	    		browserWidth = requestedWidth;
	    	}
	    }
	    if (preferences.containsKey(MANGO_BROWSER_MIN_WIDTH)) {
	    	int requestedWidth = Integer.valueOf(preferences.getProperty(MANGO_BROWSER_MIN_WIDTH)).intValue();
	    	if (requestedWidth > browserWidth) {
	    		browserWidth = requestedWidth;
	    	}
	    }
		val dim = new Dimension(browserWidth, browserHeight);
		driver.manage().window().setSize(dim);
		
	    if (preferences.containsKey(MANGO_EXECUTION_DELAY)) {
	    	executionDelay = Integer.valueOf(preferences.getProperty(MANGO_EXECUTION_DELAY)).intValue();
	    }
	    if (preferences.containsKey(MANGO_EXECUTION_TRACER)) {
	    	showTracer = Boolean.valueOf(preferences.getProperty(MANGO_EXECUTION_TRACER));
	    }
	    if (preferences.containsKey(MANGO_EXECUTION_RELOAD)) {
	    	if (Boolean.valueOf(preferences.getProperty(MANGO_EXECUTION_RELOAD))) {
	    		val protocol = url.substring(0, url.indexOf("//")+2);
	    		val address = url.substring(url.indexOf("@")+1, url.length());
	    		driver.get(protocol+address);
	    	}
	    }
	}

	private void parseOptions(Properties preferences, String options) {
		if (options != null && options != "") {
			for (val entry: options.split(";")) {
				val pair = entry.split("=");
				preferences.setProperty(pair[0], pair[1]);
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
    	if (files == null) {
    		log.debug("no extensions found");
    		return;
    	}
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
				log.debug("added extension {} {}", name, version);
			} catch (IOException e) {
				log.debug("adding extension {}, unexpected: {}",name, e);
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