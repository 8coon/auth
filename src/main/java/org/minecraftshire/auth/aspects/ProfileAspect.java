package org.minecraftshire.auth.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.http.ResponseEntity;


@Aspect
public class ProfileAspect {

    @Around("execution(public * *(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object fire(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed(pjp.getArgs());
        long end = System.currentTimeMillis();

        if (result instanceof ResponseEntity) {
            String time = String.valueOf(end - start) + "ms";
            ((ResponseEntity) result).getHeaders().add("X-Execution-Time", time);
        } else {
            Logger.getLogger().warning(
                    "Method ", pjp.getSignature(), " at ", pjp.getSourceLocation().getFileName(),
                    " didn't return ResponseEntity. An object of type ", result.getClass().getName(),
                    " was returned instead."
            );
        }

        return result;
    }

}
