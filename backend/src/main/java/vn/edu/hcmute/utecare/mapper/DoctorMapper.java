package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorInfoResponse;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalSpecialtyResponse;
import vn.edu.hcmute.utecare.model.Doctor;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {MedicalSpecialtyResponse.class})
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    Doctor toEntity(DoctorRequest request);

    DoctorResponse toResponse(Doctor doctor);

    DoctorInfoResponse toInfoResponse(Doctor doctor);

    void updateEntity(DoctorRequest request,@MappingTarget Doctor doctor);
}
