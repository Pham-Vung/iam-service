package vincent.pham.iamservice.exception;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import vincent.pham.iamservice.common.response.ErrorResponse;

@Component
@RequiredArgsConstructor
public class ErrorResponseFactory {

    private final MessageSource messageSource;

    public ErrorResponse fromErrorCode(ErrorCode errorCode, Locale locale) {
        return buildResponse(errorCode, null, locale);
    }

    public ErrorResponse fromBusinessException(BusinessException exception, Locale locale) {
        return buildResponse(
                exception.getErrorCode(),
                exception.getMessageParameter(),
                locale
        );
    }

    public ErrorResponse fromValidationErrors(Map<String, String> errors, Locale locale) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        return ErrorResponse.builder()
                .success(false)
                .errorCode(errorCode.name())
                .message(resolveMessage(errorCode, null, locale))
                .errors(errors)
                .timestamp(Instant.now())
                .build();
    }

    private ErrorResponse buildResponse(
            ErrorCode errorCode,
            Object messageParameter,
            Locale locale
    ) {
        return ErrorResponse.builder()
                .success(false)
                .errorCode(errorCode.name())
                .message(resolveMessage(errorCode, messageParameter, locale))
                .timestamp(Instant.now())
                .build();
    }

    private String resolveMessage(
            ErrorCode errorCode,
            Object messageParameter,
            Locale locale
    ) {
        Object[] arguments = messageParameter == null
                ? null
                : new Object[]{messageParameter};

        return messageSource.getMessage(
                errorCode.getMessageKey(),
                arguments,
                errorCode.name(),
                locale
        );
    }
}
