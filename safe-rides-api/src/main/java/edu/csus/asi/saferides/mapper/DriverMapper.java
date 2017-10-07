package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Maps a Driver object to a DriverDto object and vice-versa
 */
@Component
public class DriverMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Driver.class, DriverDto.class)
                .field("id", "id")
                .fieldAToB("user.username", "username")
                .fieldAToB("user.firstName", "driverFirstName")
                .fieldAToB("user.lastName", "driverLastName")
                .field("phoneNumber", "phoneNumber")
                .field("dlChecked", "dlChecked")
                .field("insuranceChecked", "insuranceChecked")
                .field("insuranceCompany", "insuranceCompany")
                .fieldAToB("user.active", "active")
                .field("vehicle", "vehicle")
                .field("endOfNightOdo", "endOfNightOdo")
                .fieldAToB("latestDriverLocation", "location")
                .fieldAToB("latestRideRequest", "currentRideRequest")
                .byDefault()
                .register();

        factory.classMap(DriverDto.class, User.class)
                .field("username", "username")
                .field("driverFirstName", "firstName")
                .field("driverLastName", "lastName")
                .field("active", "active")
                .byDefault()
                .register();

        factory.classMap(RideRequest.class, RideRequestDto.class)
                .fieldAToB("id", "id")
                .fieldAToB("requestDate", "requestDate")
                .fieldAToB("lastModified", "lastModified")
                .fieldAToB("assignedDate", "assignedDate")
                .fieldAToB("oneCardId", "oneCardId")
                .fieldAToB("requestorFirstName", "requestorFirstName")
                .fieldAToB("requestorLastName", "requestorLastName")
                .fieldAToB("requestorPhoneNumber", "requestorPhoneNumber")
                .fieldAToB("numPassengers", "numPassengers")
                .fieldAToB("startOdometer", "startOdometer")
                .fieldAToB("endOdometer", "endOdometer")
                .fieldAToB("pickupLine1", "pickupLine1")
                .fieldAToB("pickupLine2", "pickupLine2")
                .fieldAToB("pickupCity", "pickupCity")
                .fieldAToB("dropoffLine1", "dropoffLine1")
                .fieldAToB("dropoffLine2", "dropoffLine2")
                .fieldAToB("dropoffCity", "dropoffCity")
                .fieldAToB("status", "status")
                .fieldAToB("cancelMessage", "cancelMessage")
                .fieldAToB("messageToDriver", "messageToDriver")
                .fieldAToB("estimatedTime", "estimatedTime")
                .fieldAToB("pickupLatitude", "pickupLatitude")
                .fieldAToB("pickupLongitude", "pickupLongitude")
                .fieldAToB("dropoffLatitude", "dropoffLatitude")
                .fieldAToB("dropoffLongitude", "dropoffLongitude")
                .register();
        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));

        factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));
    }
}
