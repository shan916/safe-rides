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
     * @param jsonParser JsonParser used for reading JSON content
     * @param context    DeserializationContext that can be used to access information about this deserialization activity
     * @return Deserialized value
     * @throws IOException IOException
     */
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        return Instant.ofEpochMilli(jsonParser.getLongValue()).atZone(ZoneId.of(Util.APPLICATION_TIME_ZONE)).toLocalDateTime();
    }
}
