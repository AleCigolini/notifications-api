package br.com.fiap.messages.core;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class GsonConfigTest {

    private GsonConfig gsonConfig;
    private Gson gson;

    @BeforeEach
    void setUp() {
        gsonConfig = new GsonConfig();
        gson = gsonConfig.gson();
    }

    @Test
    void shouldCreateGsonInstance() {
        assertNotNull(gson);
    }

    @Test
    void shouldSerializeOffsetDateTime() {
        OffsetDateTime dateTime = OffsetDateTime.of(2023, 5, 15, 10, 30, 0, 0, ZoneOffset.UTC);
        TestObject testObject = new TestObject(dateTime);

        String json = gson.toJson(testObject);
        assertTrue(json.contains("\"2023-05-15T10:30:00Z\""));
    }

    @Test
    void shouldDeserializeOffsetDateTime() {
        String json = "{\"dateTime\":\"2023-05-15T10:30:00Z\"}";

        TestObject testObject = gson.fromJson(json, TestObject.class);

        assertNotNull(testObject);
        assertNotNull(testObject.getDateTime());
        assertEquals(2023, testObject.getDateTime().getYear());
        assertEquals(5, testObject.getDateTime().getMonthValue());
        assertEquals(15, testObject.getDateTime().getDayOfMonth());
        assertEquals(10, testObject.getDateTime().getHour());
        assertEquals(30, testObject.getDateTime().getMinute());
    }

    @Test
    void shouldHandleInvalidDateFormat() {
        String json = "{\"dateTime\":\"invalid-date-format\"}";

        assertThrows(RuntimeException.class, () -> gson.fromJson(json, TestObject.class));
    }

    @Test
    void shouldHandleEmptyString() {
        String json = "{\"dateTime\":\"\"}";

        assertThrows(RuntimeException.class, () -> gson.fromJson(json, TestObject.class));
    }

    @Test
    void shouldHandleDifferentFormats() {

        OffsetDateTime dateTime = OffsetDateTime.of(2023, 12, 31, 23, 59, 59, 999_000_000, ZoneOffset.ofHours(2));
        TestObject testObject = new TestObject(dateTime);

        String json = gson.toJson(testObject);
        TestObject deserializedObject = gson.fromJson(json, TestObject.class);

        assertEquals(dateTime, deserializedObject.getDateTime());
    }

    static class TestObject {
        private OffsetDateTime dateTime;

        public TestObject() {
        }

        public TestObject(OffsetDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public OffsetDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(OffsetDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}
