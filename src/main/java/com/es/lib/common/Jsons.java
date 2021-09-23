/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Date;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class Jsons {

    private static final ObjectMapper OBJECT_MAPPER = mapper();

    private Jsons() {}

    public static ObjectMapper mapper() {
        return new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
    }

    public static <T> T fromJson(String json, Class<T> classOfT, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return fromJson(json, classOfT, OBJECT_MAPPER);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJson(json, typeReference, OBJECT_MAPPER);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object object) {
        return toJson(object, OBJECT_MAPPER);
    }

    public static String toJson(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class UnixTimeDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return new Date(Long.parseLong(parser.getValueAsString()) * 1000);
        }
    }

    public static class UnixTimeSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeString(String.valueOf(value.getTime() / 1000));
        }

    }
}
