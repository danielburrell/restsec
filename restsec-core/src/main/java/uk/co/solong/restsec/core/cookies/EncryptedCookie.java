package uk.co.solong.restsec.core.cookies;


import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptedCookie extends Cookie {

    /**
     * 
     */
    private static final long serialVersionUID = -6847023526644340419L;
    private static final Logger logger = LoggerFactory.getLogger(EncryptedCookie.class);
    private final String encryptedContent;

    public EncryptedCookie(String encryptedContent, String _domain) {
        super("data",encryptedContent);
        if (!"localhost".equals(_domain)){
            super.setDomain(_domain);
            super.setSecure(true);
        }
        logger.info(encryptedContent);
        this.encryptedContent = encryptedContent;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }
    
    
}
