package net.mloehr.mango;
import java.io.File;

import lombok.Delegate;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

@Slf4j
public class WebUser implements DriveSupport {
    private static final String APPDATA = System.getenv("APPDATA");

    @Delegate
    private WebTasks            tasks   = new WebTasks(this);
    private WebDriver           driver;
    private Timer               timer;

    public WebUser(String url) {
        val profilesDir = APPDATA + "\\Mozilla\\Firefox\\Profiles";

        FirefoxProfile profile = new FirefoxProfile();
        String[] directories = new File(profilesDir).list();
        for (val item : directories) {
            if (item.endsWith("default")) {
                profile = new FirefoxProfile(new File(profilesDir + "\\" + item));
            }
        }
        profile.setAcceptUntrustedCertificates(true);
        driver = new FirefoxDriver(profile);
        driver.manage()
            .deleteAllCookies();
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