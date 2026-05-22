package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Societe;
import com.orgacare.app.service.dto.SocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Societe} and its DTO {@link SocieteDTO}.
 */
@Mapper(componentModel = "spring", uses = { FormeJuridiqueMapper.class })
public interface SocieteMapper extends EntityMapper<SocieteDTO, Societe> {
    @Mapping(target = "formeJuridiquee", source = "formeJuridiquee", qualifiedByName = "id")
    SocieteDTO toDto(Societe s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocieteDTO toDtoId(Societe societe);
}
