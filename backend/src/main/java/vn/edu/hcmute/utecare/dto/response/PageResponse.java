package vn.edu.hcmute.utecare.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;

    @Builder.Default
    private List<T> content = Collections.emptyList();
}
