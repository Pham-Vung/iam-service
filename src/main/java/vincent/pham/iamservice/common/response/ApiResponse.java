package vincent.pham.iamservice.common.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class ApiResponse<T> {

    public static final String DEFAULT_MESSAGE = "Ok";

    private final boolean success;
    private final String message;
    private final T data;
    private final Instant timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(DEFAULT_MESSAGE)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message == null || message.isBlank() ? DEFAULT_MESSAGE : message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

}
