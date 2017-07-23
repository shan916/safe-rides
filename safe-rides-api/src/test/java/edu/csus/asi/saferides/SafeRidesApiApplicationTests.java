package edu.csus.asi.saferides;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SafeRidesApiApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void isNotNullWhenRideRequestStatusDeserializedWithValidValue() throws IOException {
        String json = "{\"rideRequestStatus\": \"UNASSIGNED\"}";

        MockObject mockObject = new ObjectMapper()
                .readerFor(MockObject.class)
                .readValue(json);
        assertThat(mockObject.getRideRequestStatus()).isNotNull();
        assertThat(mockObject.getRideRequestStatus().equals("UNASSIGNED"));
    }

    @Test
    public void isNullWhenRideRequestStatusDeserializedWithInvalidValue() throws IOException {
        String json = "{\"rideRequestStatus\": \"INVALIDSTATUS\"}";

        MockObject mockObject = new ObjectMapper()
                .readerFor(MockObject.class)
                .readValue(json);
        assertThat(mockObject.getRideRequestStatus()).isNull();
    }

}
