package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffectationMapperTest {

    private AffectationMapper affectationMapper;

    @BeforeEach
    public void setUp() {
        affectationMapper = new AffectationMapperImpl();
    }
}
