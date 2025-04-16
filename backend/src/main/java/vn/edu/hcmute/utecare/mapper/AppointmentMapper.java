package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.response.AppointmentDetailResponse;
import vn.edu.hcmute.utecare.model.Appointment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = { DoctorScheduleMapper.class},
imports = {MedicalRecordMapper.class})
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(AppointmentMapper.class);

    AppointmentDetailResponse toDetailResponse(Appointment appointment);
}
