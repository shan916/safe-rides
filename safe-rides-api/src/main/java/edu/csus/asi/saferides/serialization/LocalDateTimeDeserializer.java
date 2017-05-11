package edu.csus.asi.saferides.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.csus.asi.saferides.utility.Util;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * JSON deserializer for LocalDateTime
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    /**
     * Deserialization implementation
     *
     * @param arg0 JsonParser used for reading JSON content
     * @param arg1 DeserializationContext that can be used to access information about this deserialization activity
     * @return Deserialized value
     * @throws IOException IOException
     */
    @Override
    public LocalDateTime deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
        return Instant.ofEpochMilli(arg0.getLongValue()).atZone(ZoneId.of(Util.APPLICATION_TIME_ZONE)).toLocalDateTime();
    }
}
