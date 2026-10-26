package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionFormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionFormation.class);
        SessionFormation sessionFormation1 = new SessionFormation();
        sessionFormation1.setId(1L);
        SessionFormation sessionFormation2 = new SessionFormation();
        sessionFormation2.setId(sessionFormation1.getId());
        assertThat(sessionFormation1).isEqualTo(sessionFormation2);
        sessionFormation2.setId(2L);
        assertThat(sessionFormation1).isNotEqualTo(sessionFormation2);
        sessionFormation1.setId(null);
        assertThat(sessionFormation1).isNotEqualTo(sessionFormation2);
    }
}
