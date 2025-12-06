package telran.java57.farmmarket.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.RefreshTokenRepository;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.dto.LoginDto;
import telran.java57.farmmarket.dto.TokenResponseDto;
import telran.java57.farmmarket.dto.exceptions.UserNotFoundException;
import telran.java57.farmmarket.model.CookieProps;
import telran.java57.farmmarket.model.RefreshTokenEntity;
import telran.java57.farmmarket.dto.UserDto;
import telran.java57.farmmarket.model.UserAccount;

import java.time.Duration;

import static telran.java57.farmmarket.utils.TokenHashUtil.hash;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CookieProps cookieProps;

    private ResponseCookie buildRefreshCookie(String value) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie
                .from("refreshToken", value)
                .httpOnly(true)
                .path(cookieProps.getPath())
                .secure(cookieProps.isSecure())
                .sameSite(cookieProps.getSameSite())
                // можно использовать либо TTL из JwtUtil, либо из props — выбери одно:
                .maxAge(Duration.ofMillis(jwtUtil.getRefreshExpiration()));
        // .maxAge(Duration.ofMillis(cookieProps.getMaxAgeMs()));
        if (cookieProps.getDomain() != null && !cookieProps.getDomain().isBlank()) {
            b.domain(cookieProps.getDomain());
        }
        return b.build();
    }

    private ResponseCookie expireRefreshCookie() {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .path(cookieProps.getPath())
                .secure(cookieProps.isSecure())
                .sameSite(cookieProps.getSameSite())
                .maxAge(0);
        if (cookieProps.getDomain() != null && !cookieProps.getDomain().isBlank()) {
            b.domain(cookieProps.getDomain());
        }
        return b.build();
    }

    public ResponseEntity<UserDto> login(LoginDto loginDto, HttpServletResponse response) {
        UserAccount userAccount = userRepository.findById(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getUsername()));

        if (!passwordEncoder.matches(loginDto.getPassword(), userAccount.getPassword())) {
            throw new BadCredentialsException("Incorrect login or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userAccount.getLogin());

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        String hashedRefresh = hash(refreshToken);
        refreshTokenRepository.deleteById(userAccount.getLogin());
        refreshTokenRepository.save(new RefreshTokenEntity(userAccount.getLogin(), hashedRefresh));

        // Кладём refresh-cookie с флагами из конфига
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(refreshToken).toString());

        UserDto dto = modelMapper.map(userAccount, UserDto.class);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(dto);
    }

    public ResponseEntity<Void> logout(String login, HttpServletResponse response) {
        refreshTokenRepository.deleteById(login);
        response.addHeader(HttpHeaders.SET_COOKIE, expireRefreshCookie().toString());
        return ResponseEntity.noContent().build();
    }

    public TokenResponseDto refresh(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);

        RefreshTokenEntity stored = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        String hashedRefresh = hash(refreshToken);
        if (!hashedRefresh.equals(stored.getHashedRefreshToken())
                || !jwtUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        String newHashed = hash(newRefreshToken);
        refreshTokenRepository.save(new RefreshTokenEntity(username, newHashed));

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
}