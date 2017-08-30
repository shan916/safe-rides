package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.dto.DriverDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Maps a Driver object to a DriverDto object and vice-versa
 */
@Component
public class DriverMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Driver.class, DriverDto.class)
                .field("id", "id")
                .field("user.username", "oneCardId")
                .field("user.firstName", "driverFirstName")
                .field("user.lastName", "driverLastName")
                .field("phoneNumber", "phoneNumber")
                .field("dlChecked", "dlChecked")
                .field("insuranceChecked", "insuranceChecked")
                .field("insuranceCompany", "insuranceCompany")
                .field("user.active", "active")
                .field("status", "status")
                .field("vehicle", "vehicle")
                .field("endOfNightOdo", "endOfNightOdo")
                .byDefault()
                .register();
    }
}
