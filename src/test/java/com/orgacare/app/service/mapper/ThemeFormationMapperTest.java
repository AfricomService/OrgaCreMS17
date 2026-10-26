package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThemeFormationMapperTest {

    private ThemeFormationMapper themeFormationMapper;

    @BeforeEach
    public void setUp() {
        themeFormationMapper = new ThemeFormationMapperImpl();
    }
}
