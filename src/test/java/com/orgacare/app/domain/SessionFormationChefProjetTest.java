package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationChefProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormationChefProjet.class);
        SessionFormationChefProjet sessionFormationChefProjet1 = new SessionFormationChefProjet();
        sessionFormationChefProjet1.setId(1L);
        SessionFormationChefProjet sessionFormationChefProjet2 = new SessionFormationChefProjet();
        sessionFormationChefProjet2.setId(sessionFormationChefProjet1.getId());
        assertThat(sessionFormationChefProjet1).isEqualTo(sessionFormationChefProjet2);
        sessionFormationChefProjet2.setId(2L);
        assertThat(sessionFormationChefProjet1).isNotEqualTo(sessionFormationChefProjet2);
        sessionFormationChefProjet1.setId(null);
        assertThat(sessionFormationChefProjet1).isNotEqualTo(sessionFormationChefProjet2);
    }
}
