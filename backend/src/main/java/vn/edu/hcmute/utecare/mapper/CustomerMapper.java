package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.CustomerRequest;
import vn.edu.hcmute.utecare.dto.response.CustomerResponse;
import vn.edu.hcmute.utecare.model.Customer;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequest request);

    CustomerResponse toResponse(Customer customer);

    void updateEntity(CustomerRequest request, @MappingTarget Customer customer);
}