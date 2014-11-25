package net.mloehr.mango;

import javassist.util.proxy.ProxyFactory;
import lombok.Getter;
import net.mloehr.mango.action.ActionHandler;
import net.mloehr.mango.selenium.WebUser;

import org.junit.After;

public class BaseTest {

	@Getter
    protected WebUser webUser;

    @SuppressWarnings("unchecked")
    protected <T> T on(Class<T> pageClass) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(pageClass);
        try {
            return (T) proxyFactory.create(new Class<?>[0], new Object[0],
                    new ActionHandler(webUser));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create proxy for page object.", e);
        }
    }

    @After
    public void closeBrowser() {
        if (webUser != null)
            webUser.quit();
    }

}
