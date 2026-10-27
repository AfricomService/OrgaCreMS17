package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Salaire;
import com.orgacare.app.service.dto.SalaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salaire} and its DTO {@link SalaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SalaireMapper extends EntityMapper<SalaireDTO, Salaire> {}
