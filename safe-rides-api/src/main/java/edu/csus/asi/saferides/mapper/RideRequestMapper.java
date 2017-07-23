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
                .field("id", "id")
                .field("driver.id", "driverId")
                .field("oneCardId", "oneCardId")
                .field("requestDate", "requestDate")
                .field("lastModified", "lastModified")
                .field("assignedDate", "assignedDate")
                .field("requestorFirstName", "requestorFirstName")
                .field("requestorLastName", "requestorLastName")
                .field("requestorPhoneNumber", "requestorPhoneNumber")
                .field("numPassengers", "numPassengers")
                .field("startOdometer", "startOdometer")
                .field("endOdometer", "endOdometer")
                .field("pickupLine1", "pickupLine1")
                .field("pickupLine2", "pickupLine2")
                .field("pickupCity", "pickupCity")
                .field("dropoffLine1", "dropoffLine1")
                .field("dropoffLine2", "dropoffLine2")
                .field("dropoffCity", "dropoffCity")
                .field("status", "status")
                .field("cancelMessage", "cancelMessage")
                .field("messageToDriver", "messageToDriver")
                .field("estimatedTime", "estimatedTime")
                .field("pickupLatitude", "pickupLatitude")
                .field("pickupLongitude", "pickupLongitude")
                .field("dropoffLatitude", "dropoffLatitude")
                .field("dropoffLongitude", "dropoffLongitude")
                .byDefault()
                .register();
        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));
    }

}
