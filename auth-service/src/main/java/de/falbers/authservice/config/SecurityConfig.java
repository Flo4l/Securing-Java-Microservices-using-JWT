package de.falbers.authservice.config;

import de.falbers.jwt.JwtConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Import(JwtConfig.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("DATA_SERVICE", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();

        userDetailsService.createUser(
                User.withUsername("data-service")
                        .password(passwordEncoder.encode("data-service-secret"))
                        .roles("DATA_SERVICE")
                        .build()
        );

        userDetailsService.createUser(
                User.withUsername("user")
                        .password(passwordEncoder.encode("user-secret"))
                        .roles("USER")
                        .build()
        );

        userDetailsService.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin-secret"))
                        .roles("USER", "ADMIN")
                        .build()
        );

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
