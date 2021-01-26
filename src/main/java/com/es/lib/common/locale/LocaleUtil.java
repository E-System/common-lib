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
public final class LocaleUtil {

    private LocaleUtil() { }

    public static Locale toLocale(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        try {
            Locale result = LocaleUtils.toLocale(code);
            if ("th".equals(result.getLanguage()) && "TH".equals(result.getCountry())) {
                result = new Locale.Builder().setLocale(result).setUnicodeLocaleKeyword("ca", "gregory").build();
            }
            return result;
        } catch (Exception e) {
            log.error("Invalid locale: {}", code);
        }
        return null;
    }
}
