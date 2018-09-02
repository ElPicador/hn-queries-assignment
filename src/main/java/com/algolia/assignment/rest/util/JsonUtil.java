package com.algolia.assignment.rest.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class JsonUtil {
    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("IOException while mapping object (" + data + ") to JSON.", e);
        }
    }
}