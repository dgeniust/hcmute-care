package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.model.Schedule;
import vn.edu.hcmute.utecare.model.ScheduleSlot;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleSlotMapper.class, TimeSlotMapper.class})
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ScheduleMapper.class);

    Schedule toEntity(ScheduleRequest request);

    @Mapping(target = "timeSlots", source = "scheduleSlots", qualifiedByName = "mapTimeSlots")
    ScheduleInfoResponse toInfoResponse(Schedule schedule);

    @Mapping(target = "scheduleSlots", source = "scheduleSlots")
    ScheduleResponse toResponse(Schedule schedule);

    @Named("mapTimeSlots")
    default Set<TimeSlotResponse> mapTimeSlots(Set<ScheduleSlot> scheduleSlots) {
        return scheduleSlots.stream()
                .map(ScheduleSlot::getTimeSlot)
                .map(TimeSlotMapper.INSTANCE::toResponse)
                .collect(Collectors.toSet());
    }
}
