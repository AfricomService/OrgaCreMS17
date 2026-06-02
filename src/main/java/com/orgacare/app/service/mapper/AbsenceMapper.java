package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Absence;
import com.orgacare.app.domain.Personne;
import com.orgacare.app.service.dto.AbsenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {
    // ── toDto : entité → DTO ─────────────────────────────────────────────
    @Mapping(target = "personneAbscentId", source = "personneAbscent.id")
    @Mapping(target = "personneRemplacantId", source = "personneRemplacant.id")
    AbsenceDTO toDto(Absence s);

    // ── toEntity : DTO → entité ──────────────────────────────────────────
    @Mapping(target = "personneAbscent", source = "personneAbscentId", qualifiedByName = "personneFromId")
    @Mapping(target = "personneRemplacant", source = "personneRemplacantId", qualifiedByName = "personneFromId")
    Absence toEntity(AbsenceDTO absenceDTO);

    // ── helper inverse ───────────────────────────────────────────────────
    @Named("personneFromId")
    default Personne personneFromId(Long id) {
        if (id == null) return null;
        Personne p = new Personne();
        p.setId(id);
        return p;
    }
}
