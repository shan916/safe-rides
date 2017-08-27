package edu.csus.asi.saferides.serialization;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Customizes the configuration of the Jackson ObjectMapper
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customObjectMapper() {
        return jackson2ObjectMapperBuilder -> {
            jackson2ObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            jackson2ObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

}
