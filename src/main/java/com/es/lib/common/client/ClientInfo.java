package com.es.lib.common.client;

import com.es.lib.common.collection.CollectionUtil;
import com.es.lib.common.locale.LocaleUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class ClientInfo {

    private static final String APP_PLATFORM_KEY = "es-app-platform";
    private static final String APP_PLATFORM_VERSION_KEY = "es-app-platform-version";
    @Deprecated
    private static final String PLATFORM_VERSION_KEY = "es-platform-version";
    private static final String APP_VERSION_KEY = "es-app-version";
    private static final String APP_TIMEZONE_KEY = "es-app-timezone";
    private static final String APP_LOCALE_KEY = "es-app-locale";
    private static final String APP_KEY = "es-app-key";

    private final Platform platform;
    private final String platformVersion;
    private final String appVersion;
    private final ZoneId appTimezone;
    private final Locale appLocale;
    private final String appKey;

    public static ClientInfo create(Map<String, String> headers) {
        if (CollectionUtil.isEmpty(headers)) {
            return new ClientInfo(Platform.undefined, "", "", ZoneId.systemDefault(), Locale.getDefault(), "");
        }
        return new ClientInfo(
            getPlatform(headers),
            getPlatformVersion(headers),
            StringUtils.defaultIfBlank(headers.get(APP_VERSION_KEY), ""),
            getZoneId(headers),
            getLocale(headers),
            StringUtils.defaultIfBlank(headers.get(APP_KEY), "")
        );
    }

    private static Platform getPlatform(Map<String, String> headers) {
        try {
            return Platform.valueOf(headers.get(APP_PLATFORM_KEY).toLowerCase());
        } catch (Exception ignored) {}
        return Platform.undefined;
    }

    private static String getPlatformVersion(Map<String, String> headers) {
        return StringUtils.defaultIfBlank(
            headers.get(PLATFORM_VERSION_KEY),
            StringUtils.defaultIfBlank(headers.get(APP_PLATFORM_VERSION_KEY), "")
        );
    }

    private static ZoneId getZoneId(Map<String, String> headers) {
        try {
            return ZoneId.of(headers.get(APP_TIMEZONE_KEY));
        } catch (Exception ignored) {}
        return ZoneId.systemDefault();
    }

    private static Locale getLocale(Map<String, String> headers) {
        try {
            String localeValue = headers.get(APP_LOCALE_KEY);
            if (StringUtils.isNotBlank(localeValue)) {
                return LocaleUtil.toLocale(localeValue);
            }
        } catch (Exception ignored) {}
        return Locale.getDefault();
    }

    public enum Platform {
        undefined,
        android,
        ios,
        web,
        arm
    }
}