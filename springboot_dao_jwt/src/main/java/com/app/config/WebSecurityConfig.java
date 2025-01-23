package com.app.config;

import com.app.filter.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.filter.JwtRequestFilter;
import com.app.service.MyUserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver exceptionResolver;
	
	@Bean
	public JwtRequestFilter jwtRequestFilter() {
		return new JwtRequestFilter(exceptionResolver);
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(myUserDetailsService);
		dao.setPasswordEncoder(encoder());
		return dao;
	}
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request
						.requestMatchers("/api/register", "/api/login", "/api/welcome").permitAll()
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/customer/**").hasRole("CUSTOMER")
						.requestMatchers("/api/view/**").hasAnyRole("ADMIN", "CUSTOMER")
						.anyRequest().authenticated())
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
				.build();
	}
}
