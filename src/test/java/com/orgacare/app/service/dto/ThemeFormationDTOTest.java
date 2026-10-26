package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeFormationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeFormationDTO.class);
        ThemeFormationDTO themeFormationDTO1 = new ThemeFormationDTO();
        themeFormationDTO1.setId(1L);
        ThemeFormationDTO themeFormationDTO2 = new ThemeFormationDTO();
        assertThat(themeFormationDTO1).isNotEqualTo(themeFormationDTO2);
        themeFormationDTO2.setId(themeFormationDTO1.getId());
        assertThat(themeFormationDTO1).isEqualTo(themeFormationDTO2);
        themeFormationDTO2.setId(2L);
        assertThat(themeFormationDTO1).isNotEqualTo(themeFormationDTO2);
        themeFormationDTO1.setId(null);
        assertThat(themeFormationDTO1).isNotEqualTo(themeFormationDTO2);
    }
}
