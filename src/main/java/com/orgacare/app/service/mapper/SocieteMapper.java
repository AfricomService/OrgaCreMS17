package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Societe;
import com.orgacare.app.service.dto.SocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Societe} and its DTO {@link SocieteDTO}.
 */
@Mapper(componentModel = "spring", uses = { FormeJuridiqueMapper.class })
public interface SocieteMapper extends EntityMapper<SocieteDTO, Societe> {
    @Mapping(target = "formeJuridiqueeId", source = "formeJuridiquee.id")
    SocieteDTO toDto(Societe s);

    @Mapping(target = "formeJuridiquee", source = "formeJuridiqueeId", qualifiedByName = "formeJuridiqueFromId")
    Societe toEntity(SocieteDTO dto);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocieteDTO toDtoId(Societe societe);

    @Named("raisonSociale")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "raisonSociale", source = "raisonSociale")
    SocieteDTO toDtoRaisonSociale(Societe societe);

    // ── helper inverse (id → entité shell) ───────────────────────────────

    @Named("formeJuridiqueFromId")
    default com.orgacare.app.domain.FormeJuridique formeJuridiqueFromId(Long id) {
        if (id == null) return null;
        com.orgacare.app.domain.FormeJuridique fj = new com.orgacare.app.domain.FormeJuridique();
        fj.setId(id);
        return fj;
    }
}
