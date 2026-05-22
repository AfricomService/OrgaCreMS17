package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.service.dto.OrganigrammeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organigramme} and its DTO {@link OrganigrammeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SocieteMapper.class })
public interface OrganigrammeMapper extends EntityMapper<OrganigrammeDTO, Organigramme> {
    @Mapping(target = "societe", source = "societe", qualifiedByName = "id")
    OrganigrammeDTO toDto(Organigramme s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganigrammeDTO toDtoId(Organigramme organigramme);
}
