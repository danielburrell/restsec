package uk.co.solong.restsec.core.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import uk.co.solong.restsec.core.annotations.EntitlementAspect;
import uk.co.solong.restsec.core.loader.AbstractRestSecLoader;
import uk.co.solong.restsec.core.sessions.SessionManager;

@Configuration
@EnableAspectJAutoProxy
public class EntitlementAspectConfig {

    @Inject
    private SessionManager sessionManager;

    @Inject
    private AbstractRestSecLoader loader;

    @Bean
    public EntitlementAspect entitlementAspect() {
        return new EntitlementAspect(sessionManager, loader);
    }
}
