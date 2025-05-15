package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentTicketResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorTicketSummaryResponse;
import vn.edu.hcmute.utecare.dto.response.TicketResponse;
import vn.edu.hcmute.utecare.model.Ticket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleMapper.class, ScheduleSlotMapper.class, MedicalRecordMapper.class,})
public interface TicketMapper {
    @Mapping(target = "schedule", source = "scheduleSlot.schedule")
    @Mapping(target = "medicalRecord", source = "appointment.medicalRecord")
    @Mapping(target = "timeSlot", source = "scheduleSlot.timeSlot")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "patientName", source = "appointment.medicalRecord.patient.name")
    @Mapping(target = "patientGender", source = "appointment.medicalRecord.patient.gender")
    @Mapping(target = "patientDob", source = "appointment.medicalRecord.patient.dob")
    @Mapping(target = "medicalRecordId", source = "appointment.medicalRecord.id")
    @Mapping(target = "medicalRecordBarcode", source = "appointment.medicalRecord.barcode")
    DoctorTicketSummaryResponse toDoctorTicketSummaryResponse(Ticket ticket);

    @Mapping(target = "schedule", source = "scheduleSlot.schedule")
    @Mapping(target = "timeSlot", source = "scheduleSlot.timeSlot")
    AppointmentTicketResponse toAppointmentTicketResponse(Ticket ticket);
}
