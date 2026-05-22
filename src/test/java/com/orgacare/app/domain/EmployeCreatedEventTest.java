package com.orgacare.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.orgacare.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeCreatedEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeCreatedEvent.class);
        EmployeCreatedEvent employeCreatedEvent1 = new EmployeCreatedEvent();
        employeCreatedEvent1.setId(1L);
        EmployeCreatedEvent employeCreatedEvent2 = new EmployeCreatedEvent();
        employeCreatedEvent2.setId(employeCreatedEvent1.getId());
        assertThat(employeCreatedEvent1).isEqualTo(employeCreatedEvent2);
        employeCreatedEvent2.setId(2L);
        assertThat(employeCreatedEvent1).isNotEqualTo(employeCreatedEvent2);
        employeCreatedEvent1.setId(null);
        assertThat(employeCreatedEvent1).isNotEqualTo(employeCreatedEvent2);
    }
}
