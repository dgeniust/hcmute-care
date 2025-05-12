package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PostImageRequest;
import vn.edu.hcmute.utecare.dto.request.PostRequest;
import vn.edu.hcmute.utecare.dto.response.PostResponse;
import vn.edu.hcmute.utecare.model.Post;
import vn.edu.hcmute.utecare.model.PostImage;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PostImageMapper.class)
public interface PostMapper {
    @Mapping(target = "staffId", source = "staff.id")
    PostResponse toResponse(Post post);

    Post toEntity(PostRequest request);

    void updateEntity(PostRequest request, @MappingTarget Post post);
}