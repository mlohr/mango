package net.mloehr.mango;
import java.lang.reflect.Proxy;

import org.junit.After;

//@Slf4j
public class BaseTest {

    protected WebUser webUser;

    @SuppressWarnings("unchecked")
    protected <T extends Page> T on(Class<T> clazz) {
        Object instance = null;
        try {
            String clazzName = clazz.getName()
                .replaceFirst("\\$Actions", "");
            instance = Class.forName(clazzName)
                .getConstructor()
                .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot find inner Actions interface for class "
                    + clazz.getName());
        }
        // val loadAction = ((Page) inst).load();
        // if (loadAction != null) {
        // getWebUser().perform(loadAction);
        // } else {
        // log.warn("no load action for page " + inst.toString());
        // }

        return (T) Proxy.newProxyInstance(this.getClass()
            .getClassLoader(), new Class<?>[] { clazz }, new ActionHandler(webUser, instance));
    }

    @After
    public void closeBrowser() {
        webUser.quit();
    }

}
