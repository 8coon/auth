package org.minecraftshire.auth;


import org.minecraftshire.auth.aspects.AuthAspect;
import org.minecraftshire.auth.aspects.ProfileAspect;
import org.minecraftshire.auth.aspects.UserAgentResolver;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;


@Configuration
@EnableAspectJAutoProxy
@EnableWebMvc
@ComponentScan(basePackages = {"org.minecraftshire.auth"})
public class ServerConfig extends WebMvcConfigurerAdapter {

    @Bean
    @Autowired
    public AuthAspect getAuthAspect(TokenRepository tokens) {
        return new AuthAspect(tokens);
    }

    @Bean
    public ProfileAspect getProfileAspect() {
        return new ProfileAspect();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserAgentResolver());
    }

}
