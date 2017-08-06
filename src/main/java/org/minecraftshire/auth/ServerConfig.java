package org.minecraftshire.auth;


import org.minecraftshire.auth.aspects.AuthAspect;
import org.minecraftshire.auth.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.minecraftshire.auth"})
public class ServerConfig {

    @Bean
    public AuthAspect getAuthAspect() {
        return new AuthAspect(Server.getContext().getBean(UserRepository.class));
    }

}
