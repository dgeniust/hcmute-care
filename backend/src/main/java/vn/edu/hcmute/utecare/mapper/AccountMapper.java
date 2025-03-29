package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.AccountRequest;
import vn.edu.hcmute.utecare.dto.response.AccountResponse;
import vn.edu.hcmute.utecare.model.Account;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toEntity(AccountRequest request);

    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    AccountResponse toResponse(Account account);
}
