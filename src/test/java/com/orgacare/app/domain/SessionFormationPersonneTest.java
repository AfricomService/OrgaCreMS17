package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationPersonneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormationPersonne.class);
        SessionFormationPersonne sessionFormationPersonne1 = new SessionFormationPersonne();
        sessionFormationPersonne1.setId(1L);
        SessionFormationPersonne sessionFormationPersonne2 = new SessionFormationPersonne();
        sessionFormationPersonne2.setId(sessionFormationPersonne1.getId());
        assertThat(sessionFormationPersonne1).isEqualTo(sessionFormationPersonne2);
        sessionFormationPersonne2.setId(2L);
        assertThat(sessionFormationPersonne1).isNotEqualTo(sessionFormationPersonne2);
        sessionFormationPersonne1.setId(null);
        assertThat(sessionFormationPersonne1).isNotEqualTo(sessionFormationPersonne2);
    }
}
