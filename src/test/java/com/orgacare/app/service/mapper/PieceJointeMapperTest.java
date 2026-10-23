package com.orgacare.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PieceJointeMapperTest {

    private PieceJointeMapper pieceJointeMapper;

    @BeforeEach
    public void setUp() {
        pieceJointeMapper = new PieceJointeMapperImpl();
    }
}
