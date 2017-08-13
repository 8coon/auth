package org.minecraftshire.auth.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.minecraftshire.auth.data.AuthTokenData;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class AuthAspect {

    private TokenRepository tokens;

    @Autowired
    public AuthAspect(TokenRepository tokens) {
        this.tokens = tokens;
    }


    @Around("execution(public * *(..)) && @annotation(org.minecraftshire.auth.aspects.AuthRequired)")
    public Object fire(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        AuthTokenData tokenData = null;
        String userAgent = null;
        Integer sessionIndex = null;

        int i = 0;
        for (Object arg: args) {

            if (tokenData == null && arg instanceof AuthTokenData) {

                tokenData = (AuthTokenData) arg;

            } else if (userAgent == null && arg instanceof UserAgent) {

                userAgent = arg.toString();

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

}
