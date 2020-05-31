package com.es.lib.common.model;

import com.es.lib.common.collection.Cols;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class ClientInfo {

    private static final String APP_PLATFORM_KEY = "es-app-platform";
    private static final String APP_PLATFORM_VERSION_KEY = "es-app-platform-version";
    @Deprecated
    private static final String PLATFORM_VERSION_KEY = "es-platform-version";
    private static final String APP_VERSION_KEY = "es-app-version";

    private final Platform platform;
    private final String platformVersion;
    private final String appVersion;

    public static ClientInfo create(Map<String, String> headers) {
        if (Cols.isEmpty(headers)) {
            return new ClientInfo(Platform.undefined, "", "");
        }
        Platform platform = Platform.undefined;
        try {
            platform = Platform.valueOf(headers.get(APP_PLATFORM_KEY).toLowerCase());
        } catch (Exception ignored) { }
        return new ClientInfo(
            platform,
            StringUtils.defaultIfBlank(
                headers.get(PLATFORM_VERSION_KEY),
                StringUtils.defaultIfBlank(headers.get(APP_PLATFORM_VERSION_KEY), "")
            ),
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