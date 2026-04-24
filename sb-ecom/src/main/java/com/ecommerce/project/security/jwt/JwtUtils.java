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
/** Clase auxiliar que se encarga de todo lo relacionado con el token JWT.
 * Esta clase es el cerebro del JWT:
 * 📦 Crea el token
 * 🍪 Lo guarda en cookie
 * 🔍 Lo lee
 * ✅ Lo valida
 * 👤 Extrae usuario
 **/
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    //Tiempo de expiración del Token
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationsMs;
    //Clave secreta para firmar el Token
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    //Nombre de la Cookie
    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;


    // Obtener el JWT desde la cookie
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    // Obtener el token de la cabezera (Para swagger)
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return null;
    }

    //Generar cookie con JWT
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {

        String jwt = generateTokenFromUsername(userPrincipal.getUsername());

        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)//duracion x 1 dia
                .httpOnly(false)
                .build();

        return cookie;
    }

    //Cookie limpia (logout)
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
        return cookie;
    }

    //Generar el JWT a partir del Username
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username) //username
                .issuedAt(new Date()) //fecha de creacion
                .expiration(new Date((new Date().getTime() + jwtExpirationsMs))) //cuando expira
                .signWith(key()) //firma con clave secreta
                .compact();
    }


    //Obtener username desde el token
    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }


    //Metodo interno - Generar la clave secreta
    private Key key() {

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    //Validar el token
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage()); //Token no válido
        }catch(ExpiredJwtException ex) {
            logger.error("JWT token is expired: {}", ex.getMessage()); //Token expirado
        }catch(UnsupportedJwtException ex) {
            logger.error("JWT token is unsupported: {}", ex.getMessage()); //Token no soportado
        }catch(IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage()); //Token vacío
        }
        return false;
    }

}
