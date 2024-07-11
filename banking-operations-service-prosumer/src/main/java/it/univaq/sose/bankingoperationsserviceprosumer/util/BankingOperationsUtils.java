package it.univaq.sose.bankingoperationsserviceprosumer.util;

import it.univaq.sose.bankingoperationsserviceprosumer.service.UrlLocationMalformedException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class BankingOperationsUtils {
    private BankingOperationsUtils() {
        throw new IllegalStateException("Class Utils");
    }

    public static long getIdFromUrlLocator(String url) throws UrlLocationMalformedException {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] segments = path.split("/");
            if (segments.length == 0 || segments[segments.length - 1].isEmpty()) {
                throw new UrlLocationMalformedException("The URL Location does not contain a valid ID.");
            }
            String id = segments[segments.length - 1];
            log.info("ID: {}", id);
            return Long.parseLong(id);
        } catch (URISyntaxException | NumberFormatException e) {
            log.error("Failed to retrieve id of Account from URL Location: {}", e.getMessage(), e);
            throw new UrlLocationMalformedException(e.getMessage());
        }
    }
}
