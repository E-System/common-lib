/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2019
 */

package com.es.lib.common.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 16.07.15
 */
public class MessageBundle {

    private static final Logger LOG = LoggerFactory.getLogger(MessageBundle.class);

    private Properties properties;

    public MessageBundle(Class clazz, String name) { this(clazz.getResourceAsStream(name + ".properties")); }

    public MessageBundle(String path) { this(ClassLoader.getSystemClassLoader().getResourceAsStream(path)); }

    private MessageBundle(InputStream is) {
        properties = new Properties();
        try (InputStreamReader isr = new InputStreamReader(is, "UTF-8")) {
            properties.load(isr);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String get(byte code) { return get(String.valueOf(code & 0xff)); }

    public String get(int code) {
        return get(String.valueOf(code));
    }

    public String get(String code) { return properties.getProperty(code == null ? "null" : code); }

    public String get(byte code, String defaultValue) { return get(String.valueOf(code & 0xff), defaultValue); }

    public String get(int code, String defaultValue) {
        return get(String.valueOf(code), defaultValue);
    }

    public String get(String code, String defaultValue) { return properties.getProperty(code == null ? "null" : code, defaultValue); }

}
