package vincent.pham.iamservice.exception;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vincent.pham.iamservice.common.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorResponseFactory errorResponseFactory;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException exception,
            Locale locale
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        log.warn("Business exception: code={}", errorCode);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromBusinessException(exception, locale));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception,
            Locale locale
    ) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;

        log.warn("Access denied: {}", exception.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromErrorCode(errorCode, locale));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            Locale locale
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.putIfAbsent(
                        toSnakeCase(fieldError.getField()),
                        fieldError.getDefaultMessage()
                ));

        log.warn("Validation failed: {}", errors);

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromValidationErrors(errors, locale));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBody(
            HttpMessageNotReadableException exception,
            Locale locale
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

        log.warn("Request body is invalid", exception);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromErrorCode(errorCode, locale));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            Locale locale
    ) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        log.warn("Method not allowed: {}", exception.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromErrorCode(errorCode, locale));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            Locale locale
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_ARGUMENT;

        log.warn("Invalid argument", exception);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromErrorCode(errorCode, locale));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            Locale locale
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

        log.error("Unexpected exception", exception);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponseFactory.fromErrorCode(errorCode, locale));
    }

    private String toSnakeCase(String fieldName) {
        return fieldName
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .toLowerCase(Locale.ROOT);
    }
}
