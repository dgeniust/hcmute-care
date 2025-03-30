package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.model.DoctorSchedule;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DoctorMapper.class, TimeSlotMapper.class})
public interface DoctorScheduleMapper {
    DoctorScheduleMapper INSTANCE = Mappers.getMapper(DoctorScheduleMapper.class);

    DoctorSchedule toEntity(DoctorScheduleRequest request);

    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "timeSlot", source = "timeSlot")
    DoctorScheduleResponse toResponse(DoctorSchedule schedule);

    @Mapping(target = "bookedSlots", ignore = true)
    void updateEntity(DoctorScheduleRequest request, @MappingTarget DoctorSchedule doctorSchedule);
}
