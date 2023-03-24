package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.tag.TagGetDto;
import com.avengers.gamera.dto.tag.TagPostDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    TagGetDto tagToTagGetDto(Tag tag);

    TagPostDto tagToTagPostDto(Tag tag);

    Tag tagPostDtoToTag(TagPostDto tagPostDto);

    TagSlimDto tagToTagSlimDto (Tag tag);


}
