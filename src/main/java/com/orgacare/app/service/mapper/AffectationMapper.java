package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Affectation;
import com.orgacare.app.domain.Departement;
import com.orgacare.app.domain.Groupe;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.service.dto.AffectationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Affectation} and its DTO {@link AffectationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationMapper extends EntityMapper<AffectationDTO, Affectation> {
    // ── toDto : entité → DTO ─────────────────────────────────────────────
    @Mapping(target = "departementId", source = "departement.id")
    @Mapping(target = "groupeId", source = "groupe.id")
    @Mapping(target = "societeId", source = "societe.id")
    AffectationDTO toDto(Affectation s);

    // ── toEntity : DTO → entité ──────────────────────────────────────────
    @Mapping(target = "departement", source = "departementId", qualifiedByName = "departementFromId")
    @Mapping(target = "groupe", source = "groupeId", qualifiedByName = "groupeFromId")
    @Mapping(target = "societe", source = "societeId", qualifiedByName = "societeFromId")
    Affectation toEntity(AffectationDTO affectationDTO);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AffectationDTO toDtoId(Affectation affectation);

    // ── helpers inverse ──────────────────────────────────────────────────

    @Named("departementFromId")
    default Departement departementFromId(Long id) {
        if (id == null) return null;
        Departement d = new Departement();
        d.setId(id);
        return d;
    }

    @Named("groupeFromId")
    default Groupe groupeFromId(Long id) {
        if (id == null) return null;
        Groupe g = new Groupe();
        g.setId(id);
        return g;
    }

    @Named("societeFromId")
    default Societe societeFromId(Long id) {
        if (id == null) return null;
        Societe s = new Societe();
        s.setId(id);
        return s;
    }
}
