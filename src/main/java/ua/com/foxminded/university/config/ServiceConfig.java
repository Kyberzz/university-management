package ua.com.foxminded.university.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ua.com.foxminded.university.converter.TimetableConverter;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;

@Configuration
@PropertySource("classpath:timetable-converter-config.properties")
@EnableConfigurationProperties(TimetableConverterConfig.class)
public class ServiceConfig {
    
    @Bean
    public ModelMapper modelMapper(TimetableConverterConfig config) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Converter<TimetableEntity, TimetableModel> converter = new TimetableConverter(config);
        TypeMap<TimetableEntity, TimetableModel> typeMap = modelMapper.createTypeMap(
                TimetableEntity.class, TimetableModel.class);
        typeMap.setPostConverter(converter);
        return modelMapper;
    }
}
