package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Personne;
import com.orgacare.app.service.dto.PersonneDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personne} and its DTO {@link PersonneDTO}.
 */
@Mapper(componentModel = "spring", uses = { AffectationMapper.class, GradeMapper.class, FonctionMapper.class })
public interface PersonneMapper extends EntityMapper<PersonneDTO, Personne> {
    @Mapping(target = "affectationId", source = "affectation.id")
    @Mapping(target = "gradeId", source = "grade.id")
    @Mapping(target = "fonctionId", source = "fonction.id")
    @Mapping(target = "userId", source = "userId")
    PersonneDTO toDto(Personne s);

    @Mapping(target = "affectation", source = "affectationId", qualifiedByName = "affectationFromId")
    @Mapping(target = "grade", source = "gradeId", qualifiedByName = "gradeFromId")
    @Mapping(target = "fonction", source = "fonctionId", qualifiedByName = "fonctionFromId")
    @Mapping(target = "userId", source = "userId")
    Personne toEntity(PersonneDTO dto);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoId(Personne personne);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PersonneDTO> toDtoIdSet(Set<Personne> personne);

    // ── helpers inverse (DTO id → entité shell) ──────────────────────────

    @Named("affectationFromId")
    default com.orgacare.app.domain.Affectation affectationFromId(Long id) {
        if (id == null) return null;
        com.orgacare.app.domain.Affectation a = new com.orgacare.app.domain.Affectation();
        a.setId(id);
        return a;
    }

    @Named("gradeFromId")
    default com.orgacare.app.domain.Grade gradeFromId(Long id) {
        if (id == null) return null;
        com.orgacare.app.domain.Grade g = new com.orgacare.app.domain.Grade();
        g.setId(id);
        return g;
    }

    @Named("fonctionFromId")
    default com.orgacare.app.domain.Fonction fonctionFromId(Long id) {
        if (id == null) return null;
        com.orgacare.app.domain.Fonction f = new com.orgacare.app.domain.Fonction();
        f.setId(id);
        return f;
    }
}
