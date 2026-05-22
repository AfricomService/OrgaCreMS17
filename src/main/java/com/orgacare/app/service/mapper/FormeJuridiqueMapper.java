package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.FormeJuridique;
import com.orgacare.app.service.dto.FormeJuridiqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormeJuridique} and its DTO {@link FormeJuridiqueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormeJuridiqueMapper extends EntityMapper<FormeJuridiqueDTO, FormeJuridique> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormeJuridiqueDTO toDtoId(FormeJuridique formeJuridique);
}
