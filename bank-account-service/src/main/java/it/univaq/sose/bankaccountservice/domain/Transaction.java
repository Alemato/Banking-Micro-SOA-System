package it.univaq.sose.bankaccountservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4023048747929019500L;

    @Column(name = "transaction_code", nullable = false, unique = true)
    private String transactionCode;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private BankAccount senderBankAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private BankAccount receiverBankAccount;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Transaction that = (Transaction) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}