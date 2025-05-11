package vn.edu.hcmute.utecare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.utecare.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser_Phone(String phone, Pageable pageable);

    Page<Notification> findByUser_IdOrUser_IdNull(Long userId, Pageable pageable);

    Page<Notification> findByUser_PhoneOrUserIsNull(String phone, Pageable pageable);
}
