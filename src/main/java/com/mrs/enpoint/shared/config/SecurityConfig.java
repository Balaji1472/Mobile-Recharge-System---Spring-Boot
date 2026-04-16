//package com.mrs.enpoint.shared.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.mrs.enpoint.shared.security.JwtAuthFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//	private final JwtAuthFilter jwtAuthFilter;
//
//	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
//		this.jwtAuthFilter = jwtAuthFilter;
//	}
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
//
//				.requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/logout").permitAll()
//
//				.requestMatchers(HttpMethod.GET, "/plans/**", "/offers/**", "/operators/**", "/categories/**")
//				.permitAll()
//
//				.requestMatchers("/recharges/**").authenticated()
//
//				.requestMatchers("/payments/**").authenticated()
//
//				.requestMatchers("/transactions/**").authenticated()
//
//				.requestMatchers("/notifications/**").authenticated()
//
//				.requestMatchers("/invoices/**").authenticated()
//
//				.requestMatchers("/saved-numbers/**").authenticated()
//
//				.requestMatchers("/refunds/**").authenticated()
//
//				.requestMatchers("/roles/**").authenticated()
//
//				.requestMatchers("/analytics/**").authenticated()
//
//				.anyRequest().authenticated())
//				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//	}
//}


package com.mrs.enpoint.shared.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mrs.enpoint.shared.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ✅ Password Encoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Main Security Config
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // ✅ Enable CORS (CRITICAL)
            .cors(cors -> {})

            // ✅ Stateless (for JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // ✅ Allow preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Public Auth APIs
                .requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/logout").permitAll()

                // ✅ Public GET APIs
                .requestMatchers(HttpMethod.GET, "/plans/**", "/offers/**", "/operators/**", "/categories/**").permitAll()

                // ✅ Protected APIs
                .requestMatchers("/recharges/**").authenticated()
                .requestMatchers("/payments/**").authenticated()
                .requestMatchers("/transactions/**").authenticated()
                .requestMatchers("/notifications/**").authenticated()
                .requestMatchers("/invoices/**").authenticated()
                .requestMatchers("/saved-numbers/**").authenticated()
                .requestMatchers("/refunds/**").authenticated()
                .requestMatchers("/roles/**").authenticated()
                .requestMatchers("/analytics/**").authenticated()

                // ✅ Everything else secured
                .anyRequest().authenticated()
            )

            // ✅ JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ GLOBAL CORS CONFIG (REAL FIX)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173")); // frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}