package edu.csus.asi.saferides.mapper;

import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.model.dto.UserDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Maps a User object to a UserDto object and vice-versa
 */
@Component
public class UserMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(UserDto.class, User.class)
                .field("id", "id")
                .field("username", "username")
                .field("firstName", "firstName")
                .field("lastName", "lastName")
                .field("active", "active")
                .byDefault()
                .register();
    }

}
