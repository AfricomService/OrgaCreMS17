package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganigrammeMapperTest {

    private OrganigrammeMapper organigrammeMapper;

    @BeforeEach
    public void setUp() {
        organigrammeMapper = new OrganigrammeMapperImpl();
    }
}
