package com.lavkatech.townofgames.misc;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
    Is required to parse LocalDateTime into json using Gson library
 */
public class LocaleDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Value("${time.format}")
    private String dateTimeFormat;

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if(localDateTime == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(
                localDateTime.format(
                        DateTimeFormatter.ofPattern(dateTimeFormat)
                )
        );
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        String dateTimeString = jsonReader.nextString();
        return LocalDateTime.parse(
                dateTimeString,
                DateTimeFormatter.ofPattern(dateTimeFormat)
        );
    }
}
