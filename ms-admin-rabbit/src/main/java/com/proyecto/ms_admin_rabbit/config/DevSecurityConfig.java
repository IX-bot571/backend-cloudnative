package com.proyecto.ms_admin_rabbit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** TEMPORAL: solo con el perfil "sinjwt". Se reemplaza por validacion JWT en la Fase 5. */
@Configuration
@Profile("sinjwt")
public class DevSecurityConfig {

    @Bean
    SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(a -> a.anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
