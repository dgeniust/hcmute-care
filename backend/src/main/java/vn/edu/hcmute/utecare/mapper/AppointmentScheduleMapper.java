package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorAppointmentResponse;
import vn.edu.hcmute.utecare.model.AppointmentSchedule;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {ScheduleMapper.class, DoctorMapper.class, PatientMapper.class, TimeSlotMapper.class})
public interface AppointmentScheduleMapper {
    AppointmentScheduleMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(AppointmentScheduleMapper.class);

    AppointmentScheduleResponse toResponse(AppointmentSchedule appointmentSchedule);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "date", source = "schedule.date")
    @Mapping(target = "patient", source = "appointment.medicalRecord.patient")
    @Mapping(target = "timeSlot", source = "schedule.timeSlot")
    DoctorAppointmentResponse toDoctorAppointmentResponse(AppointmentSchedule appointmentSchedule);

    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "date", source = "schedule.date")
    @Mapping(target = "doctor", source = "schedule.doctor")
    AppointmentScheduleInfoResponse toInfoResponse(AppointmentSchedule appointmentSchedule);
}
