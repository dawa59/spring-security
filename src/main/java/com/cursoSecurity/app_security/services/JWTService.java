package com.cursoSecurity.app_security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  public static final String JWT_SECRET = "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4";

  /**
   * Método que se encarga de obtener los Claims del token
   *
   * @param token
   * @return
   */
  private Claims getAllClaimsFromToken(String token) {
    final SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts
            .parserBuilder()//parseamos para analizar el JWT.
            .setSigningKey(key)//le ponemos una firma en el token. Garantiza la seguridad y la integridad del token.
            .build() //construye el parseo.
            .parseClaimsJws(token)// analiza el token y verifica su firma.
            .getBody();//obtiene el JWT que contiene los Claims.
  }

  /**
   * Método Auxiliar que recibe los claims
   *
   * @param token
   * @param claimsResolver para extraer el claim deseado.
   * @param <T>
   * @return
   */
  public <T> T getClaimFormToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Método Auxiliar para indicar el tiempo de caducidad del token
   *
   * @param token
   * @return
   */
  private Date getExpirationToken(String token) {
    return getClaimFormToken(token, Claims::getExpiration);
  }

  /**
   * Método Auxiliar que verifica la caducidad del token
   *
   * @param token
   * @return
   */
  private Boolean isTokenExpired(String token) {

    final Date expirationDate = getExpirationToken(token);
    return expirationDate.before(new Date());
  }

  /**
   * Método Auxiliar para extraer el nombre de usuario del token
   *
   * @param token
   * @return
   */

  public String getUserNameFromToken(String token) {
    return getClaimFormToken(token, Claims::getSubject);
  }

  /**
   * Método Auxiliar que se valida el token del usuario y el tiempo del token que no esté expirado
   *
   * @param token
   * @param userDetails
   * @return
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String userNameFromUserDetails = userDetails.getUsername();
    final String userNameFromJWT = getUserNameFromToken(token);

    return (userNameFromUserDetails.equals(userNameFromJWT) && !isTokenExpired(token));
  }

  /**
   * Método para obtener el TOKEN
   *
   * @param claims
   * @param subject
   * @return
   */
  private String getToken(Map<String, Object> claims, String subject) {

    final SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(key)
            .compact();
  }

  /**
   * Método Auxiliar para generar el token
   *
   * @param userDetails
   * @return
   */
  public String generationToken(UserDetails userDetails) {
    final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
    return getToken(claims, userDetails.getUsername());
  }
}
