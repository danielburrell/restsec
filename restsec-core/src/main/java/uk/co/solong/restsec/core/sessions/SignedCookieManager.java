package uk.co.solong.restsec.core.sessions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import uk.co.solong.restsec.core.cookies.DecryptedCookie;
import uk.co.solong.restsec.core.cookies.EncryptedCookie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcryptoutils.core.cipher.symmetric.Base64EncodedCiphererWithStaticKeyImpl;

public final class SignedCookieManager {

    private final Base64EncodedCiphererWithStaticKeyImpl encrypter;
    private final Base64EncodedCiphererWithStaticKeyImpl decrypter;
    private final String domain;
    
    public final DecryptedCookie decryptCookie(EncryptedCookie encryptedCookie) {
        try {
            ObjectMapper m = new ObjectMapper();
            DecryptedCookie decryptedCookie = m.readValue(decrypt(encryptedCookie.getEncryptedContent()), DecryptedCookie.class);
            return decryptedCookie;
        } catch (Throwable t) {
            DecryptedCookie decryptedCookie = new DecryptedCookie();
            return decryptedCookie;
        }
    }

    public final DecryptedCookie decryptCookieFromRequest(HttpServletRequest request) {
        try {
            Cookie[] cookieList = request.getCookies();
            for (Cookie cookie: cookieList) {
                if ("data".equals(cookie.getName())) {
                    return decryptCookie(new EncryptedCookie(cookie.getValue(), domain));
                }
            }
            return new DecryptedCookie();
        } catch (Throwable t) {
            DecryptedCookie decryptedCookie = new DecryptedCookie();
            return decryptedCookie;
        }
    }

  

    public final EncryptedCookie encryptCookie(DecryptedCookie decryptedCookie) {
        ObjectMapper m = new ObjectMapper();
        try {
            String encryptedData = encrypt(m.writeValueAsString(decryptedCookie));
            return new EncryptedCookie(encryptedData, domain);
        } catch (JsonProcessingException e) {
            return new EncryptedCookie("", domain);
        }
    }

    private String decrypt(String encryptedContent) {
        String encryptedBytes = decrypter.encrypt(encryptedContent);
        return encryptedBytes;
    }
    
    private String encrypt(String plainText) {
        String decryptedBytes = encrypter.encrypt(plainText);
        return decryptedBytes;
    }

    public SignedCookieManager(String domain, Base64EncodedCiphererWithStaticKeyImpl encrypter, Base64EncodedCiphererWithStaticKeyImpl decrypter) {
        this.encrypter = encrypter;
        this.decrypter = decrypter;
        this.domain = domain;
    }
}
