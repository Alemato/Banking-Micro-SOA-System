package it.univaq.sose.bancomatservice.domain;

import it.univaq.sose.bancomatservice.domain.converter.YearMonthAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.time.YearMonth;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bancomat")
public class Bancomat extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7653460776778203136L;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "expiry_date", nullable = false)
    private YearMonth expiryDate;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @OneToMany(mappedBy = "bancomat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new LinkedHashSet<>();

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setBancomat(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Bancomat that = (Bancomat) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}