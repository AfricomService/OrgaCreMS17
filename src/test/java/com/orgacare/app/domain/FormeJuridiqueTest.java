package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormeJuridiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormeJuridique.class);
        FormeJuridique formeJuridique1 = new FormeJuridique();
        formeJuridique1.setId(1L);
        FormeJuridique formeJuridique2 = new FormeJuridique();
        formeJuridique2.setId(formeJuridique1.getId());
        assertThat(formeJuridique1).isEqualTo(formeJuridique2);
        formeJuridique2.setId(2L);
        assertThat(formeJuridique1).isNotEqualTo(formeJuridique2);
        formeJuridique1.setId(null);
        assertThat(formeJuridique1).isNotEqualTo(formeJuridique2);
    }
}
