package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormeJuridiqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormeJuridiqueDTO.class);
        FormeJuridiqueDTO formeJuridiqueDTO1 = new FormeJuridiqueDTO();
        formeJuridiqueDTO1.setId(1L);
        FormeJuridiqueDTO formeJuridiqueDTO2 = new FormeJuridiqueDTO();
        assertThat(formeJuridiqueDTO1).isNotEqualTo(formeJuridiqueDTO2);
        formeJuridiqueDTO2.setId(formeJuridiqueDTO1.getId());
        assertThat(formeJuridiqueDTO1).isEqualTo(formeJuridiqueDTO2);
        formeJuridiqueDTO2.setId(2L);
        assertThat(formeJuridiqueDTO1).isNotEqualTo(formeJuridiqueDTO2);
        formeJuridiqueDTO1.setId(null);
        assertThat(formeJuridiqueDTO1).isNotEqualTo(formeJuridiqueDTO2);
    }
}
