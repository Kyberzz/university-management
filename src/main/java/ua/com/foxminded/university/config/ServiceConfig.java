package ua.com.foxminded.university.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.com.foxminded.university.cache.PropertiesCache;
import ua.com.foxminded.university.cache.PropertiesCacheImpl;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.converter.TimetableConverter;

@Configuration
public class ServiceConfig {
    public static final String DEFAULT_LESSON_PERIOD_FILENAME = "default-lesson-period.properties";
    public static final String FRIDAY_LESSON_PERIOD_FILENAME = "friday-lesson-period.properties";
    
    @Bean 
    public PropertiesCache fridayLessonsPeriodCache() {
        return new PropertiesCacheImpl(FRIDAY_LESSON_PERIOD_FILENAME); 
    }
    
    @Bean
    public PropertiesCache defaultLessonsPeriodCache() {
        return new PropertiesCacheImpl(DEFAULT_LESSON_PERIOD_FILENAME);
    }
    
    @Bean
    public ModelMapper modelMapper(PropertiesCache fridayLessonsPeriodCache, 
            PropertiesCache defaultLessonsPeriodCache) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Converter<TimetableEntity, TimetableModel> converter = new TimetableConverter(
                fridayLessonsPeriodCache, 
                defaultLessonsPeriodCache);
        TypeMap<TimetableEntity, TimetableModel> typeMap = modelMapper.createTypeMap(
                TimetableEntity.class, TimetableModel.class);
        typeMap.setPreConverter(converter);
        modelMapper.addConverter(converter);
        return modelMapper;
    }
}
