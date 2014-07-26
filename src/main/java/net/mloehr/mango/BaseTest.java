package net.mloehr.mango;

import java.lang.reflect.InvocationTargetException;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import net.mloehr.mango.action.ActionHandler;
import net.mloehr.mango.selenium.WebUser;

import org.junit.After;

public class BaseTest {

    protected WebUser webUser;

    protected <T> T on(Class<T> pageClass) {
    	return perform(pageClass, new ActionHandler(webUser));
    }

    @SuppressWarnings("unchecked")
    protected <T> T perform(Class<T> pageClass, MethodHandler methodHandler) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(pageClass);
        try {
            return (T) proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
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
