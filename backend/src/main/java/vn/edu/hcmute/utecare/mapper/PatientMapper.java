package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PatientRequest;
import vn.edu.hcmute.utecare.dto.response.PatientInfoResponse;
import vn.edu.hcmute.utecare.dto.response.PatientResponse;
import vn.edu.hcmute.utecare.model.Patient;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);
    PatientResponse toResponse(Patient patient);
    Patient toEntity(PatientRequest request);

    PatientInfoResponse toInfoResponse(Patient patient);
}