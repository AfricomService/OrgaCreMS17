package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormationDTO.class);
        SessionFormationDTO sessionFormationDTO1 = new SessionFormationDTO();
        sessionFormationDTO1.setId(1L);
        SessionFormationDTO sessionFormationDTO2 = new SessionFormationDTO();
        assertThat(sessionFormationDTO1).isNotEqualTo(sessionFormationDTO2);
        sessionFormationDTO2.setId(sessionFormationDTO1.getId());
        assertThat(sessionFormationDTO1).isEqualTo(sessionFormationDTO2);
        sessionFormationDTO2.setId(2L);
        assertThat(sessionFormationDTO1).isNotEqualTo(sessionFormationDTO2);
        sessionFormationDTO1.setId(null);
        assertThat(sessionFormationDTO1).isNotEqualTo(sessionFormationDTO2);
    }
}
