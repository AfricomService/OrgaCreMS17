package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Contrat;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.domain.Societe;
import com.orgacare.app.domain.TypeContrat;
import com.orgacare.app.service.dto.ContratDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contrat} and its DTO {@link ContratDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContratMapper extends EntityMapper<ContratDTO, Contrat> {
    // ── toDto : entité → DTO ─────────────────────────────────────────────
    @Mapping(target = "societeId", source = "societe.id")
    @Mapping(target = "typeContratId", source = "typeContrat.id")
    @Mapping(target = "personneId", source = "personne.id")
    // champs dénormalisés
    @Mapping(target = "nomPersonne", source = "personne.nomPrenom")
    @Mapping(target = "matriculePersonne", source = "personne.matricule")
    @Mapping(target = "raisonSocialeSociete", source = "societe.raisonSociale")
    @Mapping(target = "nomTypeContrat", source = "typeContrat.nom")
    ContratDTO toDto(Contrat s);

    // ── toEntity : DTO → entité ──────────────────────────────────────────
    @Mapping(target = "societe", source = "societeId", qualifiedByName = "societeFromId")
    @Mapping(target = "typeContrat", source = "typeContratId", qualifiedByName = "typeContratFromId")
    @Mapping(target = "personne", source = "personneId", qualifiedByName = "personneFromId")
    Contrat toEntity(ContratDTO contratDTO);

    // ── helpers inverse ──────────────────────────────────────────────────

    @Named("societeFromId")
    default Societe societeFromId(Long id) {
        if (id == null) return null;
        Societe s = new Societe();
        s.setId(id);
        return s;
    }

    @Named("typeContratFromId")
    default TypeContrat typeContratFromId(Long id) {
        if (id == null) return null;
        TypeContrat t = new TypeContrat();
        t.setId(id);
        return t;
    }

    @Named("personneFromId")
    default Personne personneFromId(Long id) {
        if (id == null) return null;
        Personne p = new Personne();
        p.setId(id);
        return p;
    }
}
