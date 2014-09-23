package net.mloehr.mango;

import org.slf4j.LoggerFactory;

public class XPathNotFoundException extends Exception {

    private static final long serialVersionUID = 4445972411189251015L;
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(XPathNotFoundException.class);

    public XPathNotFoundException(String xpath) {
        super(xpath);
        logger.error("XPath not found: {}", xpath);
    }

}
