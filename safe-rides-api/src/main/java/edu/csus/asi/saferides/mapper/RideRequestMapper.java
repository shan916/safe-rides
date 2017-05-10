package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Maps a RideRequest object to a RideRequestDto object and vice-versa
 */
@Component
public class RideRequestMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(RideRequest.class, RideRequestDto.class)
                .field("status", "status")
                .field("estimatedTime", "estimatedTime")
                .field("lastModified", "lastModified")
                .field("assignedDate", "assignedDate")
                .field("driver.driverFirstName", "driverName")
                .field("driver.vehicle.color", "vehicleColor")
                .field("driver.vehicle.year", "vehicleYear")
                .field("driver.vehicle.make", "vehicleMake")
                .field("driver.vehicle.model", "vehicleModel")
                .field("driver.vehicle.licensePlate", "vehicleLicensePlate")
                .byDefault()
                .register();
        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));
    }

}
