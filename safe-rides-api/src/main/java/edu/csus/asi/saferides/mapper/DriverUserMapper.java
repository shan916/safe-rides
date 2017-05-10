package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.dto.DriverCreationDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class DriverUserMapper extends ConfigurableMapper {

	@Override
	protected void configure(MapperFactory factory) {
	    factory.classMap(Driver.class, DriverCreationDto.class)
				.field("id", "id")
				.field("csusId","csusId")
				.field("driverFirstName", "driverFirstName")
				.field("driverLastName", "driverLastName")
				.field("phoneNumber", "phoneNumber")
				.field("dlState", "dlState")
				.field("dlNumber", "dlNumber")
				.field("insuranceChecked", "insuranceChecked")
				.field("insuranceCompany", "insuranceCompany")
				.field("active", "active")
				.byDefault()
				.register();
	    }
}
