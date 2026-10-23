package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PieceJointeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PieceJointeDTO.class);
        PieceJointeDTO pieceJointeDTO1 = new PieceJointeDTO();
        pieceJointeDTO1.setId(1L);
        PieceJointeDTO pieceJointeDTO2 = new PieceJointeDTO();
        assertThat(pieceJointeDTO1).isNotEqualTo(pieceJointeDTO2);
        pieceJointeDTO2.setId(pieceJointeDTO1.getId());
        assertThat(pieceJointeDTO1).isEqualTo(pieceJointeDTO2);
        pieceJointeDTO2.setId(2L);
        assertThat(pieceJointeDTO1).isNotEqualTo(pieceJointeDTO2);
        pieceJointeDTO1.setId(null);
        assertThat(pieceJointeDTO1).isNotEqualTo(pieceJointeDTO2);
    }
}
