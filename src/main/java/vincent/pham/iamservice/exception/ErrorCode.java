package vincent.pham.iamservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "error.validation-failed"),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "error.invalid-request-body"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "error.invalid-argument"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "error.method-not-allowed"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal"),

    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "error.unauthenticated"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "error.forbidden"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "error.invalid-credentials");

    private final HttpStatus httpStatus;
    private final String messageKey;
}
