package com.example.demo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	private String secretKey = null;
	
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts	
					.builder()
					.claims()
					.add(claims)
					.subject(user.getUsername())
					.issuer("Vishal-Service")
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))
					.and()
					.signWith(generateKey())
					.compact();
	}
	
	private SecretKey generateKey() {
		// TODO Auto-generated method stub
		byte[] decode = Decoders.BASE64.decode(getSecretKey());
		return Keys.hmacShaKeyFor(decode);
	}

	public String getSecretKey() {
		return secretKey = "u8x/Y7EaQx4ZFZc+3Z2rKk7uzXtQTuHT5n7XM3+6y6A=";
	}

	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(generateKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	
	

}
