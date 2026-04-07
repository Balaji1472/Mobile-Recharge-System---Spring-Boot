package com.mrs.enpoint.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mrs.enpoint.shared.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

				.requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/logout").permitAll()

				.requestMatchers(HttpMethod.GET, "/plans/**", "/offers/**", "/operators/**", "/categories/**")
				.permitAll()

				.requestMatchers("/recharges/**").authenticated()

				.requestMatchers("/payments/**").authenticated()

				.requestMatchers("/transactions/**").authenticated()

				.requestMatchers("/notifications/**").authenticated()

				.requestMatchers("/invoices/**").authenticated()

				.requestMatchers("/saved-numbers/**").authenticated()

				.requestMatchers("/refunds/**").authenticated()

				.requestMatchers("/roles/**").authenticated()

				.requestMatchers("/analytics/**").authenticated()

				.anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
