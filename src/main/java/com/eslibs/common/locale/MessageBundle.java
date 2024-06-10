/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2019
 */

package com.eslibs.common.locale;

import org.apache.commons.lang3.LocaleUtils;

import java.util.ResourceBundle;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 16.07.15
 */
public class MessageBundle {

    private final String path;

    public MessageBundle(Class<?> clazz, String name) {this(clazz.getPackage().getName().replaceAll("\\.", "/") + "/" + (name + ".properties"));}

    public MessageBundle(String path) {
        this.path = path.replaceAll("(.*)\\.properties", "$1");
    }

    public String get(byte code) {return getLocalized(String.valueOf(code & 0xFF), null, null);}

    public String get(int code) {return getLocalized(String.valueOf(code), null, null);}

    public String get(String code) {return getLocalized(code, null, null);}

    public String get(byte code, String defaultValue) {return getLocalized(String.valueOf(code & 0xFF), defaultValue, null);}

    public String get(int code, String defaultValue) {return getLocalized(String.valueOf(code), defaultValue, null);}

    public String get(String code, String defaultValue) {return getLocalized(code, defaultValue, null);}

    public String getLocalized(byte code, String locale) {return getLocalized(String.valueOf(code & 0xFF), null, locale);}

    public String getLocalized(int code, String locale) {return getLocalized(String.valueOf(code), null, locale);}

    public String getLocalized(String code, String locale) {return getLocalized(code, null, locale);}

    public String getLocalized(byte code, String defaultValue, String locale) {return getLocalized(String.valueOf(code & 0xFF), defaultValue, locale);}

    public String getLocalized(int code, String defaultValue, String locale) {return getLocalized(String.valueOf(code), defaultValue, locale);}

    public String getLocalized(String key, String defaultValue, String locale) {
        if (key == null) {
            key = "null";
        }
        if (locale == null) {
            locale = "";
        }
        try {
            return ResourceBundle.getBundle(path, LocaleUtils.toLocale(locale), new UTF8Control()).getString(key);
        } catch (Exception _) {
            return defaultValue;
        }
    }
}
