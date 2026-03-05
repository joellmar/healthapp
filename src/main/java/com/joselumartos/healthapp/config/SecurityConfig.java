package com.joselumartos.healthapp.config;

import com.joselumartos.healthapp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 1. Desactivamos CSRF con lambda
            .csrf(csrf -> csrf.disable())

            // 2. Configuramos la sesión como stateless con lambda
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Configuramos las rutas con lambda
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/register").permitAll() // Rutas públicas
                    .requestMatchers("/medico/**").hasRole("MEDICO") // Rutas para médicos
                    .requestMatchers("/paciente/**").hasRole("PACIENTE")
                    .requestMatchers("/citas/**").authenticated() // Ambos pueden entrar a sus citas
                    .anyRequest().authenticated() // El resto, con token obligatorio
            )

            .logout(logout -> logout
                    .logoutUrl("/logout") // La URL que dispara el logout
                    .logoutSuccessUrl("/login?logout") // Dónde ir tras salir
                    .deleteCookies("JWT", "JSESSIONID") // Borra la cookie del token automáticamente
                    .invalidateHttpSession(true)
            )
            // 4. Añadimos el filtro personalizado antes del filtro por defecto
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
