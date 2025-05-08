package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PostImageRequest;
import vn.edu.hcmute.utecare.dto.response.PostImageResponse;
import vn.edu.hcmute.utecare.model.PostImage;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostImageMapper {
    PostImageMapper INSTANCE = Mappers.getMapper(PostImageMapper.class);

    PostImage toEntity(PostImageRequest request);
    PostImageResponse toResponse(PostImage postImage);
}
