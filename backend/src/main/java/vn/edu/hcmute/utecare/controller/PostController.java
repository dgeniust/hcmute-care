package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Post", description = "Post API")
@Slf4j(topic = "POST_CONTROLLER")
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve a post by its ID")
    public ResponseData<PostResponse> getPostById(@PathVariable("id") Long id) {
        log.info("Get post request for id: {}", id);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Post retrieved successfully")
                .data(postService.getPostById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new post", description = "Create a new post with associated staff and images")
    public ResponseData<PostResponse> createPost(@RequestBody @Valid PostRequest request) {
        log.info("Create post request: {}", request);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Post created successfully")
                .data(postService.createPost(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post", description = "Update an existing post by its ID")
    public ResponseData<PostResponse> updatePost(
            @PathVariable("id") Long id,
            @RequestBody @Valid PostRequest request) {
        log.info("Update post request for id: {}", id);
        return ResponseData.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Post updated successfully")
                .data(postService.updatePost(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", description = "Delete a post by its ID")
    public ResponseData<Void> deletePost(@PathVariable("id") Long id) {
        log.info("Delete post request for id: {}", id);
        postService.deletePost(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Post deleted successfully")
                .build();
    }

    @GetMapping("/list")
    @Operation(summary = "Get all posts (non-paginated)", description = "Retrieve all posts as a list")
    public ResponseData<List<PostResponse>> getAllPosts() {
        log.info("Get all posts (non-paginated)");
        return ResponseData.<List<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("All posts retrieved successfully")
                .data(postService.getAllPosts())
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all posts (paginated)", description = "Retrieve a paginated list of posts")
    public ResponseData<PageResponse<PostResponse>> getAllPostsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "doc") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        log.info("Get all posts paginated: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Posts retrieved successfully")
                .data(postService.getAllPosts(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Search posts by keyword in header or content")
    public ResponseData<PageResponse<PostResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "doc") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        log.info("Search posts with keyword: {}", keyword);
        return ResponseData.<PageResponse<PostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Post search completed successfully")
                .data(postService.searchPosts(keyword, page, size, sort, direction))
                .build();
    }
}
