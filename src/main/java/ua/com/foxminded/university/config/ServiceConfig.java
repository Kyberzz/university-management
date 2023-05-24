package ua.com.foxminded.university.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ua.com.foxminded.university.converter.TimingConverter;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;

@Configuration
@PropertySource("classpath:timetable-converter-config.properties")
@EnableConfigurationProperties(TimetableConverterConfig.class)
public class ServiceConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(Timing.class, TimingDTO.class);
        modelMapper.getTypeMap(Timing.class, TimingDTO.class)
                   .setConverter(new TimingConverter());
        return modelMapper;
    }
}
