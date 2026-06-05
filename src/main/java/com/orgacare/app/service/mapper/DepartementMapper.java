package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Departement;
import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.domain.Site;
import com.orgacare.app.service.dto.DepartementDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    // ── toDto : entité → DTO ─────────────────────────────────────────────
    @Mapping(target = "organigrammeId", source = "organigramme.id")
    @Mapping(target = "siteId", source = "site.id")
    @Mapping(target = "departementParentId", source = "departementParent.id")
    @Mapping(target = "societeId", source = "organigramme.societe.id")
    @Mapping(target = "personnes", source = "personnes", qualifiedByName = "idSet")
    DepartementDTO toDto(Departement s);

    // ── toEntity : DTO → entité ──────────────────────────────────────────
    @Mapping(target = "organigramme", source = "organigrammeId", qualifiedByName = "organigrammeFromId")
    @Mapping(target = "site", source = "siteId", qualifiedByName = "siteFromId")
    @Mapping(target = "departementParent", source = "departementParentId", qualifiedByName = "departementFromId")
    @Mapping(target = "removePersonne", ignore = true)
    Departement toEntity(DepartementDTO departementDTO);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartementDTO toDtoId(Departement departement);

    // ── helpers inverse ──────────────────────────────────────────────────

    @Named("organigrammeFromId")
    default Organigramme organigrammeFromId(Long id) {
        if (id == null) return null;
        Organigramme o = new Organigramme();
        o.setId(id);
        return o;
    }

    @Named("siteFromId")
    default Site siteFromId(Long id) {
        if (id == null) return null;
        Site s = new Site();
        s.setId(id);
        return s;
    }

    @Named("departementFromId")
    default Departement departementFromId(Long id) {
        if (id == null) return null;
        Departement d = new Departement();
        d.setId(id);
        return d;
    }
}
