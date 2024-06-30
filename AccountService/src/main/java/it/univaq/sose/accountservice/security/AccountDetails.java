package it.univaq.sose.accountservice.security;

import it.univaq.sose.accountservice.domain.Role;

public record AccountDetails(String username,
                             Role role) {
}
