package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;

import vn.edu.hcmute.utecare.dto.request.PatientRequest;
import vn.edu.hcmute.utecare.dto.response.PatientResponse;
import vn.edu.hcmute.utecare.model.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toResponse(Patient patient);
    Patient toEntity(PatientRequest request);
}