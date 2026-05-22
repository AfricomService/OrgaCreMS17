package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatusHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusHistory.class);
        StatusHistory statusHistory1 = new StatusHistory();
        statusHistory1.setId(1L);
        StatusHistory statusHistory2 = new StatusHistory();
        statusHistory2.setId(statusHistory1.getId());
        assertThat(statusHistory1).isEqualTo(statusHistory2);
        statusHistory2.setId(2L);
        assertThat(statusHistory1).isNotEqualTo(statusHistory2);
        statusHistory1.setId(null);
        assertThat(statusHistory1).isNotEqualTo(statusHistory2);
    }
}
