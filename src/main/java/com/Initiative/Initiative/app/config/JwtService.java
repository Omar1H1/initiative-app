package com.Initiative.Initiative.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGtleSBmb3IgSlNXIFNpZ25hdHVyZUFsZ29yaXRobS5IUzI1Ng==";
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String extractUsername(String token) {
        logger.info("Extracting username from token: {}", token);
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getSignInKey() {
        logger.info("Getting sign in key");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.info("Extracting claim from token: {}", token);
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating token for user: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        logger.info("Generating token with extra claims for user: {}", userDetails.getUsername());
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.info("Validating token: {}", token);
        try {
            final String username = extractUsername(token);
            logger.info("Extracted username: {}", username);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired: {}", token, e);
            return false;
        } catch (SignatureException e) {
            logger.error("Invalid token signature: {}", token, e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        logger.info("Checking if token is expired: {}", token);
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        logger.info("Extracting expiration date from token: {}", token);
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        logger.info("Extracting all claims from token: {}", token);
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired: {}", token, e);
            throw e;
        } catch (SignatureException e) {
            logger.error("Invalid token signature: {}", token, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error extracting claims from token: {}", token, e);
            return null;
        }
    }
}