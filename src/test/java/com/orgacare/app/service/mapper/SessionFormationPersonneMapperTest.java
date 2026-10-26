package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionFormationPersonneMapperTest {

    private SessionFormationPersonneMapper sessionFormationPersonneMapper;

    @BeforeEach
    public void setUp() {
        sessionFormationPersonneMapper = new SessionFormationPersonneMapperImpl();
    }
}
