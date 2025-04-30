package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.model.Ticket;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleMapper.class, ScheduleSlotMapper.class})
public interface TicketMapper {
    TicketMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "scheduleSlot", source = "scheduleSlot")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "patientName", source = "appointment.medicalRecord.patient.name")
    @Mapping(target = "patientGender", source = "appointment.medicalRecord.patient.gender")
    @Mapping(target = "patientDob", source = "appointment.medicalRecord.patient.dob")
    @Mapping(target = "medicalRecordId", source = "appointment.medicalRecord.id")
    @Mapping(target = "medicalRecordBarcode", source = "appointment.medicalRecord.barcode")
    DoctorTicketSummaryResponse toDoctorTicketSummaryResponse(Ticket ticket);
}
