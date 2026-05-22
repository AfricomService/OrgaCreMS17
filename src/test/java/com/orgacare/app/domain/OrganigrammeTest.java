package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganigrammeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organigramme.class);
        Organigramme organigramme1 = new Organigramme();
        organigramme1.setId(1L);
        Organigramme organigramme2 = new Organigramme();
        organigramme2.setId(organigramme1.getId());
        assertThat(organigramme1).isEqualTo(organigramme2);
        organigramme2.setId(2L);
        assertThat(organigramme1).isNotEqualTo(organigramme2);
        organigramme1.setId(null);
        assertThat(organigramme1).isNotEqualTo(organigramme2);
    }
}
