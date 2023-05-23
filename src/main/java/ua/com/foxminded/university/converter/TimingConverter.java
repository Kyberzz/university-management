package ua.com.foxminded.university.converter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;

public class TimingConverter implements Converter<Timing, TimingDTO> {

    @Override
    public TimingDTO convert(MappingContext<Timing, TimingDTO> context) {
        Timing timing = context.getSource();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimingDTO timingDto = modelMapper.map(timing, TimingDTO.class);
        timingDto.setEndTime(timingDto.getStartTime().plus(timingDto.getLessonDuration()));
        return timingDto;
    }
}
