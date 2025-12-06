package telran.java57.farmmarket.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telran.java57.farmmarket.dto.TokenResponseDto;
import telran.java57.farmmarket.model.CookieProps;
import telran.java57.farmmarket.security.AuthService;
import telran.java57.farmmarket.security.JwtUtil;
import telran.java57.farmmarket.security.UserDetailsServiceImpl;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class RefreshController {

    private final AuthService authService;
    private final CookieProps cookieProps;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String token = extractTokenFromCookie(request);
        TokenResponseDto dto = authService.refresh(token);

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie
                .from("refreshToken", dto.getRefreshToken())
                .httpOnly(true)
                .path(cookieProps.getPath())
                .secure(cookieProps.isSecure())
                .sameSite(cookieProps.getSameSite())
                .maxAge(Duration.ofMillis(cookieProps.getMaxAgeMs()));

        if (cookieProps.getDomain() != null && !cookieProps.getDomain().isBlank()) {
            builder.domain(cookieProps.getDomain());
        }
        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dto.getAccessToken())
                .build();
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("Refresh token cookie not found or empty");
    }
}