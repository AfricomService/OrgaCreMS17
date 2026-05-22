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
    @Mapping(target = "affectation", source = "affectation", qualifiedByName = "id")
    @Mapping(target = "grade", source = "grade", qualifiedByName = "id")
    @Mapping(target = "fonction", source = "fonction", qualifiedByName = "id")
    PersonneDTO toDto(Personne s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoId(Personne personne);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PersonneDTO> toDtoIdSet(Set<Personne> personne);
}
