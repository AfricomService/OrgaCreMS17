package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonneMapperTest {

    private PersonneMapper personneMapper;

    @BeforeEach
    public void setUp() {
        personneMapper = new PersonneMapperImpl();
    }
}
