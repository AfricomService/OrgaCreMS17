package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Groupe;
import com.orgacare.app.service.dto.GroupeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Groupe} and its DTO {@link GroupeDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrganigrammeMapper.class })
public interface GroupeMapper extends EntityMapper<GroupeDTO, Groupe> {
    @Mapping(target = "organigramme", source = "organigramme", qualifiedByName = "id")
    GroupeDTO toDto(Groupe s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupeDTO toDtoId(Groupe groupe);
}
