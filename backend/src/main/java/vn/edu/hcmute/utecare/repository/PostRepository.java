package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.utecare.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
            "(:keyword IS NULL OR LOWER(p.header) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchPosts(String keyword, Pageable pageable);

    Page<Post> findByHeaderContainingIgnoreCaseOrContentContainingIgnoreCase(String header, String content, Pageable pageable);
}
