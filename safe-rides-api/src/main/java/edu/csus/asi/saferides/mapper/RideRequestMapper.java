package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by shan on 4/2/17.
 */
@Component
public class RideRequestMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(RideRequest.class, RideRequestDto.class)
                .field("requestorFirstName", "requestorFirstName")
                .field("requestorLastName", "requestorLastName")
                .field("status", "status")
                .field("estimatedTime", "estimatedTime")
                .field("driver.driverFirstName", "driverName")
                .field("driver.vehicle.color", "vehicleColor")
                .field("driver.vehicle.year", "vehicleYear")
                .field("driver.vehicle.make", "vehicleMake")
                .field("driver.vehicle.model", "vehicleModel")
                .field("driver.vehicle.licensePlate", "vehicleLicensePlate")
                .byDefault()
                .register();
    }

}
