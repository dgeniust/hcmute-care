package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleSummaryResponse;
import vn.edu.hcmute.utecare.model.Schedule;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DoctorMapper.class, TimeSlotMapper.class
, RoomDetailMapper.class})
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    Schedule toEntity(ScheduleRequest request);

    ScheduleInfoResponse toInfoResponse(Schedule schedule);

    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "doctorName", source = "doctor.fullName")
    @Mapping(target = "doctorGender", source = "doctor.gender")
    @Mapping(target = "doctorQualification", source = "doctor.qualification")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    @Mapping(target = "roomName", source = "roomDetail.name")
    ScheduleSummaryResponse toSummaryResponse(Schedule schedule);

    @Mapping(target = "bookedSlots", ignore = true)
    void updateEntity(ScheduleRequest request, @MappingTarget Schedule schedule);

}
