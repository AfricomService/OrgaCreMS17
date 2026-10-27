package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salaire.class);
        Salaire salaire1 = new Salaire();
        salaire1.setId(1L);
        Salaire salaire2 = new Salaire();
        salaire2.setId(salaire1.getId());
        assertThat(salaire1).isEqualTo(salaire2);
        salaire2.setId(2L);
        assertThat(salaire1).isNotEqualTo(salaire2);
        salaire1.setId(null);
        assertThat(salaire1).isNotEqualTo(salaire2);
    }
}
