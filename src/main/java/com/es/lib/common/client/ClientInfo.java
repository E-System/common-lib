package com.es.lib.common.client;

import com.es.lib.common.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@ToString
@AllArgsConstructor
public class ClientInfo {

    private static final String PLATFORM_KEY = "es-app-platform";
    private static final String PLATFORM_VERSION_KEY = "es-platform-version";
    private static final String APP_VERSION_KEY = "es-app-version";

    @Getter
    private Platform platform;
    @Getter
    private String platformVersion;
    @Getter
    private String appVersion;

    public static ClientInfo create(Map<String, String> headers) {
        if (CollectionUtil.isEmpty(headers)) {
            return new ClientInfo(Platform.undefined, "", "");
        }
        Platform platform = Platform.undefined;
        try {
            platform = Platform.valueOf(headers.get(PLATFORM_KEY).toLowerCase());
        } catch (Exception ignored) { }
        return new ClientInfo(
            platform,
            StringUtils.defaultIfBlank(headers.get(PLATFORM_VERSION_KEY), ""),
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