package org.minecraftshire.auth.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Aspect
public class ProfileAspect {

    @Around("execution(public org.springframework.http.ResponseEntity *(..))")
    public Object fire(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed(pjp.getArgs());
        long end = System.currentTimeMillis();

        ResponseEntity newResult = (ResponseEntity) result;

        if (result != null) {
            String time = String.valueOf(end - start) + "ms";
            ResponseEntity oldResult = newResult;

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

            for (String key: oldResult.getHeaders().keySet()) {
                headers.put(key, oldResult.getHeaders().get(key));
            }

            headers.set("X-Execution-Time", time);

            newResult = new ResponseEntity<>(
                    oldResult.getBody(),
                    headers,
                    oldResult.getStatusCode()
            );
        }

        return newResult;
    }

}
