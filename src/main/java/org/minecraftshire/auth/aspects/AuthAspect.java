package org.minecraftshire.auth.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.minecraftshire.auth.data.AuthTokenData;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.utils.AuthTargets;


@Aspect
public class AuthAspect {

    public AuthAspect() {
    }

    @Around("execution(public * *(..)) && @annotation(org.minecraftshire.auth.aspects.AuthRequired)")
    public Object fire(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        if (args.length < 3 || !(args[0] instanceof AuthTokenData) || !(args[1] instanceof String)) {
            throw new UnauthorizedException();
        }

        return pjp.proceed(new Object[]{
                args[0],
                args[1],
                UserRepository.verifyAuthToken(
                        ((AuthTokenData) args[0]).getAuthToken(),
                        args[1].toString()
                )
        });
    }

}
