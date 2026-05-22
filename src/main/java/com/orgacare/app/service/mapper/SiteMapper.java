package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Site;
import com.orgacare.app.service.dto.SiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Site} and its DTO {@link SiteDTO}.
 */
@Mapper(componentModel = "spring", uses = { SocieteMapper.class })
public interface SiteMapper extends EntityMapper<SiteDTO, Site> {
    @Mapping(target = "societe", source = "societe", qualifiedByName = "id")
    SiteDTO toDto(Site s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteDTO toDtoId(Site site);
}
