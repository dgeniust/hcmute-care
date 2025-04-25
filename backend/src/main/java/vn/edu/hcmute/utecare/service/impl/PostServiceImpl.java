package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.dto.request.PostRequest;
import vn.edu.hcmute.utecare.dto.request.PostImageRequest;
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
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StaffRepository staffRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse createPost(PostRequest request) {
        Post post = postMapper.toEntity(request);
        // Set Staff
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
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

        Post saved = postRepository.save(post);
        return postMapper.toResponse(saved);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return postMapper.toResponse(post);
    }

    @Override
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postMapper.updateEntity(request, post);

        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        post.setStaff(staff);

        Set<PostImage> postImages = request.getPostImages().stream()
                .map(imgReq -> {
                    PostImage image = new PostImage();
                    image.setImageUrl(imgReq.getImageUrl());
                    image.setPost(post);
                    return image;
                }).collect(Collectors.toSet());
        post.setPostImages(postImages);

        Post updated = postRepository.save(post);
        return postMapper.toResponse(updated);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(id);
    }

    @Override
    public PageResponse<PostResponse> getAllPosts(int page, int size, String sort, String direction) {
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostResponse> content = postPage.getContent().stream()
                .map(postMapper::toResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .content(content)
                .build();
    }

    @Override
    public PageResponse<PostResponse> searchPosts(String keyword, int page, int size, String sort, String direction) {
        Sort.Direction sortDir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        Page<Post> postPage = postRepository.findByHeaderContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);

        List<PostResponse> content = postPage.getContent().stream()
                .map(postMapper::toResponse)
                .toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(postPage.getNumber())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .content(content)
                .build();
    }

}
