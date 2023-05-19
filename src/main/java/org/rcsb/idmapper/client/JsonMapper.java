package org.rcsb.idmapper.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonMapper {
    public Gson create() {
        return new GsonBuilder()
                //.serializeNulls()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(Multimap.class, new MultiMapAdapter())
                .create();
    }

    private static final class MultiMapAdapter implements JsonSerializer<Multimap<String,String>>, JsonDeserializer<Multimap<String,String>> {
        private static final Type asMapReturnType;
        static {
            try {
                asMapReturnType = Multimap.class.getDeclaredMethod("asMap").getGenericReturnType();
            } catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public JsonElement serialize(Multimap<String, String> src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.asMap(), asMapType(typeOfSrc));
        }


        private static Type asMapType(Type multimapType) {
            return TypeToken.of(multimapType).resolveType(asMapReturnType).getType();
        }

        @Override
        public Multimap<String, String> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            Map<String, List<String>> asMap = context.deserialize(jsonElement, asMapType(type));
            Multimap<String, String> multimap = ArrayListMultimap.create();
            for (var entry : asMap.entrySet()) {
                multimap.putAll(entry.getKey(), entry.getValue());
            }
            return multimap;
        }
    }
}
