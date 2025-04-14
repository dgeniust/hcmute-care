package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.model.MedicalRecord;

@Mapper(componentModel = "spring", uses = {PatientMapper.class})
public interface MedicalRecordMapper {
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "patient", source = "patient")
    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);

    @Mapping(target = "patient", source = "patient")
    @Mapping(target = "customer.id", source = "customerId")
    MedicalRecord toEntity(MedicalRecordRequest request);
}