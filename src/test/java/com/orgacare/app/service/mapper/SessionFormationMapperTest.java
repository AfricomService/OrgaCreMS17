package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionFormationMapperTest {

    private SessionFormationMapper sessionFormationMapper;

    @BeforeEach
    public void setUp() {
        sessionFormationMapper = new SessionFormationMapperImpl();
    }
}
