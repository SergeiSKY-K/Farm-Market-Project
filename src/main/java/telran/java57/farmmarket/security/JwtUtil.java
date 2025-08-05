package telran.java57.farmmarket.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtUtil {

    private String secret;
    private long accessExpiration;
    private long refreshExpiration;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        try {
            Dotenv dotenv = Dotenv.load();
            secret = dotenv.get("JWT_SECRET", System.getenv("JWT_SECRET"));
            accessExpiration = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION", System.getenv("JWT_ACCESS_EXPIRATION")));
            refreshExpiration = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION", System.getenv("JWT_REFRESH_EXPIRATION")));
        } catch (Exception e) {
            secret = System.getenv("JWT_SECRET");
            accessExpiration = Long.parseLong(System.getenv("JWT_ACCESS_EXPIRATION"));
            refreshExpiration = Long.parseLong(System.getenv("JWT_REFRESH_EXPIRATION"));
        }

        algorithm = Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
                .sign(algorithm);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), refreshExpiration);
    }

    private String generateToken(String username, long duration) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return getDecodedJWT(token).getSubject();
    }


    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            DecodedJWT jwt = getDecodedJWT(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getDecodedJWT(token).getExpiresAt().before(new Date());
    }

    public DecodedJWT getDecodedJWT(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}