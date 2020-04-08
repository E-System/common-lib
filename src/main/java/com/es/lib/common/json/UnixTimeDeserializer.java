package com.es.lib.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 05.04.20
 */
public class UnixTimeDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Date(Long.parseLong(p.getValueAsString()) * 1000);
    }

}
