package telran.java57.farmmarket.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import telran.java57.farmmarket.dto.exceptions.*;

import java.time.Instant;
import java.util.UUID;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError.ApiErrorBuilder base(HttpServletRequest req, HttpStatus status, String code) {
        return ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(code)
                .path(req.getRequestURI())
                .traceId(UUID.randomUUID().toString());
    }

    @ExceptionHandler({ ProductNotFoundException.class, OrderNotFoundException.class, UserNotFoundException.class })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.NOT_FOUND;
        String code =
                (ex instanceof ProductNotFoundException) ? "PRODUCT_NOT_FOUND" :
                        (ex instanceof OrderNotFoundException)   ? "ORDER_NOT_FOUND"   :
                                "USER_NOT_FOUND";
        return ResponseEntity.status(st).body(
                base(req, st, code).message(ex.getMessage()).build()
        );
    }

    @ExceptionHandler(NotEnoughQuantityOfProductException.class)
    public ResponseEntity<ApiError> handleQty(NotEnoughQuantityOfProductException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.CONFLICT; // или 422
        return ResponseEntity.status(st).body(
                base(req, st, "NOT_ENOUGH_QUANTITY").message(ex.getMessage()).build()
        );
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> handleUserExists(UserExistsException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.CONFLICT;
        return ResponseEntity.status(st).body(
                base(req, st, "USER_EXISTS").message("User already exists").build()
        );
    }

    // общие вещи:
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(st).body(
                base(req, st, "BAD_REQUEST").message(ex.getMessage()).build()
        );
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(Exception ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(st).body(
                base(req, st, "FORBIDDEN").message("You don't have permission to access this resource").build()
        );
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(Exception ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(st).body(
                base(req, st, "UNAUTHORIZED").message("Authentication required or token invalid").build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        var st = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = base(req, st, "INTERNAL_ERROR")
                .message("Unexpected server error")
                .build();

        // логируем стек, но клиенту не светим детали
        log.error("Unhandled exception at {}: {}", req.getRequestURI(), ex.toString(), ex);
        return ResponseEntity.status(st).body(body);
    }
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        var st = HttpStatus.BAD_REQUEST;
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ApiError.FieldError.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .rejectedValue(fe.getRejectedValue())
                        .build())
                .toList();

        var body = base(req, st, "VALIDATION_ERROR")
                .message("Validation failed")
                .details(details)
                .build();

        return ResponseEntity.status(st).body(body);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest req
    ) {
        var st = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(st).body(
                base(req, st, "MALFORMED_JSON")
                        .message("Malformed JSON or wrong payload")
                        .build()
        );
    }
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleUploadTooLarge(
            org.springframework.web.multipart.MaxUploadSizeExceededException ex,
            HttpServletRequest req
    ) {
        var st = HttpStatus.PAYLOAD_TOO_LARGE; // 413
        return ResponseEntity.status(st).body(
                base(req, st, "FILE_TOO_LARGE")
                        .message("Uploaded file is too large")
                        .build()
        );
    }
}