package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Absence;
import com.orgacare.app.service.dto.AbsenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Absence} and its DTO {@link AbsenceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {
    @Mapping(target = "personneAbscent", source = "personneAbscent", qualifiedByName = "id")
    @Mapping(target = "personneRemplacant", source = "personneRemplacant", qualifiedByName = "id")
    AbsenceDTO toDto(Absence s);
}
