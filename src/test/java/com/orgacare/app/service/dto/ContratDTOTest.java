package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContratDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContratDTO.class);
        ContratDTO contratDTO1 = new ContratDTO();
        contratDTO1.setId(1L);
        ContratDTO contratDTO2 = new ContratDTO();
        assertThat(contratDTO1).isNotEqualTo(contratDTO2);
        contratDTO2.setId(contratDTO1.getId());
        assertThat(contratDTO1).isEqualTo(contratDTO2);
        contratDTO2.setId(2L);
        assertThat(contratDTO1).isNotEqualTo(contratDTO2);
        contratDTO1.setId(null);
        assertThat(contratDTO1).isNotEqualTo(contratDTO2);
    }
}
