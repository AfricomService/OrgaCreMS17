package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaireMapperTest {

    private SalaireMapper salaireMapper;

    @BeforeEach
    public void setUp() {
        salaireMapper = new SalaireMapperImpl();
    }
}
