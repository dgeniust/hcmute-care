package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordInfoResponse;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.model.MedicalRecord;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PatientMapper.class})
public interface MedicalRecordMapper {
    @Mapping(target = "customerId", source = "customer.id")
    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);


    @Mapping(target = "customer.id", source = "customerId")
    MedicalRecord toEntity(MedicalRecordRequest request);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "dob", source = "patient.dob")
    @Mapping(target = "gender", source = "patient.gender")
    @Mapping(target = "phone", source = "patient.phone")
    MedicalRecordInfoResponse toInfoResponse(MedicalRecord medicalRecord);
}