package edu.csus.asi.saferides.security.mapper;

import edu.csus.asi.saferides.security.dto.UserDto;
import edu.csus.asi.saferides.security.model.User;
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
                .fieldMap("password", "password").mapNulls(false).mapNullsInReverse(false).add()
                .field("active", "active")
                .byDefault()
                .register();
    }

}
