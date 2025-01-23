package com.app.utils;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.app.service.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

	@Value("${SECRET_KEY}")
	private String jwtSecret;
	
	@Value("${EXP_TIMEOUT}")
	private String jwtExpiry;
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateJwtToken(Authentication authentication) {
		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
		return Jwts.builder()
				.subject(userPrincipal.getUsername())
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + Long.parseLong(jwtExpiry)))
				.signWith(getSignInKey())
				.compact();
	}

	public String getUsernameFromJwtToken(String token) {
		if (token.startsWith("Bearer ")) {
			return extractClaims(token.substring(7), Claims::getSubject);
		}
		return extractClaims(token, Claims::getSubject);
	}

	public boolean validateJwtToken(String token) {
		return Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(token) != null;
	}
	
	private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
		return claimsFunction
				.apply(
						Jwts.parser()
						.verifyWith(getSignInKey())
						.build()
						.parseSignedClaims(token)
						.getPayload()
				);
	}
}
