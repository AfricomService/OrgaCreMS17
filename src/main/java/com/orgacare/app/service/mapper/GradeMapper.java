package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Grade;
import com.orgacare.app.service.dto.GradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grade} and its DTO {@link GradeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GradeMapper extends EntityMapper<GradeDTO, Grade> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GradeDTO toDtoId(Grade grade);
}
