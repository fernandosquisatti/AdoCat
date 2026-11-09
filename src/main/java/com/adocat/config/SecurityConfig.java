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
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        PathPatternRequestMatcher.Builder path = PathPatternRequestMatcher.withDefaults();

        http
                .authorizeHttpRequests(auth -> auth
                        // Site público
                        .requestMatchers(HttpMethod.GET,
                                "/", "/index.html", "/favicon.ico", "/images/**", "/api/gatos"
                        ).permitAll()

                        // Formulários públicos: inscrição e declaração de contribuição (simulada)
                        .requestMatchers(HttpMethod.POST,
                                "/api/inscricoes", "/api/pagamentos", "/api/assinaturas"
                        ).permitAll()

                        // Login administrativo
                        .requestMatchers("/admin-login.html", "/api/security/csrf", "/error").permitAll()

                        // Todo o resto que mexe com dados é administrativo
                        .requestMatchers(
                                "/admin.html",
                                "/api/admin/**",
                                "/api/gatos/**",
                                "/api/pagamentos/**",
                                "/api/assinaturas/**",
                                "/usuarios", "/usuarios/**",
                                "/doacoes", "/doacoes/**"
                        ).hasRole("ADMIN")
                        .requestMatchers("/api/gatos", "/api/pagamentos", "/api/assinaturas").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // CSRF continua ativo para tudo, exceto os três POSTs públicos acima
                // (o site público não tem sessão). Ações do admin exigem o token.
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        path.matcher(HttpMethod.POST, "/api/inscricoes"),
                        path.matcher(HttpMethod.POST, "/api/pagamentos"),
                        path.matcher(HttpMethod.POST, "/api/assinaturas")
                ))

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

        // Sem as variáveis, a aplicação sobe sem conta admin; nunca usa senha padrão.
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
