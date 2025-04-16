package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.MedicalRecordRequest;
import vn.edu.hcmute.utecare.dto.response.MedicalRecordResponse;
import vn.edu.hcmute.utecare.model.MedicalRecord;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PatientMapper.class})
public interface MedicalRecordMapper {

    MedicalRecordMapper INSTANCE = Mappers.getMapper(MedicalRecordMapper.class);
    @Mapping(target = "customerId", source = "customer.id")
    MedicalRecordResponse toResponse(MedicalRecord medicalRecord);


    @Mapping(target = "customer.id", source = "customerId")
    MedicalRecord toEntity(MedicalRecordRequest request);
}