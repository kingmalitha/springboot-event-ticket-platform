package com.kingmalitha.springbooteventticketplatform.config;

import com.kingmalitha.springbooteventticketplatform.filters.UserProvisioningFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserProvisioningFilter userProvisioningFilter,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/api/v1" +
                                        "/published-events/**").permitAll()
                                .requestMatchers("/api/v1/events/**").hasRole("ORGANIZER")
                                .requestMatchers("api/v1/ticket-validations").hasRole("STAFF")
                                // CATCH ALL ROUTES
                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(
                        jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                ))
                .addFilterAfter(userProvisioningFilter,
                        BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
