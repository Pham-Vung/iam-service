package vincent.pham.iamservice.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseSearchRequest {

    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    private String keyword;

    @Min(value = 1, message = "pageIndex phải lớn hơn hoặc bằng 1")
    private Integer pageIndex = DEFAULT_PAGE_INDEX;

    @Min(value = 1, message = "pageSize phải lớn hơn hoặc bằng 1")
    @Max(value = MAX_PAGE_SIZE, message = "pageSize phải nhỏ hơn hoặc bằng 100")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public int getPageNumber() {
        return Math.max(
                pageIndex == null ? DEFAULT_PAGE_INDEX : pageIndex,
                DEFAULT_PAGE_INDEX
        ) - 1;
    }

    public int getLimit() {
        int requestedPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        return Math.min(Math.max(requestedPageSize, 1), MAX_PAGE_SIZE);
    }

    public String getKeyword() {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim();
    }
}
