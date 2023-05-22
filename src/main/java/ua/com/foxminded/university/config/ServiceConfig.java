package ua.com.foxminded.university.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ua.com.foxminded.university.repository.TimingRepository;


@Configuration
@PropertySource("classpath:timetable-converter-config.properties")
@EnableConfigurationProperties(TimetableConverterConfig.class)
public class ServiceConfig {
    
    @Bean
    public ModelMapper modelMapper(TimingRepository timingRepository) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
