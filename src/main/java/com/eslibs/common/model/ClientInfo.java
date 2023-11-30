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
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 29.06.17
 */

public record ClientInfo(
    Platform platform,
    String platformVersion,
    String appVersion
) {

    private static final String APP_PLATFORM_KEY = "es-app-platform";
    private static final String APP_PLATFORM_VERSION_KEY = "es-app-platform-version";
    private static final String APP_VERSION_KEY = "es-app-version";

    public static ClientInfo from(Map<String, String> headers) {
        if (Items.isEmpty(headers)) {
            return new ClientInfo(Platform.undefined, "", "");
        }
        Platform platform = Platform.undefined;
        try {
            platform = Platform.valueOf(headers.get(APP_PLATFORM_KEY).toLowerCase());
        } catch (Exception ignored) {}
        return new ClientInfo(
            platform,
            StringUtils.defaultIfBlank(headers.get(APP_PLATFORM_VERSION_KEY), ""),
            StringUtils.defaultIfBlank(headers.get(APP_VERSION_KEY), "")
        );
    }

    public enum Platform {
        undefined,
        android,
        ios,
        web
    }
}