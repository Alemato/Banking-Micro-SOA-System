package it.univaq.sose.bancomatservice.domain.dto.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * XML Adapter for converting between {@link LocalDateTime} and {@link String}.
 * This adapter is used to marshal and unmarshal {@link LocalDateTime} objects
 * to and from their ISO-8601 string representation.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts a string representation of {@link LocalDateTime} to a {@link LocalDateTime} object.
     *
     * @param v the string representation of the {@link LocalDateTime}
     * @return the {@link LocalDateTime} object
     * @throws RuntimeException if the string cannot be parsed
     */
    @Override
    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v, DATE_FORMATTER);
    }

    /**
     * Converts a {@link LocalDateTime} object to its string representation.
     *
     * @param v the {@link LocalDateTime} object
     * @return the string representation of the {@link LocalDateTime}
     * @throws RuntimeException if the {@link LocalDateTime} cannot be formatted
     */
    @Override
    public String marshal(LocalDateTime v) {
        return v != null ? DATE_FORMATTER.format(v) : null;
    }
}
