package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.dto.DriverDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Driver.class, DriverDto.class)
                .field("id", "id")
                .field("oneCardId", "oneCardId")
                .field("driverFirstName", "driverFirstName")
                .field("driverLastName", "driverLastName")
                .field("phoneNumber", "phoneNumber")
                .field("dlChecked", "dlChecked")
                .field("insuranceChecked", "insuranceChecked")
                .field("insuranceCompany", "insuranceCompany")
                .field("active", "active")
                .field("status", "status")
                .field("vehicle", "vehicle")
                .field("endOfNightOdo", "endOfNightOdo")
                .byDefault()
                .register();
    }
}
