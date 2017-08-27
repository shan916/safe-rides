package edu.csus.asi.saferides;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.csus.asi.saferides.model.views.JsonViews;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonViewTests {

    private static MockObject mockObject;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        mockObject = new MockObject();
        mockObject.setMessageToDriver("Some message to driver");
        mockObject.setRequesterPhoneNumber("9161234567");
    }

    @Test
    public void messageToDriverIsVisibleIfJsonViewIsDriver() throws JsonProcessingException {
        String json = objectMapper
                .writerWithView(JsonViews.Driver.class)
                .writeValueAsString(mockObject);

        assertThat(json.contains(mockObject.getMessageToDriver())).isTrue();

    }

    @Test
    public void messageToDriverIsNotVisibleIfJsonViewerIsRider() throws JsonProcessingException {
        String json = objectMapper
                .writerWithView(JsonViews.Rider.class)
                .writeValueAsString(mockObject);

        assertThat(json.contains(mockObject.getMessageToDriver())).isFalse();
    }

    @Test
    public void requestorPhoneNumberIsVisibleIfJsonViewerIsRider() throws JsonProcessingException {
        String json = objectMapper
                .writerWithView(JsonViews.Rider.class)
                .writeValueAsString(mockObject);

        assertThat(json.contains(mockObject.getRequesterPhoneNumber())).isTrue();
    }

    @Test
    public void requestorPhoneNumberIsNotVisibleIfJsonViewerIsRider() throws JsonProcessingException {
        String json = objectMapper
                .writerWithView(JsonViews.Driver.class)
                .writeValueAsString(mockObject);

        assertThat(json.contains(mockObject.getRequesterPhoneNumber())).isFalse();
    }

}
