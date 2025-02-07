package uz.auth.auth.security.tokenGenerator;

import uz.auth.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String keyForToken = "WEftg45534ssgfsg445dfg$%^e_transfer_app_$@Jk+IEK397WEftg45534ssgfsg445dfg$%^e_transfer_app_$@Jk+IEK397";  // Секретный ключ для генерации токена
    private static final long expireTimeRefreshToken = 1000 * 60 * 60 * 24 * 14; // Время жизни токена обновления (14 дней)

    public static String generatorToken(String username, Role role) {

        // Время жизни токена (7 дней)
//        long expireTime = 1000 * 60 * 60 * 24 * 7;
        Date expireDate = new Date(System.currentTimeMillis() + expireTimeRefreshToken/2);

        try {
            SecretKey key = Keys.hmacShaKeyFor(keyForToken.getBytes());

            return Jwts
                    .builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(expireDate)
                    .claim("roles", role.getName())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating token: " + e.getMessage());
        }
    }

    public static String generateRefreshToken(String username, Role role) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTimeRefreshToken);

        String keyForTokenRefresh = "FDSgfghy674^4sdassdaAasdf498_transfer_app_$@Jk+IEK397FDSgfghy674^4sdassdaAasdf498_transfer_app_$@Jk+IEK397";
        SecretKey key = Keys.hmacShaKeyFor(keyForTokenRefresh.getBytes());
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", role.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            boolean tokenExpired = isTokenExpired(token);
            if (tokenExpired) {
                return null;
            }

            SecretKey key = Keys.hmacShaKeyFor(keyForToken.getBytes());

            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(keyForToken.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
}
