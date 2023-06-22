package ua.com.foxminded.university.converter;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;

@RequiredArgsConstructor
public class TimingConverter extends AbstractConverter<Timing, TimingDTO> {
    
    @Override
    protected TimingDTO convert(Timing source) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimingDTO timingDto = modelMapper.map(source, TimingDTO.class);
        timingDto.setEndTime(source.getStartTime().plus(source.getLessonDuration()));
        return timingDto;
    }
}
