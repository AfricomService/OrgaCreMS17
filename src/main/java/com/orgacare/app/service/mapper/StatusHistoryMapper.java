package com.orgacare.app.service.mapper;

import com.orgacare.app.domain.StatusHistory;
import com.orgacare.app.service.dto.StatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StatusHistory} and its DTO {@link StatusHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StatusHistoryMapper extends EntityMapper<StatusHistoryDTO, StatusHistory> {}
