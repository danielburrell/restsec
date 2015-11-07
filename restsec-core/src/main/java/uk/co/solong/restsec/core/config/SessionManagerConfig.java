package uk.co.solong.restsec.core.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.restsec.core.sessions.SessionManager;
import uk.co.solong.restsec.core.sessions.SignedCookieManager;

@Configuration
public class SessionManagerConfig {

    @Inject
    private SignedCookieManager signedCookieManager;
    
    @Bean
    public SessionManager sessionManager() {
        SessionManager SessionManager = new SessionManager(signedCookieManager);
        return SessionManager;
    }
    
}
