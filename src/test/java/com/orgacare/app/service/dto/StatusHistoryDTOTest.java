package com.orgacare.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusHistoryDTO.class);
        StatusHistoryDTO statusHistoryDTO1 = new StatusHistoryDTO();
        statusHistoryDTO1.setId(1L);
        StatusHistoryDTO statusHistoryDTO2 = new StatusHistoryDTO();
        assertThat(statusHistoryDTO1).isNotEqualTo(statusHistoryDTO2);
        statusHistoryDTO2.setId(statusHistoryDTO1.getId());
        assertThat(statusHistoryDTO1).isEqualTo(statusHistoryDTO2);
        statusHistoryDTO2.setId(2L);
        assertThat(statusHistoryDTO1).isNotEqualTo(statusHistoryDTO2);
        statusHistoryDTO1.setId(null);
        assertThat(statusHistoryDTO1).isNotEqualTo(statusHistoryDTO2);
    }
}
