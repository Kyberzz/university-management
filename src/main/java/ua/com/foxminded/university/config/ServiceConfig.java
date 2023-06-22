package ua.com.foxminded.university.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.com.foxminded.university.converter.TimingConverter;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;

@Configuration
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
