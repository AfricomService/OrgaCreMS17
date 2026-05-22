package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.TypeContrat;
import com.orgacare.app.service.dto.TypeContratDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeContrat} and its DTO {@link TypeContratDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeContratMapper extends EntityMapper<TypeContratDTO, TypeContrat> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeContratDTO toDtoId(TypeContrat typeContrat);
}
