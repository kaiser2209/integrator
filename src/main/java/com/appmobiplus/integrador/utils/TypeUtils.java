package com.appmobiplus.integrador.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class TypeUtils {

    public static Object getValue(JsonNode jsonNode) {
        if (jsonNode.isDouble() || jsonNode.isFloat()) {
            return jsonNode.floatValue();
        } else if (jsonNode.isTextual()) {
            return jsonNode.textValue();
        } else if (jsonNode.isInt()) {
            return jsonNode.intValue();
        } else if (jsonNode.isLong()) {
            return jsonNode.longValue();
        }

        return null;
    }
}
