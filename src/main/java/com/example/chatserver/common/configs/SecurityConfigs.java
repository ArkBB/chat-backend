package com.example.chatserver.common.configs;

import com.example.chatserver.common.jwt.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigs {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;

    public SecurityConfigs(AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.objectMapper = objectMapper;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain AuthFilterChain(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);

        LoginFilter loginFilter = new LoginFilter(objectMapper);
        loginFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)//csrf 방지 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)//HTTP Basic 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a->a.requestMatchers("/member/create","/member/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션방식 사용하지 않음.
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cs = new CorsConfiguration();
        cs.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        cs.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        cs.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        cs.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cs);
        return request -> cs;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
