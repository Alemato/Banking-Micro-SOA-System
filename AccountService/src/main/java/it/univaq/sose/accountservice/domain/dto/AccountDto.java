package it.univaq.sose.accountservice.domain.dto;

import it.univaq.sose.accountservice.domain.Role;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link it.univaq.sose.accountservice.domain.Account}
 */
@XmlRootElement(name = "AccountResponse")
public class AccountDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4592896323731902686L;
    Long id;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    String name;
    String surname;
    String username;
    Role role;
    Long idBankAccount;

    public AccountDto() {
    }

    public AccountDto(Long id, LocalDateTime createDate, LocalDateTime updateDate, String name, String surname, String username, Role role, Long idBankAccount) {
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
        this.idBankAccount = idBankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getIdBankAccount() {
        return idBankAccount;
    }

    public void setIdBankAccount(Long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AccountDto that)) return false;

        return getId().equals(that.getId()) && getCreateDate().equals(that.getCreateDate()) && getUpdateDate().equals(that.getUpdateDate()) && getName().equals(that.getName()) && getSurname().equals(that.getSurname()) && getUsername().equals(that.getUsername()) && getRole() == that.getRole() && Objects.equals(getIdBankAccount(), that.getIdBankAccount());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCreateDate().hashCode();
        result = 31 * result + getUpdateDate().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSurname().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getRole().hashCode();
        result = 31 * result + Objects.hashCode(getIdBankAccount());
        return result;
    }
}