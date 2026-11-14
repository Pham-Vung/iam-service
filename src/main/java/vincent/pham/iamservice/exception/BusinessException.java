package vincent.pham.iamservice.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object messageParameter;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BusinessException(ErrorCode errorCode, Object messageParameter) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.messageParameter = messageParameter;
    }
}
