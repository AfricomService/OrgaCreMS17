package com.orgacare.app.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.orgacare.app.domain.StatusHistory} entity.
 */
public class StatusHistoryDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateTransaction;

    private ZonedDateTime dateFin;

    private String loginUser;

    private String transaction;

    private String transactionReference;

    private String dataObject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(ZonedDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getDataObject() {
        return dataObject;
    }

    public void setDataObject(String dataObject) {
        this.dataObject = dataObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusHistoryDTO)) {
            return false;
        }

        StatusHistoryDTO statusHistoryDTO = (StatusHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, statusHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusHistoryDTO{" +
            "id=" + getId() +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", loginUser='" + getLoginUser() + "'" +
            ", transaction='" + getTransaction() + "'" +
            ", transactionReference='" + getTransactionReference() + "'" +
            ", dataObject='" + getDataObject() + "'" +
            "}";
    }
}
