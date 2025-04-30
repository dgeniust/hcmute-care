package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.ScheduleSlotInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSlotResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {TimeSlotMapper.class})
public interface ScheduleSlotMapper {
    ScheduleSlotMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ScheduleSlotMapper.class);

    @Mapping(target = "timeSlot", source = "timeSlot")
    ScheduleSlotResponse toResponse(vn.edu.hcmute.utecare.model.ScheduleSlot scheduleSlot);

    @Mapping(target = "timeSlot", source = "timeSlot")
    @Mapping(target = "doctorName", source = "schedule.doctor.fullName")
    @Mapping(target = "roomName", source = "schedule.roomDetail.name")
    ScheduleSlotInfoResponse toInfoResponse(vn.edu.hcmute.utecare.model.ScheduleSlot scheduleSlot);
}
