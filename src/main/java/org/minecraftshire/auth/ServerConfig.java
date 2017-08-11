package org.minecraftshire.auth;


import org.minecraftshire.auth.aspects.AuthAspect;
import org.minecraftshire.auth.aspects.ProfileAspect;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.minecraftshire.auth"})
public class ServerConfig {

    @Bean
    @Autowired
    public AuthAspect getAuthAspect(TokenRepository tokens) {
        return new AuthAspect(tokens);
    }

    @Bean
    public ProfileAspect getProfileAspect() {
        return new ProfileAspect();
    }

}
