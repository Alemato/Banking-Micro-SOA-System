package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "UserCredentials")
public class UserCredentials {
    private String username;
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
