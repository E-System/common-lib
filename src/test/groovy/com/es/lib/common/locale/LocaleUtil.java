package com.es.lib.common.locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 25.01.2018
 */
public final class LocaleUtil {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleUtil.class);

    private LocaleUtil() { }

    public static Locale toLocale(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        try {
            return LocaleUtils.toLocale(code);
        } catch (Exception e) {
            LOG.error("Invalid locale: {}", code);
        }
        return null;
    }
}
