package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.utecare.dto.request.PostRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PostResponse;
import vn.edu.hcmute.utecare.exception.ResourceNotFoundException;
import vn.edu.hcmute.utecare.mapper.PostMapper;
import vn.edu.hcmute.utecare.model.Post;
import vn.edu.hcmute.utecare.model.PostImage;
import vn.edu.hcmute.utecare.model.Staff;
import vn.edu.hcmute.utecare.repository.PostRepository;
import vn.edu.hcmute.utecare.repository.StaffRepository;
import vn.edu.hcmute.utecare.service.PostService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StaffRepository staffRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request) {
        log.info("Tạo bài viết mới với thông tin: {}", request);
        Post post = postMapper.toEntity(request);

        // Set Staff
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + request.getStaffId()));
        post.setStaff(staff);

        // Map PostImages
        Set<PostImage> postImages = request.getPostImages().stream()
                .map(imageRequest -> {
                    PostImage image = new PostImage();
                    image.setImageUrl(imageRequest.getImageUrl());
                    image.setPost(post);
                    return image;
                }).collect(Collectors.toSet());
        post.setPostImages(postImages);

        Post savedPost = postRepository.save(post);
        log.info("Tạo bài viết thành công với ID: {}", savedPost.getId());
        return postMapper.toResponse(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        log.info("Truy xuất danh sách tất cả bài viết (không phân trang)");
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        log.info("Truy xuất bài viết với ID: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với ID: " + id));
        log.info("Truy xuất bài viết thành công với ID: {}", id);
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        log.info("Cập nhật bài viết với ID: {} và thông tin: {}", id, request);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với ID: " + id));

        postMapper.updateEntity(request, post);

        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + request.getStaffId()));
        post.setStaff(staff);

        Set<PostImage> postImages = request.getPostImages().stream()
                .map(imgReq -> {
                    PostImage image = new PostImage();
                    image.setImageUrl(imgReq.getImageUrl());
                    image.setPost(post);
                    return image;
                }).collect(Collectors.toSet());
        post.setPostImages(postImages);

        Post updatedPost = postRepository.save(post);
        log.info("Cập nhật bài viết thành công với ID: {}", id);
        return postMapper.toResponse(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        log.info("Xóa bài viết với ID: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết với ID: " + id));
        postRepository.delete(post);
        log.info("Xóa bài viết thành công với ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getAllPosts(int page, int size, String sort, String direction) {
        log.info("Truy xuất danh sách bài viết (phân trang): trang={}, kích thước={}, sắp xếp={}, hướng={}", page, size, sort, direction);
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        Page<Post> postPage = postRepository.findAll(pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .content(postPage.getContent().stream()
                        .map(postMapper::toResponse)
                        .toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> searchPosts(String keyword, int page, int size, String sort, String direction) {
        log.info("Tìm kiếm bài viết: từ khóa={}, trang={}, kích thước={}, sắp xếp={}, hướng={}", keyword, page, size, sort, direction);
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        Page<Post> postPage = postRepository.findByHeaderContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .content(postPage.getContent().stream()
                        .map(postMapper::toResponse)
                        .toList())
                .build();
    }
}