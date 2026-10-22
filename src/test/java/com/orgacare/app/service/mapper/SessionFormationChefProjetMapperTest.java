package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionFormationChefProjetMapperTest {

    private SessionFormationChefProjetMapper sessionFormationChefProjetMapper;

    @BeforeEach
    public void setUp() {
        sessionFormationChefProjetMapper = new SessionFormationChefProjetMapperImpl();
    }
}
