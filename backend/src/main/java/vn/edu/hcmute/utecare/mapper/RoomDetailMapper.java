package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import vn.edu.hcmute.utecare.dto.request.RoomDetailRequest;
import vn.edu.hcmute.utecare.dto.response.RoomDetailResponse;
import vn.edu.hcmute.utecare.model.RoomDetail;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomDetailMapper {
    RoomDetailMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(RoomDetailMapper.class);

     RoomDetail toEntity(RoomDetailRequest request);
     RoomDetailResponse toResponse(RoomDetail roomDetail);

     void update(RoomDetailRequest request,@MappingTarget RoomDetail roomDetail);
}
