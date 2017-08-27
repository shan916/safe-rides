package edu.csus.asi.saferides.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.csus.asi.saferides.utility.Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * JSON serializer for LocalDateTime
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    /**
     * serialization implementation
     *
     * @param localDateTime      LocalDateTime object to serialize; can not be null
     * @param jsonGenerator      JsonGenerator used to output resulting Json content
     * @param serializerProvider SerializerProvider that can be used to get serializers for serializing Objects value contains, if any
     * @throws IOException IOException
     */
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(localDateTime.toInstant(ZoneId.of(Util.APPLICATION_TIME_ZONE).getRules().getOffset(localDateTime)).toEpochMilli());
    }
}
