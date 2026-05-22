package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeContratMapperTest {

    private TypeContratMapper typeContratMapper;

    @BeforeEach
    public void setUp() {
        typeContratMapper = new TypeContratMapperImpl();
    }
}
