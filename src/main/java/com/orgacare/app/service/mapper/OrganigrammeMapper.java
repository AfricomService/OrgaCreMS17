package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Organigramme;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.service.dto.OrganigrammeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organigramme} and its DTO {@link OrganigrammeDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganigrammeMapper extends EntityMapper<OrganigrammeDTO, Organigramme> {
    // ── toDto : entité → DTO ─────────────────────────────────────────────
    @Mapping(target = "societeId", source = "societe.id")
    OrganigrammeDTO toDto(Organigramme s);

    // ── toEntity : DTO → entité ──────────────────────────────────────────
    @Mapping(target = "societe", source = "societeId", qualifiedByName = "societeFromId")
    Organigramme toEntity(OrganigrammeDTO organigrammeDTO);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganigrammeDTO toDtoId(Organigramme organigramme);

    // ── helper inverse ───────────────────────────────────────────────────

    @Named("societeFromId")
    default Societe societeFromId(Long id) {
        if (id == null) return null;
        Societe s = new Societe();
        s.setId(id);
        return s;
    }
}
