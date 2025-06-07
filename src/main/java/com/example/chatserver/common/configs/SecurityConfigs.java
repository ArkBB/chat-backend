package com.example.chatserver.common.configs;

import com.example.chatserver.common.auth.JwtAuthFilter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class SecurityConfigs {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfigs(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain AuthFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)//csrf 방지 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)//HTTP Basic 비활성화
                .authorizeHttpRequests(a->a.requestMatchers("/member/create","/member/login","/connect/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션방식 사용하지 않음.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //요청에 담긴 토큰을 검증하는 과정을 필터 전에 걸기
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
