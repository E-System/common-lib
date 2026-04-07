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

package com.eslibs.common;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Date;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Jsons {

    private static final JsonMapper OBJECT_MAPPER = mapper();

    public static JsonMapper mapper() {
        return JsonMapper.builder()
            .findAndAddModules()
            .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DateTimeFeature.WRITE_DATES_WITH_ZONE_ID)
            .build();
    }

    public static <T> T fromJson(String json, Class<T> classOfT, JsonMapper objectMapper) {
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return fromJson(json, classOfT, OBJECT_MAPPER);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJson(json, typeReference, OBJECT_MAPPER);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference, JsonMapper objectMapper) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object object) {
        return toJson(object, OBJECT_MAPPER);
    }

    public static String toJson(Object object, JsonMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T clone(T source, Class<T> classOfT) {
        return fromJson(toJson(source), classOfT);
    }

    public static <T> T clone(T source, Class<T> classOfT, JsonMapper objectMapper) {
        return fromJson(toJson(source, objectMapper), classOfT, objectMapper);
    }

    public static <T> T clone(T source, TypeReference<T> typeReference) {
        return fromJson(toJson(source), typeReference);
    }

    public static <T> T clone(T source, TypeReference<T> typeReference, JsonMapper objectMapper) {
        return fromJson(toJson(source, objectMapper), typeReference, objectMapper);
    }

    public static class UnixTimeDeserializer extends ValueDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
            return new Date(Long.parseLong(parser.getValueAsString()) * 1000);
        }
    }

    public static class UnixTimeSerializer extends ValueSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator generator, SerializationContext serializers) throws JacksonException {
            generator.writeString(String.valueOf(value.getTime() / 1000));
        }

    }
}
