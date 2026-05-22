package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Fonction;
import com.orgacare.app.service.dto.FonctionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fonction} and its DTO {@link FonctionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FonctionMapper extends EntityMapper<FonctionDTO, Fonction> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FonctionDTO toDtoId(Fonction fonction);
}
