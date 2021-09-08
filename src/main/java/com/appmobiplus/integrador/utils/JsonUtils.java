package com.appmobiplus.integrador.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static Map<String, String> getErrorStatus() {
        return null;
    }

    public static JsonNode getJsonObject(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json);
    }

    public static List<String> getJsonFields(JsonNode json) {
        List<String> fields = new ArrayList<>();
        for (Iterator<String> it = json.fieldNames(); it.hasNext(); ) {
            String f = it.next();
            fields.add(f);
        }

        return fields;
    }

    public static List<String> getJsonFieldsInArray(JsonNode json) {
        List<String> fields = new ArrayList<>();
        if (json.isArray()) {
            fields = getJsonFields(json.get(0));
            return fields;
        } else {
            List<String> listFields = getJsonFields(json);
            for(String f : listFields) {
                JsonNode j = json.get(f);
                fields = getJsonFieldsInArray(j);
                if (fields.size() > 0)
                    return fields;
            }
        }

        return fields;
    }
}
