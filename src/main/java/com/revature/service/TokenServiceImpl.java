package com.revature.service;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.model.AuthenticatedUser;
import com.revature.model.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenServiceImpl implements TokenService {

	private static final long TOKEN_EXPIRY = 1000 * 60 * 60 * 24; // 1 day
	private static final TokenService instance = new TokenServiceImpl();
	private final Logger logger = LogManager.getLogger(getClass());
	private final KeyPair keyPair;
	
	private TokenServiceImpl() {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			keyStore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("demo_jwt.jks"), "Password123!".toCharArray());
			Key key = keyStore.getKey("demo_jwt", "Password123!".toCharArray());
			if (key instanceof PrivateKey) {
				// Get the certificate
				Certificate cert = keyStore.getCertificate("demo_jwt");
				
				// Get Public Key
				PublicKey pubKey = cert.getPublicKey();
				this.keyPair = new KeyPair(pubKey, (PrivateKey) key);
			} else {
				throw new IOException("Failed to read KeyStore");
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
			logger.error("Failed to generate KeyPair: {}", e);
			throw new RuntimeException(e);
		}
	}
	
	public static final TokenService getInstance() {
		return instance;
	}
	
	@Override
	public String generateToken(UserDetails details) {
		if (details == null) 
			throw new IllegalArgumentException("User details must not be null");
		final Date now = new Date();
		final String jti = UUID.randomUUID().toString();
		return Jwts.builder().signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
				   .setSubject(details.getUsername())
				   .claim("email", details.getEmail())
				   .claim("roles", details.getRoles().stream().collect(Collectors.joining(",")))
				   .setIssuedAt(now)
				   .setExpiration(new Date(now.getTime() + TOKEN_EXPIRY))
				   .setId(jti)
				   .compact();
	}

	@Override
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			logger.error("JWT validation failed at {}. Exception was {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")), e.getClass().getName());
		}
		return false;
	}

	@Override
	public UserDetails getUserDetailsFromToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			Claims claims = Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token.replace("Bearer ", "")).getBody();
			return new AuthenticatedUser(claims.getSubject(), (String) claims.get("email"), Arrays.stream(claims.get("roles").toString().split(",")).collect(Collectors.toList()));
		}
		return null;
	}

	@Override
	public String getTokenId(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			Claims claims = Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token.replace("Bearer ", "")).getBody();
			return claims.getId();
		}
		return null;
	}

}
