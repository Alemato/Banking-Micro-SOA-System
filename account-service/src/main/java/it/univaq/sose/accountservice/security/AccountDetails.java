package it.univaq.sose.accountservice.security;

import it.univaq.sose.accountservice.domain.Role;

/**
 * A record class representing the details of an account.
 *
 * @param username the username of the account
 * @param role     the role of the account
 */
public record AccountDetails(String username,
                             Role role) {
}
