package it.univaq.sose.accountservice.domain.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;


@XmlRootElement(name = "ErrorResponse")
public class ErrorResponse {
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ErrorResponse that)) return false;

        return Objects.equals(getError(), that.getError());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getError());
    }
}
