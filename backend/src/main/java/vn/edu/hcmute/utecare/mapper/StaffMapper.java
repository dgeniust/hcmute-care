package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;
import vn.edu.hcmute.utecare.model.Staff;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    Staff toEntity(StaffRequest request);

    StaffResponse toResponse(Staff staff);

    void updateEntity(StaffRequest request, @MappingTarget Staff staff);
}