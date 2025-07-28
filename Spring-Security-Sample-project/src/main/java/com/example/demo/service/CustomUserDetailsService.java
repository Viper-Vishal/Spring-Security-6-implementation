package com.example.demo.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.CustomUserDetails;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		// TODO Auto-generated constructor stub
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username);
		
		if(Objects.isNull(user)) {
			System.out.println("User not available.");
			throw new UsernameNotFoundException(username);
		}
		
		return new CustomUserDetails(user);
	}

}
