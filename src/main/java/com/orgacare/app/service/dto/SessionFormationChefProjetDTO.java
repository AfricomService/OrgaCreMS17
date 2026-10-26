package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.SessionFormationChefProjet} entity.
 */
public class SessionFormationChefProjetDTO implements Serializable {

    private Long id;

    private Long sessionId;

    private Long chefProjetId;

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

    public Long getChefProjetId() {
        return chefProjetId;
    }

    public void setChefProjetId(Long chefProjetId) {
        this.chefProjetId = chefProjetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionFormationChefProjetDTO)) {
            return false;
        }

        SessionFormationChefProjetDTO sessionFormationChefProjetDTO = (SessionFormationChefProjetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionFormationChefProjetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionFormationChefProjetDTO{" +
            "id=" + getId() +
            ", sessionId=" + getSessionId() +
            ", chefProjetId=" + getChefProjetId() +
            "}";
    }
}
