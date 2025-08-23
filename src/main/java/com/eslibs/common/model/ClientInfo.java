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
package com.eslibs.common.model;

import com.eslibs.common.collection.Items;
import com.eslibs.common.locale.Locales;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 29.06.17
 */

public record ClientInfo(
    Platform platform,
    String platformVersion,
    String appVersion,
    ZoneId appTimezone,
    Locale appLocale,
    String appKey
) implements Serializable {

    private static final String APP_PLATFORM_KEY = "es-app-platform";
    private static final String APP_PLATFORM_VERSION_KEY = "es-app-platform-version";
    private static final String APP_VERSION_KEY = "es-app-version";
    private static final String APP_TIMEZONE_KEY = "es-app-timezone";
    private static final String APP_LOCALE_KEY = "es-app-locale";
    private static final String APP_KEY = "es-app-key";

    public static ClientInfo from(Map<String, String> headers) {
        if (Items.isEmpty(headers)) {
            return new ClientInfo(
                Platform.undefined,
                "",
                "",
                ZoneId.systemDefault(),
                Locale.getDefault(),
                null
            );
        }
        return new ClientInfo(
            getPlatform(headers),
            StringUtils.defaultIfBlank(headers.get(APP_PLATFORM_VERSION_KEY), ""),
            StringUtils.defaultIfBlank(headers.get(APP_VERSION_KEY), ""),
            getZoneId(headers),
            getLocale(headers),
            StringUtils.defaultIfBlank(headers.get(APP_KEY), "")
        );
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<>();
        if (platform != null) {
            result.put(APP_PLATFORM_KEY, platform.name());
        }
        if (StringUtils.isNotBlank(platformVersion)) {
            result.put(APP_PLATFORM_VERSION_KEY, platformVersion);
        }
        if (StringUtils.isNotBlank(appVersion)) {
            result.put(APP_VERSION_KEY, appVersion);
        }
        if (appTimezone != null) {
            result.put(APP_TIMEZONE_KEY, appTimezone.getId());
        }
        if (appLocale != null) {
            result.put(APP_LOCALE_KEY, appLocale.toString());
        }
        if (StringUtils.isNotBlank(appKey)) {
            result.put(APP_KEY, appKey);
        }
        return result;
    }

    private static Platform getPlatform(Map<String, String> headers) {
        try {
            return Platform.valueOf(headers.get(APP_PLATFORM_KEY).toLowerCase());
        } catch (Exception _) {}
        return Platform.undefined;
    }

    private static ZoneId getZoneId(Map<String, String> headers) {
        try {
            return ZoneId.of(headers.get(APP_TIMEZONE_KEY));
        } catch (Exception _) {}
        return ZoneId.systemDefault();
    }

    private static Locale getLocale(Map<String, String> headers) {
        try {
            String localeValue = headers.get(APP_LOCALE_KEY);
            if (StringUtils.isNotBlank(localeValue)) {
                return Locales.of(localeValue);
            }
        } catch (Exception _) {}
        return Locale.getDefault();
    }

    public enum Platform {
        undefined,
        android,
        ios,
        web,
        arm,
        telegram
    }
}