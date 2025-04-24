package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.dto.response.AppointmentSummaryResponse;
import vn.edu.hcmute.utecare.model.Appointment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = { ScheduleMapper.class,MedicalRecordMapper.class,
PatientMapper.class, AppointmentScheduleMapper.class })
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(AppointmentMapper.class);

    AppointmentDetailResponse toDetailResponse(Appointment appointment);

    @Mapping(target = "patient", source = "medicalRecord.patient")
    AppointmentSummaryResponse toSummaryResponse(Appointment appointment);
}
