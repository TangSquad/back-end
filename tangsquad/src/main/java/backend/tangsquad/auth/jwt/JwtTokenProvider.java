package backend.tangsquad.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expiration}") Long expiration,
                            @Value("${jwt.refresh-token-expiration}") Long refreshExpiration) {

        this.accessTokenExpiration = expiration;
        this.refreshTokenExpiration = refreshExpiration;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String nickname, String role) {
        return generateToken(nickname, role, accessTokenExpiration);
    }

    public String generateRefreshToken(String nickname) {
        return generateToken(nickname, null, refreshTokenExpiration);
    }

    public String generateToken(String nickname, String role, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", nickname);  // "sub" is the standard claim name for subject
        if (role != null) {
            claims.put("role", role);
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            System.out.println("Key: " + key.toString());

            Jwts.parser().verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getNickname(String token) {
        return getClaims(token).getSubject();
    }
}
