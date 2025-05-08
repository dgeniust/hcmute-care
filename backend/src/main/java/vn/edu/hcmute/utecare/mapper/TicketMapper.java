package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentTicketResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.model.Ticket;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleMapper.class, ScheduleSlotMapper.class, MedicalRecordMapper.class,})
public interface TicketMapper {
    TicketMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "schedule", source = "scheduleSlot.schedule")
    @Mapping(target = "medicalRecord", source = "appointment.medicalRecord")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "patientName", source = "appointment.medicalRecord.patient.name")
    @Mapping(target = "patientGender", source = "appointment.medicalRecord.patient.gender")
    @Mapping(target = "patientDob", source = "appointment.medicalRecord.patient.dob")
    @Mapping(target = "medicalRecordId", source = "appointment.medicalRecord.id")
    @Mapping(target = "medicalRecordBarcode", source = "appointment.medicalRecord.barcode")
    DoctorTicketSummaryResponse toDoctorTicketSummaryResponse(Ticket ticket);

    @Mapping(target = "schedule", source = "scheduleSlot.schedule")
    AppointmentTicketResponse toAppointmentTicketResponse(Ticket ticket);
}
