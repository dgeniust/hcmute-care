package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.TicketScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.TimeSlotResponse;
import vn.edu.hcmute.utecare.model.Schedule;
import vn.edu.hcmute.utecare.model.ScheduleSlot;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleSlotMapper.class, TimeSlotMapper.class})
public interface ScheduleMapper {

    Schedule toEntity(ScheduleRequest request);

    @Mapping(target = "scheduleSlots", source = "scheduleSlots")
    ScheduleInfoResponse toInfoResponse(Schedule schedule);

    @Mapping(target = "scheduleSlots", source = "scheduleSlots")
    ScheduleResponse toResponse(Schedule schedule);

    TicketScheduleResponse toTicketScheduleResponse(Schedule schedule);
}
