package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.DriverLocation;
import edu.csus.asi.saferides.model.dto.DriverLocationDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Maps a DriverLocation object to a DriverLocationDto object and vice-versa
 */
@Component
public class DriverLocationMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(DriverLocation.class, DriverLocationDto.class)
                .field("latitude", "latitude")
                .field("longitude", "longitude")
                .byDefault()
                .register();
    }
}