package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PieceJointeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PieceJointe.class);
        PieceJointe pieceJointe1 = new PieceJointe();
        pieceJointe1.setId(1L);
        PieceJointe pieceJointe2 = new PieceJointe();
        pieceJointe2.setId(pieceJointe1.getId());
        assertThat(pieceJointe1).isEqualTo(pieceJointe2);
        pieceJointe2.setId(2L);
        assertThat(pieceJointe1).isNotEqualTo(pieceJointe2);
        pieceJointe1.setId(null);
        assertThat(pieceJointe1).isNotEqualTo(pieceJointe2);
    }
}
