package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeFormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeFormation.class);
        ThemeFormation themeFormation1 = new ThemeFormation();
        themeFormation1.setId(1L);
        ThemeFormation themeFormation2 = new ThemeFormation();
        themeFormation2.setId(themeFormation1.getId());
        assertThat(themeFormation1).isEqualTo(themeFormation2);
        themeFormation2.setId(2L);
        assertThat(themeFormation1).isNotEqualTo(themeFormation2);
        themeFormation1.setId(null);
        assertThat(themeFormation1).isNotEqualTo(themeFormation2);
    }
}
