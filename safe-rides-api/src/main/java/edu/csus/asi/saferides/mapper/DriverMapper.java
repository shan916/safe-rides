package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.security.model.User;
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
                .fieldBToA("oneCardId", "oneCardId")
                .fieldAToB("user.username", "oneCardId")
                .fieldAToB("user.firstName", "driverFirstName")
                .fieldAToB("user.lastName", "driverLastName")
                .field("phoneNumber", "phoneNumber")
                .field("dlChecked", "dlChecked")
                .field("insuranceChecked", "insuranceChecked")
                .field("insuranceCompany", "insuranceCompany")
                .fieldAToB("user.active", "active")
                .fieldAToB("status", "status")
                .field("vehicle", "vehicle")
                .fieldAToB("endOfNightOdo", "endOfNightOdo")
                .byDefault()
                .register();

        factory.classMap(DriverDto.class, User.class)
                .field("driverFirstName", "firstName")
                .field("driverLastName", "lastName")
                .field("active", "active")
                .byDefault()
                .register();
    }
}
