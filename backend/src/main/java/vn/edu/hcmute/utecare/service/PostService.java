package vn.edu.hcmute.utecare.service;

import vn.edu.hcmute.utecare.dto.request.PostRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request);

    PostResponse getPostById(Long id);

    PostResponse updatePost(Long id, PostRequest request);

    void deletePost(Long id);

    List<PostResponse> getAllPosts();

    PageResponse<PostResponse> getAllPosts(int page, int size, String sort, String direction);

    PageResponse<PostResponse> searchPosts(String keyword, int page, int size, String sort, String direction);
}
