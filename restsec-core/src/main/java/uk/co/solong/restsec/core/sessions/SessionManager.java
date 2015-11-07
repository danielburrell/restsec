package uk.co.solong.restsec.core.sessions;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import uk.co.solong.restsec.core.cookies.DecryptedCookie;

public class SessionManager {
    private final SignedCookieManager signedCookieManager;

    public SessionManager(SignedCookieManager signedCookieManager) {
        this.signedCookieManager = signedCookieManager;
    }

    public boolean isLoggedIn(HttpServletRequest request) {
        DecryptedCookie cookie = signedCookieManager.decryptCookieFromRequest(request);
        Boolean loggedIn = cookie.getLoggedIn();
        Long expiry = cookie.getExpiry();
        DateTime now = new DateTime();
        DateTime dateExpiry = new DateTime(expiry);
        return ((loggedIn != null && expiry != null) && loggedIn && (dateExpiry.isAfter(now)));
    }

    public String getUserId(HttpServletRequest request) {
        DecryptedCookie cookie = signedCookieManager.decryptCookieFromRequest(request);
        return cookie.getUserId();
    }

    
    
}
