package ua.com.foxminded.university.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.converter.TimetableConverter;

@Configuration
public class ServiceConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Converter<TimetableEntity, TimetableModel> converter = new TimetableConverter();
        TypeMap<TimetableEntity, TimetableModel> typeMap = modelMapper.createTypeMap(
                TimetableEntity.class, TimetableModel.class);
        typeMap.setPostConverter(converter);
        return modelMapper;
    }
}
