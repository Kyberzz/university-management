package ua.com.foxminded.university.converter;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return Duration.ofMinutes(dbData);
    }
}
