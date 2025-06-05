package com.example.webstorydemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtils {
	
	@Value("${jwt.secretKeyString}")
	private String secretKeyString;
	
	public  String generateAccessTokens(String username, List<String> roles, long expiredMinute) {
		Date expirationDate = new Date(System.currentTimeMillis() + expiredMinute * 60 * 1000);
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		return Jwts.builder()
				.subject(username)
				.expiration(expirationDate)
				.claim("roles", roles)
				.signWith(key)
				.compact();
	}

	public String generateRefreshTokens(String username, List<String> roles, long expiredMinute) {
		Date expirationDate = new Date(System.currentTimeMillis() + expiredMinute * 60 * 1000);
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		return Jwts.builder()
				.subject(username)
				.expiration(expirationDate)
				.claim("roles", roles)
				.signWith(key)
				.compact();
	}

	
	public boolean verifyToken(String token) {
		boolean isVerify = false;
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			isVerify = true;
			System.out.println("verify: true");
		} catch (Exception e) {
			System.out.println("verify: false! Error: " + e.getMessage());
		}
		return isVerify;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> extractClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		@SuppressWarnings("deprecation")
		Claims claims = Jwts.parser()
				.verifyWith(key).build().parseSignedClaims(token).getBody();
		return claims.get("roles", List.class);
    }
	
	public String extractUsername(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		
		@SuppressWarnings("deprecation")
		String username = Jwts.parser()
							.verifyWith(key)
							.build()
							.parseSignedClaims(token).getBody().getSubject();
		return username;
	}
}
