package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormeJuridiqueMapperTest {

    private FormeJuridiqueMapper formeJuridiqueMapper;

    @BeforeEach
    public void setUp() {
        formeJuridiqueMapper = new FormeJuridiqueMapperImpl();
    }
}
