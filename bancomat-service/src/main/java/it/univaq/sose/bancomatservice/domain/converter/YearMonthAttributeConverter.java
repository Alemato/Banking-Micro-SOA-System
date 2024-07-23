package it.univaq.sose.bancomatservice.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * JPA Attribute Converter for converting {@link YearMonth} to {@link String} and vice versa.
 * This converter is applied automatically to all attributes of type {@link YearMonth}.
 */
@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Converts a {@link YearMonth} attribute to its {@link String} representation for database storage.
     *
     * @param attribute the {@link YearMonth} attribute
     * @return the {@link String} representation of the {@link YearMonth} attribute
     */
    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        return attribute != null ? attribute.format(FORMATTER) : null;
    }

    /**
     * Converts a {@link String} representation from the database to a {@link YearMonth} attribute.
     *
     * @param dbData the {@link String} representation of the {@link YearMonth} attribute
     * @return the {@link YearMonth} attribute
     */
    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        return dbData != null ? YearMonth.parse(dbData, FORMATTER) : null;
    }
}