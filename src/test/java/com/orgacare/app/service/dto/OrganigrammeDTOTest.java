package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganigrammeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganigrammeDTO.class);
        OrganigrammeDTO organigrammeDTO1 = new OrganigrammeDTO();
        organigrammeDTO1.setId(1L);
        OrganigrammeDTO organigrammeDTO2 = new OrganigrammeDTO();
        assertThat(organigrammeDTO1).isNotEqualTo(organigrammeDTO2);
        organigrammeDTO2.setId(organigrammeDTO1.getId());
        assertThat(organigrammeDTO1).isEqualTo(organigrammeDTO2);
        organigrammeDTO2.setId(2L);
        assertThat(organigrammeDTO1).isNotEqualTo(organigrammeDTO2);
        organigrammeDTO1.setId(null);
        assertThat(organigrammeDTO1).isNotEqualTo(organigrammeDTO2);
    }
}
