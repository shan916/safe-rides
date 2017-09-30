package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.dto.ConfigurationDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Maps a Configuration object to a ConfigurationDto object and vice-versa
 */
@Component
public class ConfigurationMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Configuration.class, ConfigurationDto.class)
                .field("startTime", "startTime")
                .field("endTime", "endTime")
                .field("daysOfWeek", "daysOfWeek")
                .field("active", "active")
                .field("message", "message")
                .byDefault()
                .register();
    }
}

