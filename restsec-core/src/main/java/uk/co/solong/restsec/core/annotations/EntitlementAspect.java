package uk.co.solong.restsec.core.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.co.solong.restsec.core.exceptions.CannotVerifyAuthenticationException;
import uk.co.solong.restsec.core.exceptions.NotAuthenticatedException;
import uk.co.solong.restsec.core.roles.Role;
import uk.co.solong.restsec.core.sessions.SessionManager;

@Aspect
@Component
public class EntitlementAspect {

    private static final Logger logger = LoggerFactory.getLogger(EntitlementAspect.class);

    private final SessionManager sessionManager;
    private final Map<String,List<String>> entitlementDirectory;
    @Around("execution(* *(..)) && @annotation(uk.co.solong.hatf2.web.annotations.Entitlement)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        for (Object arg:args){
            if (arg instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest)arg;
                if (sessionManager.isLoggedIn(request)){
                    String userId = sessionManager.getUserId(request);
                    if (userId == null) {
                        //if the userid is null (shouldnt ever hit this) make them login again
                        throw new NotAuthenticatedException();
                    }
                    List<String> givenRoles = entitlementDirectory.get(userId);
                    List<String> requiredRoles = extractRoles(point);
                    if (((givenRoles != null) && CollectionUtils.intersection(givenRoles, requiredRoles).size() > 0) || (requiredRoles.size() == 0)) {
                        logger.info("Authorised");
                        Object result = point.proceed();
                        return result;
                    } else {
                        logger.debug("Not Authorised");
                        throw new NoPermissionException();
                    }
                } else {
                    //user is not logged in
                    throw new NotAuthenticatedException();
                }
            }
        }
        //this exception is thrown if there is no httpservletrequest parameter to perform entitlement checks on
        throw new CannotVerifyAuthenticationException();
        
    }

    private List<String> extractRoles(ProceedingJoinPoint point) {
        MethodSignature m = (MethodSignature)point.getSignature();
        Method method = m.getMethod();
        Entitlement e = method.getAnnotation(Entitlement.class);
        List<String> names = new ArrayList<String>();
        for (uk.co.solong.restsec.core.roles.Role role : e.mustbe()) {
            names.add(role.name());
        }
        return names;
    }

    public EntitlementAspect(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.entitlementDirectory = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(Role.ADMIN.name());
        list.add(Role.USER.name());
        entitlementDirectory.put("4003348", list);
        
    }

}
