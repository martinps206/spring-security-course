package com.martinps.config.security;

import com.martinps.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf( csrfConfig -> csrfConfig.disable() )
                .sessionManagement( sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests( authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());

                    authConfig.anyRequest().denyAll();
                });

        return httpSecurity.build();
    }
}
