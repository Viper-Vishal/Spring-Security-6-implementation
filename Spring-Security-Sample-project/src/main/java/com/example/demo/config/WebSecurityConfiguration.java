package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CustomUserDetailsService customUserDetailsService;
	
	private final UserDetailsService userDetailsService;
	
	private final JWTAuthenticationFilter jwtAuthenticationFilter;
	
	public WebSecurityConfiguration(UserDetailsService userDetailsService, CustomUserDetailsService customUserDetailsService,@Lazy JWTAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.customUserDetailsService = customUserDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						request -> 
							request
							.requestMatchers("user/register", "user/login")
							.permitAll()
							.anyRequest()
							.authenticated()
						)
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
//	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails vishal = User.withUsername("Vishal")
										.password("{noop}Vishal@123") // here {noop} represent no op password encoder
										.roles("USER")
										.build();
		UserDetails nikhil = User.withUsername("nikhil")
										.password("{noop}Nikhil@123")
										.roles("USER")
										.build();
		
		return new InMemoryUserDetailsManager(vishal, nikhil);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(14);
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
//		daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return daoAuthenticationProvider;
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
//	@Bean
//	public JWTAuthenticationFilter jwtAuthenticationFilter() {
//	    return new JWTAuthenticationFilter(); // or inject what you need
//	}
	
}
