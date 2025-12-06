package telran.java57.farmmarket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityExceptionHandlers {

    private final ObjectMapper mapper; // берём из контекста Boot

    private void writeJson(HttpServletResponse res, int status, ApiError body) throws IOException {
        if (res.isCommitted()) return; // на всякий случай
        res.setStatus(status);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        mapper.writeValue(res.getOutputStream(), body);
    }

    public AuthenticationEntryPoint authEntryPoint() {
        return (req, res, ex) -> {
            // Стандартизированная подсказка клиенту
            res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            var err = ApiError.builder()
                    .timestamp(Instant.now())
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .error("Unauthorized")
                    .code("UNAUTHORIZED")
                    .message("Authentication required or token invalid")
                    .path(req.getRequestURI())
                    .traceId(UUID.randomUUID().toString())
                    .build();
            writeJson(res, HttpServletResponse.SC_UNAUTHORIZED, err);
        };
    }

    public AccessDeniedHandler accessDeniedHandler() {
        return (req, res, ex) -> {
            var err = ApiError.builder()
                    .timestamp(Instant.now())
                    .status(HttpServletResponse.SC_FORBIDDEN)
                    .error("Forbidden")
                    .code("FORBIDDEN")
                    .message("You don't have permission to access this resource")
                    .path(req.getRequestURI())
                    .traceId(UUID.randomUUID().toString())
                    .build();
            writeJson(res, HttpServletResponse.SC_FORBIDDEN, err);
        };
    }
}

