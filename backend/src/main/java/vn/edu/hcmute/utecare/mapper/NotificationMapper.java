package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.NotificationRequest;
import vn.edu.hcmute.utecare.dto.response.NotificationResponse;
import vn.edu.hcmute.utecare.model.Notification;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    Notification toEntity(NotificationRequest request);

    NotificationResponse toResponse(Notification notification);
}
