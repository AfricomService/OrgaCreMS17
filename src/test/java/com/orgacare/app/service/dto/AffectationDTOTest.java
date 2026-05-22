package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffectationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationDTO.class);
        AffectationDTO affectationDTO1 = new AffectationDTO();
        affectationDTO1.setId(1L);
        AffectationDTO affectationDTO2 = new AffectationDTO();
        assertThat(affectationDTO1).isNotEqualTo(affectationDTO2);
        affectationDTO2.setId(affectationDTO1.getId());
        assertThat(affectationDTO1).isEqualTo(affectationDTO2);
        affectationDTO2.setId(2L);
        assertThat(affectationDTO1).isNotEqualTo(affectationDTO2);
        affectationDTO1.setId(null);
        assertThat(affectationDTO1).isNotEqualTo(affectationDTO2);
    }
}
