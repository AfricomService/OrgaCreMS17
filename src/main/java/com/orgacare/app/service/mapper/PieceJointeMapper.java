package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.PieceJointe;
import com.orgacare.app.service.dto.PieceJointeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PieceJointe} and its DTO {@link PieceJointeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PieceJointeMapper extends EntityMapper<PieceJointeDTO, PieceJointe> {}
