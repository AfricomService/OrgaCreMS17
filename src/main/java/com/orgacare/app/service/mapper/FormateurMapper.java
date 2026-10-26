package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.Formateur;
import com.orgacare.app.service.dto.FormateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formateur} and its DTO {@link FormateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormateurMapper extends EntityMapper<FormateurDTO, Formateur> {}
