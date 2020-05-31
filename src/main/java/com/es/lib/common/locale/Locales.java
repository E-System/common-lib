package com.es.lib.common.locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.01.2018
 */
@Slf4j
public final class Locales {

    private Locales() { }

    public static Locale from(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        try {
            return LocaleUtils.toLocale(code);
        } catch (Exception e) {
            log.error("Invalid locale: {}", code);
        }
        return null;
    }
}
