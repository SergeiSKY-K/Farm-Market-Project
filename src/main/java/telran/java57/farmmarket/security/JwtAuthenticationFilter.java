package telran.java57.farmmarket.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String path = request.getServletPath();
        final String method = request.getMethod();

        // 1) Разрешаем auth-ручки и preflight без попыток парсить токен
        if ("OPTIONS".equalsIgnoreCase(method)
                || path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Достаём заголовок
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // нет токена — просто идём дальше как аноним
        }

        final String token = authHeader.substring(7).trim();
        if (token.isEmpty() || "null".equalsIgnoreCase(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Пытаемся аккуратно распарсить и провалидировать
        try {
            // verify() валидирует подпись и стандартные поля (мы позже проверим exp)
            var decoded = jwtUtil.getDecodedJWT(token);

            // subject обязателен
            String username = decoded.getSubject();
            if (username == null || username.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // если уже аутентифицирован — не переустанавливаем
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // exp
            var exp = decoded.getExpiresAt();
            if (exp == null || exp.before(new Date())) {
                filterChain.doFilter(request, response);
                return;
            }

            // роли (claim может отсутствовать/быть пустым)
            List<String> roles = Optional.ofNullable(decoded.getClaim("roles"))
                    .map(c -> c.asList(String.class))
                    .orElse(Collections.emptyList());

            Collection<GrantedAuthority> authorities = roles.stream()
                    .filter(Objects::nonNull)
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // ЛЮБАЯ ошибка валидации/парсинга → просто пропускаем дальше как аноним
            // Можно добавить лог на debug:
            // log.debug("JWT rejected: {}", e.toString());
        }

        // 4) Всегда продолжаем цепочку
        filterChain.doFilter(request, response);
    }
}

//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        if (path.equals("/auth/login") || path.equals("/auth/refresh"))  {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String username = jwtUtil.extractUsername(token);
//
//            if (username != null &&
//                    SecurityContextHolder.getContext().getAuthentication() == null &&
//                    jwtUtil.validateToken(token, username)) {
//
//                List<String> roles = jwtUtil.getDecodedJWT(token)
//                        .getClaim("roles")
//                        .asList(String.class);
//
//                Collection<GrantedAuthority> authorities = roles.stream()
//                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                        .collect(Collectors.toList());
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(username, null, authorities);
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
