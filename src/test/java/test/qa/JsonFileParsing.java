package test.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.qa.data_model.UserDevices;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFileParsing {

    private final ClassLoader cl = JsonFileParsing.class.getClassLoader();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Проверка json файла")
    void jsonFileParsingTest() throws Exception {

        try (InputStream is = cl.getResourceAsStream("mobile_devices.json")) {

            UserDevices userDevices = mapper.readValue(is, UserDevices.class);
            assertEquals("Masha", userDevices.getName());
            assertEquals(5, userDevices.getTotalDevices());

            UserDevices.Device androidDevice = userDevices.getDevices().get(0);
            assertEquals("Android", androidDevice.getPlatform());
            assertArrayEquals(new String[]{"Google Pixel 8", "Samsung Galaxy S23"}, androidDevice.getModels().toArray());

            UserDevices.Device iosDevice = userDevices.getDevices().get(1);
            assertEquals("iOS", iosDevice.getPlatform());
            assertArrayEquals(new String[]{"iPhone 15 Pro", "iPhone XS", "iPhone 13"}, iosDevice.getModels().toArray());
        }
    }
}

