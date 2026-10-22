package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationPersonneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormationPersonneDTO.class);
        SessionFormationPersonneDTO sessionFormationPersonneDTO1 = new SessionFormationPersonneDTO();
        sessionFormationPersonneDTO1.setId(1L);
        SessionFormationPersonneDTO sessionFormationPersonneDTO2 = new SessionFormationPersonneDTO();
        assertThat(sessionFormationPersonneDTO1).isNotEqualTo(sessionFormationPersonneDTO2);
        sessionFormationPersonneDTO2.setId(sessionFormationPersonneDTO1.getId());
        assertThat(sessionFormationPersonneDTO1).isEqualTo(sessionFormationPersonneDTO2);
        sessionFormationPersonneDTO2.setId(2L);
        assertThat(sessionFormationPersonneDTO1).isNotEqualTo(sessionFormationPersonneDTO2);
        sessionFormationPersonneDTO1.setId(null);
        assertThat(sessionFormationPersonneDTO1).isNotEqualTo(sessionFormationPersonneDTO2);
    }
}
