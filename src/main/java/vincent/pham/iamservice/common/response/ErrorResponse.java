package vincent.pham.iamservice.common.response;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class ErrorResponse {

    private final boolean success;
    private final String errorCode;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, String> errors;

    private final Instant timestamp;
}
