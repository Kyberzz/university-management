package ua.com.foxminded.university.config;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ua.com.foxminded.university.converter.TimingConverter;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.repository.TimingRepository;


@Configuration
@PropertySource("classpath:timetable-converter-config.properties")
@EnableConfigurationProperties(TimetableConverterConfig.class)
public class ServiceConfig {
    
    @Bean
    public ModelMapper modelMapper(TimingRepository timingRepository) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimingConverter timingConverter = new TimingConverter();
        TypeMap<Timing, TimingDTO> timingMap = modelMapper.createTypeMap(Timing.class, TimingDTO.class);
        timingMap.setConverter(timingConverter);
//        modelMapper.getTypeMap(Timing.class, TimingDTO.class)
//                   .setConverter(timingConverter);
//        Provider<Timing> timingProvider = request -> new Timing();
//        modelMapper.typeMap(Timing.class, TimingDTO.class).addMappings(mapper -> mapper.with(timingProvider)
//                   .map(src -> src.getStartTime().plus(src.getLessonDuration()), TimingDTO::setEndTime));
        
//        Converter<TimingEntity, TimingModel> timingConverter = ctx -> ctx.getSource().getStartTime().plus(ctx.getSource().getLessonDuration());
//        modelMapper.typeMap(TimingEntity.class, TimingModel.class)
//                   .addMappings(mapper -> mapper.using(ctx -> ctx.getSource().));
        
        
//        TypeMap<Timing, TimingDTO> timingEntityMap = modelMapper.createTypeMap(
//                Timing.class, TimingDTO.class);
//        timingEntityMap.addMapping(src -> src.getStartTime(), 
//                                   TimingDTO::setEndTime);
        return modelMapper;
    }
}
