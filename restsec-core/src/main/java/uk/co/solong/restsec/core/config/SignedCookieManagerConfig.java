package uk.co.solong.restsec.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.restsec.core.sessions.SignedCookieManager;

import com.springcryptoutils.core.cipher.Mode;
import com.springcryptoutils.core.cipher.symmetric.Base64EncodedCiphererWithStaticKeyImpl;

@Configuration
public class SignedCookieManagerConfig {

    @Value("${encryption.key}")
    private String key;
    @Value("${encryption.iv}")
    private String iv;
    @Value("${domain}")
    private String domain;
    
    @Bean
    public SignedCookieManager signedCookieManager() {
        Base64EncodedCiphererWithStaticKeyImpl encrypt = new Base64EncodedCiphererWithStaticKeyImpl();
        encrypt.setKey(key);
        encrypt.setInitializationVector(iv);
        encrypt.setMode(Mode.ENCRYPT);
        encrypt.afterPropertiesSet();
        
        Base64EncodedCiphererWithStaticKeyImpl decrypt = new Base64EncodedCiphererWithStaticKeyImpl();
        decrypt.setKey(key);
        decrypt.setInitializationVector(iv);
        decrypt.setMode(Mode.DECRYPT);
        decrypt.afterPropertiesSet();
        
        SignedCookieManager signedCookieManager = new SignedCookieManager(domain, encrypt,decrypt);
        return signedCookieManager;
    }
}
