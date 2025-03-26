package vn.edu.hcmute.utecare.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.exception.InvalidDataException;
import vn.edu.hcmute.utecare.service.JwtService;
import vn.edu.hcmute.utecare.util.TokenType;

import javax.crypto.SecretKey;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiryHour}")
    private Long expiryHour;

    @Value("${jwt.expiryDay}")
    private Long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.verificationKey}")
    private String verificationKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    @Override
    public String generateAccessToken(UserDetails user) {
        if (user == null) throw new IllegalArgumentException("UserDetails cannot be null");
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getRoles(user));;

        return generateToken(claims, user);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        if (user == null) throw new IllegalArgumentException("UserDetails cannot be null");
        return generateRefreshToken(new HashMap<>(), user);
    }

    @Override
    public String generateVerificationToken(String phone) {
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Phone cannot be null or empty");
        return generateVerificationToken(new HashMap<>(), phone);
    }

    @Override
    public String extractUsername(String token, TokenType type){
        try {
            return extractClaim(token, type, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            throw new InvalidDataException("Token has expired");
        } catch (SignatureException e) {
            throw new InvalidDataException("Invalid token signature");
        } catch (Exception e) {
            throw new InvalidDataException("Malformed token");
        }
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails){
        log.info("---------- isValid ----------");
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, type));
    }

    @Override
    public boolean isTokenExpired(String token, TokenType type){
        try {
            return extractExpiration(token, type).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            throw new InvalidDataException("Invalid token");
        }
    }

    private String generateToken(Map<String, Object> claims, UserDetails user){
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))
                .signWith(getKey(TokenType.ACCESS_TOKEN), Jwts.SIG.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, UserDetails user){
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryDay))
                .signWith(getKey(TokenType.REFRESH_TOKEN), Jwts.SIG.HS256)
                .compact();
    }

    private String generateVerificationToken(Map<String, Object> claims, String phone){
        return Jwts.builder()
                .claims(claims)
                .subject(phone)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 1800 ))
                .signWith(getKey(TokenType.VERIFICATION_TOKEN), Jwts.SIG.HS256)
                .compact();
    }


    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type){
        return Jwts.parser().verifyWith(getKey(type)).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey(TokenType type){
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            case VERIFICATION_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(verificationKey));
            }
            case RESET_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(resetKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }



    private Date extractExpiration(String token, TokenType type){
        return extractClaim(token, type, Claims::getExpiration);
    }

    private String getRoles(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
