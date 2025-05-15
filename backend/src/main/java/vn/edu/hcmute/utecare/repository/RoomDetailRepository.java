package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.RoomDetail;

@Repository
public interface RoomDetailRepository extends JpaRepository<RoomDetail, Integer> {

    @Query("SELECT r FROM RoomDetail r WHERE " +
            "(:keyword IS NULL OR LOWER(r.name) LIKE LOWER('%' || :keyword || '%'))"
    )
    Page<RoomDetail> searchRoomDetails(
            @Param("keyword") String keyword, Pageable pageable);
}
