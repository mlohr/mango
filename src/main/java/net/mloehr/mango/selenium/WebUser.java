package net.mloehr.mango.selenium;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

@Slf4j
public class WebUser implements DriveSupport {

	private static final String MANGO_ADD_EXTENSIONS = "mango.extensions";
	private static final String MANGO_BROWSER_HEIGHT = "mango.browser-height";
	private static final String MANGO_BROWSER_MIN_HEIGHT = "mango.browser-min-height";
	private static final String MANGO_BROWSER_WIDTH = "mango.browser-width";
	private static final String MANGO_BROWSER_MIN_WIDTH = "mango.browser-min-width";
	private static final String MANGO_EXECUTION_DELAY = "mango.execution-delay";
	private static final String MANGO_EXECUTION_TRACER = "mango.execution-tracer";
	private static final String MANGO_TIMEOUT = "mango.timeout";

	private static final String tracerScript = "arguments[0].style='border: 3px dashed red';";
	private static final String scrollIntoViewScript = "arguments[0].scrollIntoView(false);";

	private WebDriver driver;
	private Timer timer;
	private int executionDelay = 0;
	private boolean showTracer = false;
	private int timeoutInSeconds = Timer.TIMEOUT_IN_SECONDS;
	private boolean addExtensions = true;

	public WebUser(String url) throws Exception {
		this(null, url, "");
	}

	public WebUser(String url, String options) throws Exception {
		this(null, url, options);
	}

	public WebUser(WebDriver aDriver, String options) throws Exception {
		this(aDriver, "", options);
	}

	public WebUser(WebDriver aDriver, String url, String options)
			throws Exception {
		Properties preferences = new Properties();
		parseOptions(preferences, options);
		preWebDriverActions(preferences);
		if (aDriver == null) {
			driver = new FirefoxDriver(
					useExtensionsAndAcceptUntrustedCertificates(preferences));
			driver.manage().deleteAllCookies();
			driver.manage().window().setPosition(new Point(0, 0));
		} else {
			driver = aDriver;
		}
		if (!url.equals("")) {
			driver.get(url);
		}
		postWebDriverActions(preferences);
		timer = new Timer(timeoutInSeconds);
	}

	/**
	 * Takes a screenshot from current browser content
	 * 
	 * @param name
	 *            filename with or without path
	 * @throws IOException
	 */
	public void takeScreenShot(String name) throws IOException {
		File shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(shot, new File(name));
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
		try { // skipping optional popup
			driver.switchTo().alert().accept();
		} catch (Exception e) {
		}
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

	/*
	 * (non-Javadoc)
	 * 
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
		log.warn("Time-out after {} seconds for xpath {}", timer.getTimeOut(),
				xpath);
	}

	private void executeJavaScript(List<WebElement> elements) {
		for (val element : elements) {
			executeJavaScript(element);
		}
	}

	private void executeJavaScript(final org.openqa.selenium.WebElement element) {
		((JavascriptExecutor) driver).executeScript(scrollIntoViewScript,
				element);
		if (showTracer) {
			((JavascriptExecutor) driver).executeScript(tracerScript, element);
		}
	}

	private void preWebDriverActions(Properties preferences) {
		if (preferences.containsKey(MANGO_ADD_EXTENSIONS)) {
			addExtensions = Boolean.valueOf(preferences
					.getProperty(MANGO_ADD_EXTENSIONS));
		}

	}

	private void postWebDriverActions(Properties preferences) {
		// Dimension browserSize = driver.manage().window().getSize();
		// int browserWidth = browserSize.width;
		// int browserHeight = browserSize.height;
		// JavascriptExecutor js = (JavascriptExecutor) driver;
		// int screenWidth = ((Long) js.executeScript("return screen.width"))
		// .intValue();
		// int screenHeight = ((Long) js.executeScript("return screen.height"))
		// .intValue();
		//
		// if (preferences.containsKey(MANGO_BROWSER_HEIGHT)) {
		// int requestedHeigth = Integer.valueOf(
		// preferences.getProperty(MANGO_BROWSER_HEIGHT)).intValue();
		// if (requestedHeigth <= screenHeight) {
		// browserHeight = requestedHeigth;
		// }
		// }
		// if (preferences.containsKey(MANGO_BROWSER_MIN_HEIGHT)) {
		// int requestedHeigth = Integer.valueOf(
		// preferences.getProperty(MANGO_BROWSER_MIN_HEIGHT))
		// .intValue();
		// if (requestedHeigth > browserHeight) {
		// browserHeight = requestedHeigth;
		// }
		// }
		// if (preferences.containsKey(MANGO_BROWSER_WIDTH)) {
		// int requestedWidth = Integer.valueOf(
		// preferences.getProperty(MANGO_BROWSER_WIDTH)).intValue();
		// if (requestedWidth <= screenWidth) {
		// browserWidth = requestedWidth;
		// }
		// }
		// if (preferences.containsKey(MANGO_BROWSER_MIN_WIDTH)) {
		// int requestedWidth = Integer.valueOf(
		// preferences.getProperty(MANGO_BROWSER_MIN_WIDTH))
		// .intValue();
		// if (requestedWidth > browserWidth) {
		// browserWidth = requestedWidth;
		// }
		// }
		// val dim = new Dimension(browserWidth, browserHeight);
		// driver.manage().window().setSize(dim);
		//
		// if (preferences.containsKey(MANGO_EXECUTION_DELAY)) {
		// executionDelay = Integer.valueOf(
		// preferences.getProperty(MANGO_EXECUTION_DELAY)).intValue();
		// }
		// if (preferences.containsKey(MANGO_EXECUTION_TRACER)) {
		// showTracer = Boolean.valueOf(preferences
		// .getProperty(MANGO_EXECUTION_TRACER));
		// }
		// if (preferences.containsKey(MANGO_TIMEOUT)) {
		// timeoutInSeconds = Integer.valueOf(preferences
		// .getProperty(MANGO_TIMEOUT));
		// }
	}

	private void parseOptions(Properties preferences, String options) {
		if (options != null && options != "") {
			for (val entry : options.split(";")) {
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

	private FirefoxProfile useExtensionsAndAcceptUntrustedCertificates(
			Properties preferences) {
		FirefoxProfile profile = new FirefoxProfile();
		if (addExtensions)
			addExtensions(profile, preferences);
		for (val pref : preferences.entrySet())
			profile.setPreference(pref.getKey().toString(), pref.getValue()
					.toString());
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
			if (!file.endsWith(".xpi")) {
				continue;
			}
			val extension = new File(resources + "/" + file);
			String[] parsed = parseNameAndVersion(extension).split(";");
			final String name = parsed[0];
			final String version = parsed[1];
			try {
				profile.addExtension(extension);
				preferences.setProperty("extensions." + name
						+ ".currentVersion", version);
				log.debug("added extension {} {}", name, version);
			} catch (IOException e) {
				log.debug("adding extension {}, unexpected: {}", name, e);
			}
		}

	}

	private String parseNameAndVersion(File file) {
		String result = "";
		Pattern pattern = Pattern.compile("(\\w+)-([\\d\\.]+)-*\\w*\\.xpi");
		Matcher matcher = pattern.matcher(file.getName());
		if (matcher.find()) {
			result = matcher.group(1) + ";" + matcher.group(2);
		}
		return result;
	}
}