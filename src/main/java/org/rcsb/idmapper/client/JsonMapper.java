package org.rcsb.idmapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

public class JsonMapper {
    public ObjectMapper create() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper = objectMapper.registerModule(new GuavaModule());

        return objectMapper;
    }
}
