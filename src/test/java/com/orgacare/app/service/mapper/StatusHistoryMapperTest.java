package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusHistoryMapperTest {

    private StatusHistoryMapper statusHistoryMapper;

    @BeforeEach
    public void setUp() {
        statusHistoryMapper = new StatusHistoryMapperImpl();
    }
}
