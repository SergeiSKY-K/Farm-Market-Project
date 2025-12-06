package telran.java57.farmmarket.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class ApiError {
    Instant timestamp;      // когда случилось
    int status;             // HTTP статус (число)
    String error;           // HTTP reason phrase, например "Bad Request"
    String code;            // наш машинный код, например "VALIDATION_ERROR"
    String message;         // безопасное для клиента сообщение
    String path;            // URI запроса
    String traceId;         // корреляция (например UUID)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<FieldError> details;  // детальные ошибки валидации

    @Value @Builder
    public static class FieldError {
        String field;
        String message;
        Object rejectedValue;
    }
}