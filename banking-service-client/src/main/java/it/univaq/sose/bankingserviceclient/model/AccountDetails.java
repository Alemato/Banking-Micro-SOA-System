package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDetails {

    private Long id;

    private String name;

    private String surname;

    private String username;

    private String role;

    private String email;

    private String phone;

    private BankAccount bankAccount;

    private Bancomat bancomat;

    private List<Loan> loans = new ArrayList<>();

    public AccountDetails() {
    }

    public AccountDetails(Long id, String name, String surname, String username, String role, String email, String phone, BankAccount bankAccount, Bancomat bancomat, List<Loan> loans) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.bankAccount = bankAccount;
        this.bancomat = bancomat;
        this.loans = loans;
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        this.loans.remove(loan);
    }
}
