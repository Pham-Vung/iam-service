package vincent.pham.iamservice.common.response;

import java.util.List;
import java.util.function.Function;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public final class BasePaginationResponse<T> {

    private final List<T> items;
    private final Integer pageIndex;
    private final Integer pageSize;
    private final Long totalElements;
    private final Integer totalPages;

    public static <T> BasePaginationResponse<T> from(Page<T> page) {
        return BasePaginationResponse.<T>builder()
                .items(page.getContent())
                .pageIndex(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public static <S, T> BasePaginationResponse<T> from(
            Page<S> page,
            Function<S, T> mapper
    ) {
        List<T> items = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return BasePaginationResponse.<T>builder()
                .items(items)
                .pageIndex(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
