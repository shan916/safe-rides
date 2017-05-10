package edu.csus.asi.saferides.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

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
     * @param arg0 LocalDateTime object to serialize; can not be null
     * @param arg1 JsonGenerator used to output resulting Json content
     * @param arg2 SerializerProvider that can be used to get serializers for serializing Objects value contains, if any
     * @throws IOException IOException
     */
    @Override
    public void serialize(LocalDateTime arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        arg1.writeNumber(arg0.toInstant(ZoneId.of("America/Los_Angeles").getRules().getOffset(arg0)).toEpochMilli());
    }
}
