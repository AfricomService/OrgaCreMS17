package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.service.dto.AffectationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Affectation} and its DTO {@link AffectationDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartementMapper.class, GroupeMapper.class, SocieteMapper.class })
public interface AffectationMapper extends EntityMapper<AffectationDTO, Affectation> {
    @Mapping(target = "departement", source = "departement", qualifiedByName = "id")
    @Mapping(target = "groupe", source = "groupe", qualifiedByName = "id")
    @Mapping(target = "societe", source = "societe", qualifiedByName = "id")
    AffectationDTO toDto(Affectation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AffectationDTO toDtoId(Affectation affectation);
}
