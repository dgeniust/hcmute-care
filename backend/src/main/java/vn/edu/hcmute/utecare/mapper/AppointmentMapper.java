package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentResponse;
import vn.edu.hcmute.utecare.model.Appointment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {TicketMapper.class, MedicalRecordMapper.class})
public interface AppointmentMapper {
    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "medicalRecord", source = "medicalRecord")
    AppointmentResponse toResponse(Appointment appointment);
}
