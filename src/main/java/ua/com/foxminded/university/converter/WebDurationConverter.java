package ua.com.foxminded.university.converter;

import java.time.Duration;

import org.springframework.core.convert.converter.Converter;

public class WebDurationConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(String source) {
        return Duration.ofMinutes(Long.valueOf(source));
    }
}
