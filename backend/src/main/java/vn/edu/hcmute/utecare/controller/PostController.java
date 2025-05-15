package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.PostRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PostResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "POST", description = "API quản lý bài viết trong hệ thống y tế")
@Slf4j(topic = "POST_CONTROLLER")
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin bài viết theo ID",
            description = "Truy xuất thông tin chi tiết của một bài viết dựa trên ID duy nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin bài viết thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết với ID được cung cấp")
    })
    public ResponseData<PostResponse> getPostById(
            @Parameter(description = "ID của bài viết cần truy xuất") @PathVariable("id") Long id) {
        log.info("Yêu cầu lấy thông tin bài viết với ID: {}", id);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin bài viết thành công")
                .data(postService.getPostById(id))
                .build();
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Tạo bài viết mới",
            description = "Tạo một bài viết mới với thông tin chi tiết và hình ảnh liên quan, được liên kết với một nhân viên."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo bài viết thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy nhân viên với ID được cung cấp")
    })
    public ResponseData<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        log.info("Yêu cầu tạo bài viết mới: {}", request);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo bài viết thành công")
                .data(postService.createPost(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Cập nhật bài viết",
            description = "Cập nhật thông tin của một bài viết hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật bài viết thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu yêu cầu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết hoặc nhân viên với ID được cung cấp")
    })
    public ResponseData<PostResponse> updatePost(
            @Parameter(description = "ID của bài viết cần cập nhật") @PathVariable("id") Long id,
            @Valid @RequestBody PostRequest request) {
        log.info("Yêu cầu cập nhật bài viết với ID: {}", id);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật bài viết thành công")
                .data(postService.updatePost(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(
            summary = "Xóa bài viết",
            description = "Xóa một bài viết hiện có dựa trên ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa bài viết thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bài viết với ID được cung cấp")
    })
    public ResponseData<Void> deletePost(
            @Parameter(description = "ID của bài viết cần xóa") @PathVariable("id") Long id) {
        log.info("Yêu cầu xóa bài viết với ID: {}", id);
        postService.deletePost(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Xóa bài viết thành công")
                .build();
    }

    @GetMapping("/list")
    @Operation(
            summary = "Lấy danh sách tất cả bài viết (không phân trang)",
            description = "Truy xuất toàn bộ danh sách bài viết trong hệ thống dưới dạng danh sách."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách bài viết thành công")
    })
    public ResponseData<List<PostResponse>> getAllPosts() {
        log.info("Yêu cầu lấy danh sách tất cả bài viết (không phân trang)");
        return ResponseData.<List<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách bài viết thành công")
                .data(postService.getAllPosts())
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Lấy danh sách bài viết (phân trang)",
            description = "Truy xuất danh sách bài viết với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách bài viết thành công")
    })
    public ResponseData<PageResponse<PostResponse>> getAllPostsPaginated(
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: doc, header)") @RequestParam(defaultValue = "doc") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "desc") String direction) {
        log.info("Yêu cầu lấy danh sách bài viết (phân trang): trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        return ResponseData.<PageResponse<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách bài viết thành công")
                .data(postService.getAllPosts(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Tìm kiếm bài viết",
            description = "Tìm kiếm bài viết theo từ khóa trong tiêu đề hoặc nội dung với phân trang và sắp xếp."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm bài viết thành công")
    })
    public ResponseData<PageResponse<PostResponse>> searchPosts(
            @Parameter(description = "Từ khóa tìm kiếm (tiêu đề hoặc nội dung)") @RequestParam String keyword,
            @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Số lượng bản ghi mỗi trang") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Trường để sắp xếp (ví dụ: doc, header)") @RequestParam(defaultValue = "doc") String sort,
            @Parameter(description = "Hướng sắp xếp: asc (tăng dần) hoặc desc (giảm dần)") @RequestParam(defaultValue = "desc") String direction) {
        log.info("Yêu cầu tìm kiếm bài viết: từ khóa={}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        return ResponseData.<PageResponse<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Tìm kiếm bài viết thành công")
                .data(postService.searchPosts(keyword, page, size, sort, direction))
                .build();
    }
}