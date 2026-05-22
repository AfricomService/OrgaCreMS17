package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Contrat;
import com.orgacare.app.service.dto.ContratDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contrat} and its DTO {@link ContratDTO}.
 */
@Mapper(componentModel = "spring", uses = { SocieteMapper.class, TypeContratMapper.class, PersonneMapper.class })
public interface ContratMapper extends EntityMapper<ContratDTO, Contrat> {
    @Mapping(target = "societe", source = "societe", qualifiedByName = "id")
    @Mapping(target = "typeContrat", source = "typeContrat", qualifiedByName = "id")
    @Mapping(target = "personne", source = "personne", qualifiedByName = "id")
    ContratDTO toDto(Contrat s);
}
