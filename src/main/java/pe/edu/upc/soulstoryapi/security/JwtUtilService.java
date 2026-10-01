package pe.edu.upc.soulstoryapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {

    // [Base64] Se lee de application.properties (variable de entorno JWT_SECRET)
    @Value("${app.security.jwt.secret}")
    private String jwtSignatureKey;

    // Tiempo de validez del token en milisegundos (por defecto 3 horas)
    @Value("${app.security.jwt.expiration-ms:10800000}")
    private Long jwtTokenValidity;

    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSignatureKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(extractAllClaims(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UsuarioSecurity usuario) {
        String username = extractUsername(token);
        return (!isTokenExpired(token)) && (username.equals(usuario.getUsername()));
    }

    private String createToken(String subject, Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UsuarioSecurity usuarioSecurity) {
        Map<String, Object> claims = new HashMap<>();
        Object authorities = usuarioSecurity.getAuthorities().stream()
                .map(n -> String.valueOf(n.getAuthority()))
                .toList();
        claims.put("authorities", authorities);
        claims.put("user_id", usuarioSecurity.getUsuario().getIdUsuario());
        return createToken(usuarioSecurity.getUsername(), claims);
    }

}
