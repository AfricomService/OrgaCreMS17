package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupeMapperTest {

    private GroupeMapper groupeMapper;

    @BeforeEach
    public void setUp() {
        groupeMapper = new GroupeMapperImpl();
    }
}
