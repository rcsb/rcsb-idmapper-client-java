package org.rcsb.idmapper.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.Multimap;

import java.io.IOException;

public class JsonMapper {
    public ObjectMapper create() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper = objectMapper.registerModule(new GuavaModule());

        return objectMapper;
    }
}
