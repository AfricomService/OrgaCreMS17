package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaireDTO.class);
        SalaireDTO salaireDTO1 = new SalaireDTO();
        salaireDTO1.setId(1L);
        SalaireDTO salaireDTO2 = new SalaireDTO();
        assertThat(salaireDTO1).isNotEqualTo(salaireDTO2);
        salaireDTO2.setId(salaireDTO1.getId());
        assertThat(salaireDTO1).isEqualTo(salaireDTO2);
        salaireDTO2.setId(2L);
        assertThat(salaireDTO1).isNotEqualTo(salaireDTO2);
        salaireDTO1.setId(null);
        assertThat(salaireDTO1).isNotEqualTo(salaireDTO2);
    }
}
