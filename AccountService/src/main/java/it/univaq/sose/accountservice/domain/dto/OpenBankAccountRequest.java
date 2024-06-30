package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XmlRootElement(name = "OpenBankAccountRequest")
public class OpenBankAccountRequest {
    private String name;

    private String surname;

    private String username;

    private String password;

    public OpenBankAccountRequest(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }
}
