package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.TimeSlotRequest;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.model.TimeSlot;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimeSlotMapper {
    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

    TimeSlot toEntity(TimeSlotRequest request);

    TimeSlotResponse toResponse(TimeSlot timeSlot);

    void update(TimeSlotRequest request,@MappingTarget TimeSlot timeSlot);
}
