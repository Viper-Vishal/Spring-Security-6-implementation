package com.example.demo.controller;

import java.net.http.HttpRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController {
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to Neosoft!!";
	}
	
	@GetMapping("/csrf")
	public CsrfToken getToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
}
