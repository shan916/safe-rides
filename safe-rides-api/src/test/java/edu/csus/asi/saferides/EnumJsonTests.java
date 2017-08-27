package edu.csus.asi.saferides;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.csus.asi.saferides.model.DriverStatus;
import edu.csus.asi.saferides.model.RideRequestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnumJsonTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void isSuccessWhenRideRequestStatusDeserializedWithValidValue() throws IOException {
        String json = "{\"rideRequestStatus\": \"UNASSIGNED\"}";
        MockObject mockObject = objectMapper
                .readerFor(MockObject.class)
                .readValue(json);

        assertThat(mockObject.getRideRequestStatus()).isNotNull();
        assertThat(mockObject.getRideRequestStatus()).isEqualTo(RideRequestStatus.UNASSIGNED);
    }

    @Test
    public void isNullWhenRideRequestStatusDeserializedWithInvalidValue() throws IOException {
        String json = "{\"rideRequestStatus\": \"INVALIDSTATUS\"}";
        MockObject mockObject = objectMapper
                .readerFor(MockObject.class)
                .readValue(json);

        assertThat(mockObject.getRideRequestStatus()).isNull();
    }

    @Test
    public void isSuccessWhenDriverStatusDeserializedWithValidValue() throws IOException {
        String json = "{\"driverStatus\": \"AVAILABLE\"}";
        MockObject mockObject = objectMapper
                .readerFor(MockObject.class)
                .readValue(json);

        assertThat(mockObject.getDriverStatus()).isNotNull();
        assertThat(mockObject.getDriverStatus()).isEqualTo(DriverStatus.AVAILABLE);
    }

    @Test
    public void isNullWhenDriverStatusDeserializedWithInvalidValue() throws IOException {
        String json = "{\"driverStatus\": \"INVALIDSTATUS\"}";
        MockObject mockObject = objectMapper
                .readerFor(MockObject.class)
                .readValue(json);

        assertThat(mockObject.getDriverStatus()).isNull();
    }

}
