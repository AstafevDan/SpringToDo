package com.emobile.springtodo.config.deserializer;

import com.emobile.springtodo.model.enums.TaskStatus;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Класс, отвечающий за десериализацию {@link TaskStatus}.
 */
public class TaskStatusDeserializer extends JsonDeserializer<Enum<?>> {
    @Override
    public Enum<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return TaskStatus.valueOf(jsonParser.getValueAsString());
    }
}
