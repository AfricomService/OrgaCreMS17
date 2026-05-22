package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Departement;
import com.orgacare.app.service.dto.DepartementDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrganigrammeMapper.class, SiteMapper.class, PersonneMapper.class })
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    @Mapping(target = "organigramme", source = "organigramme", qualifiedByName = "id")
    @Mapping(target = "site", source = "site", qualifiedByName = "id")
    @Mapping(target = "departementParent", source = "departementParent", qualifiedByName = "id")
    @Mapping(target = "personnes", source = "personnes", qualifiedByName = "idSet")
    DepartementDTO toDto(Departement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartementDTO toDtoId(Departement departement);

    @Mapping(target = "removePersonne", ignore = true)
    Departement toEntity(DepartementDTO departementDTO);
}
