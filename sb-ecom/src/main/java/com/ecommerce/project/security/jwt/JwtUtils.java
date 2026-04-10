package com.ecommerce.project.security.jwt;

import com.ecommerce.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
//CLASE AUXILIAR QUE ME PERMITE TRABAJAR CON EL TOKEN.
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationsMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;


    //Obtengo la Cookie
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    //Generar JWT Cookie
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {

        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)//duracion x 1 dia
                .httpOnly(false)
                .build();

        return cookie;
    }

    //Generar Cookie Limpia para cerrar sesión
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
        return cookie;
    }

    //Obtengo el token a partir del Username
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationsMs)))
                .signWith(key())
                .compact();
    }


    //Obtengo el Username a partir del Token
    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }


    //Metodo interno - Genero una clave de firma
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    //Valido el JWT Token
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        }catch(ExpiredJwtException ex) {
            logger.error("JWT token is expired: {}", ex.getMessage());
        }catch(UnsupportedJwtException ex) {
            logger.error("JWT token is unsupported: {}", ex.getMessage());
        }catch(IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

}
