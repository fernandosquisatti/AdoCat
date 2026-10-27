package com.adocat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Site público e consulta pública de animais
                .requestMatchers(HttpMethod.GET, "/", "/index.html", "/favicon.ico", "/api/gatos", "/api/gatos/**").permitAll()
                // Tela de autenticação e endpoint que fornece token CSRF
                .requestMatchers("/admin-login.html", "/api/security/csrf", "/error").permitAll()
                // Área restrita e rotas administrativas
                .requestMatchers("/admin.html", "/api/admin/**").hasRole("ADMIN")
                // Protege também os endpoints legados de alteração e dados pessoais
                .requestMatchers("/usuarios", "/usuarios/**", "/doacoes", "/doacoes/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // Proteção CSRF permanece ATIVADA para POST, PUT e DELETE
            .formLogin(form -> form
                .loginPage("/admin-login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin.html", true)
                .failureUrl("/admin-login.html?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin-login.html?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(
            PasswordEncoder encoder,
            @Value("${APP_ADMIN_USERNAME:}") String username,
            @Value("${APP_ADMIN_PASSWORD:}") String password) {
        // Sem as variáveis, app sobe sem conta admin; nunca usa senha padrão.
        if (username.isBlank() || password.isBlank()) {
            return new InMemoryUserDetailsManager();
        }
        UserDetails admin = User.withUsername(username)
                .password(encoder.encode(password))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
