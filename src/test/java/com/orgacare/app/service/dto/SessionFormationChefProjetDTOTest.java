package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationChefProjetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormationChefProjetDTO.class);
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO1 = new SessionFormationChefProjetDTO();
        sessionFormationChefProjetDTO1.setId(1L);
        SessionFormationChefProjetDTO sessionFormationChefProjetDTO2 = new SessionFormationChefProjetDTO();
        assertThat(sessionFormationChefProjetDTO1).isNotEqualTo(sessionFormationChefProjetDTO2);
        sessionFormationChefProjetDTO2.setId(sessionFormationChefProjetDTO1.getId());
        assertThat(sessionFormationChefProjetDTO1).isEqualTo(sessionFormationChefProjetDTO2);
        sessionFormationChefProjetDTO2.setId(2L);
        assertThat(sessionFormationChefProjetDTO1).isNotEqualTo(sessionFormationChefProjetDTO2);
        sessionFormationChefProjetDTO1.setId(null);
        assertThat(sessionFormationChefProjetDTO1).isNotEqualTo(sessionFormationChefProjetDTO2);
    }
}
