package it.univaq.sose.bankingoperationsserviceprosumer.util;

import it.univaq.sose.bankingoperationsserviceprosumer.service.UrlLocationMalformedException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for Banking Operations Service.
 */
@Slf4j
public class BankingOperationsUtils {
    private BankingOperationsUtils() {
        throw new IllegalStateException("Class Utils");
    }

    /**
     * Extracts the ID from a given URL locator.
     *
     * @param url the URL from which to extract the ID.
     * @return the extracted ID.
     * @throws UrlLocationMalformedException if the URL is malformed or the ID cannot be extracted.
     */
    public static long getIdFromUrlLocator(String url) throws UrlLocationMalformedException {
        try {
            // Parse the URL to a URI object
            URI uri = new URI(url);
            // Get the path component of the URI
            String path = uri.getPath();
            String[] segments = path.split("/");
            // Check if the last segment is empty or there are no segments
            if (segments.length == 0 || segments[segments.length - 1].isEmpty()) {
                throw new UrlLocationMalformedException("The URL Location does not contain a valid ID.");
            }
            // Get the ID from the last segment
            String id = segments[segments.length - 1];
            log.info("ID: {}", id);
            return Long.parseLong(id);
        } catch (URISyntaxException | NumberFormatException e) {
            // Log and throw an exception if there is an error in parsing
            log.error("Failed to retrieve id of Account from URL Location: {}", e.getMessage(), e);
            throw new UrlLocationMalformedException(e.getMessage());
        }
    }
}
