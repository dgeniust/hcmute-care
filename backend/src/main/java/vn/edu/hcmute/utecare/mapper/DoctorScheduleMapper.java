package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleSummaryResponse;
import vn.edu.hcmute.utecare.model.DoctorSchedule;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DoctorMapper.class, TimeSlotMapper.class
, RoomDetailMapper.class})
public interface DoctorScheduleMapper {
    DoctorScheduleMapper INSTANCE = Mappers.getMapper(DoctorScheduleMapper.class);

    DoctorSchedule toEntity(DoctorScheduleRequest request);

    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "timeSlot", source = "timeSlot")
    @Mapping(target = "roomDetail", source = "roomDetail")
    DoctorScheduleResponse toResponse(DoctorSchedule schedule);

    @Mapping(target = "doctorName", source = "doctor.fullName")
    @Mapping(target = "doctorGender", source = "doctor.gender")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    @Mapping(target = "roomName", source = "roomDetail.name")
    DoctorScheduleSummaryResponse toSummaryResponse(DoctorSchedule schedule);

    @Mapping(target = "bookedSlots", ignore = true)
    void updateEntity(DoctorScheduleRequest request, @MappingTarget DoctorSchedule doctorSchedule);
}
