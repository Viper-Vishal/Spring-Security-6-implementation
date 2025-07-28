package com.example.demo.config;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Lazy
public class JWTAuthenticationFilter extends OncePerRequestFilter {

//    private final SecurityFilterChain securityFilterChain;
	
	private final JWTService jwtService;
	
	private final UserDetailsService userDetailsService;
	
	public JWTAuthenticationFilter(JWTService jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
//		this.securityFilterChain = securityFilterChain;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = authHeader.substring(7);
		final String username = jwtService.extractUsername(jwt);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(username != null && authentication == null) {
			// Authenticate 
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if(jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
								= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource()
									.buildDetails(request)
						);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			
		}
		filterChain.doFilter(request, response);
	}

}
