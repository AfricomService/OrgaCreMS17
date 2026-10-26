package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.SessionFormationPersonne} entity.
 */
public class SessionFormationPersonneDTO implements Serializable {

    private Long id;

    private Long sessionId;

    private Long personneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Long personneId) {
        this.personneId = personneId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormationPersonneDTO)) {
            return false;
        }

        SessionFormationPersonneDTO sessionFormationPersonneDTO = (SessionFormationPersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionFormationPersonneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormationPersonneDTO{" +
            "id=" + getId() +
            ", sessionId=" + getSessionId() +
            ", personneId=" + getPersonneId() +
            "}";
    }
}
