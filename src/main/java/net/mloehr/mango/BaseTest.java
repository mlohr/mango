package net.mloehr.mango;

import java.lang.reflect.InvocationTargetException;

import javassist.util.proxy.ProxyFactory;
import org.junit.After;

public class BaseTest {

    protected WebUser webUser;

    @SuppressWarnings("unchecked")
    protected <T> T on(Class<T> pageClass) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(pageClass);
        try {
            return (T) proxyFactory.create(new Class<?>[0], new Object[0], new ActionHandler(webUser));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not create proxy for page object.", e);
        }
    }

    @After
    public void closeBrowser() {
        if (webUser != null)
            webUser.quit();
    }

}
