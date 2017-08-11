package org.minecraftshire.auth.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.minecraftshire.auth.data.AuthTokenData;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Aspect
public class AuthAspect {

    private TokenRepository tokens;

    @Autowired
    public AuthAspect(TokenRepository tokens) {
        this.tokens = tokens;
    }


    @Around("execution(public * *(..)) && @annotation(org.minecraftshire.auth.aspects.AuthRequired)")
    public Object fire(ProceedingJoinPoint pjp) throws Throwable {
        Method method = AuthAspect.firstMethod(pjp.getThis(), pjp.getSignature().getName());
        Object[] args = pjp.getArgs();

        AuthTokenData tokenData = null;
        String userAgent = null;
        Integer sessionIndex = null;

        int i = 0;
        for (Object arg: args) {

            if (tokenData == null && arg instanceof AuthTokenData) {

                tokenData = (AuthTokenData) arg;

            } else if (userAgent == null && arg instanceof String) {

                RequestHeader header = (RequestHeader) AuthAspect.getArgAnnotated(method, i, RequestHeader.class);
                String value;

                try {
                    value = header.value();

                    if (value.equalsIgnoreCase("User-Agent")) {
                        userAgent = (String) arg;
                    }
                } catch (NullPointerException e) {
                    value = null;
                }

            } else if (sessionIndex == null && arg instanceof SessionData) {

                sessionIndex = i;
            }

            i++;
        }

        if (tokenData == null) {
            Logger.getLogger().warning(
                    "Malformed arguments of method ", pjp.getSignature().getName(),
                    ". Authorization aborted."
            );

            throw new UnauthorizedException();
        }

        SessionData sessionData = tokens.verifyAuthToken(tokenData.getAuthToken(), userAgent);

        if (sessionIndex != null) {
            args[sessionIndex] = sessionData;
        } else {
            Logger.getLogger().warning(
                    "Method ", pjp.getSignature().getName(),
                    " requested authorization but doesn't have a SessionData argument in it's formal",
                    " parameter list."
            );
        }

        return pjp.proceed(args);
    }


    private static Method firstMethod(Object targetThis, String name) {
        for (Method method: targetThis.getClass().getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }

        return null;
    }


    private static Annotation getArgAnnotated(Method method, int idx, Class<? extends Annotation> annotation) {
        for (Annotation argAnnotation: method.getParameterAnnotations()[idx]) {
            if (argAnnotation.annotationType().equals(annotation)) {
                return argAnnotation;
            }
        }

        return null;
    }

}
