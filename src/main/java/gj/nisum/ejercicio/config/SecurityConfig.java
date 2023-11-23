package gj.nisum.ejercicio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import gj.nisum.ejercicio.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        AntPathRequestMatcher antMatcherAuth = new AntPathRequestMatcher("/auth/**");
        AntPathRequestMatcher antMatcherDoc = new AntPathRequestMatcher("/doc/**");
        AntPathRequestMatcher antMatcherOA = new AntPathRequestMatcher("/v3/**");
    
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()).disable())
                .headers(headers -> headers.frameOptions(t -> t.sameOrigin()))
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers(antMatcherAuth).permitAll()
                        .requestMatchers(antMatcherDoc).permitAll()
                        .requestMatchers(antMatcherOA).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(
                        sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
