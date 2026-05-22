package com.orgacare.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StatusHistory.
 */
@Entity
@Table(name = "status_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_transaction")
    private ZonedDateTime dateTransaction;

    @Column(name = "date_fin")
    private ZonedDateTime dateFin;

    @Column(name = "login_user")
    private String loginUser;

    @Column(name = "transaction")
    private String transaction;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "data_object")
    private String dataObject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StatusHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTransaction() {
        return this.dateTransaction;
    }

    public StatusHistory dateTransaction(ZonedDateTime dateTransaction) {
        this.setDateTransaction(dateTransaction);
        return this;
    }

    public void setDateTransaction(ZonedDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public StatusHistory dateFin(ZonedDateTime dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLoginUser() {
        return this.loginUser;
    }

    public StatusHistory loginUser(String loginUser) {
        this.setLoginUser(loginUser);
        return this;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getTransaction() {
        return this.transaction;
    }

    public StatusHistory transaction(String transaction) {
        this.setTransaction(transaction);
        return this;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getTransactionReference() {
        return this.transactionReference;
    }

    public StatusHistory transactionReference(String transactionReference) {
        this.setTransactionReference(transactionReference);
        return this;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getDataObject() {
        return this.dataObject;
    }

    public StatusHistory dataObject(String dataObject) {
        this.setDataObject(dataObject);
        return this;
    }

    public void setDataObject(String dataObject) {
        this.dataObject = dataObject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusHistory)) {
            return false;
        }
        return id != null && id.equals(((StatusHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatusHistory{" +
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
