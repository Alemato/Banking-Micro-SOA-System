package it.univaq.sose.bankingserviceclient.model;

import lombok.Data;

@Data
public class Bancomat {
    private Long id;

    private String number;

    private String cvv;

    private String dataScadenza;

    public Bancomat(Long id, String number, String cvv, String dataScadenza) {
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.dataScadenza = dataScadenza;
    }
}
