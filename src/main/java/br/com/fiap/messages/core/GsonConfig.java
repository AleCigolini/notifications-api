package br.com.fiap.messages.core;

import com.google.gson.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class GsonConfig {
    @Produces
    @ApplicationScoped
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
                    @Override
                    public JsonElement serialize(OffsetDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    }
                })
                .registerTypeAdapter(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
                    @Override
                    public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return OffsetDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                })
                .create();
    }
}
