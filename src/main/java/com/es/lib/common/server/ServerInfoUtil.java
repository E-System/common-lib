package com.es.lib.common.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.05.2018
 */
@Slf4j
public final class ServerInfoUtil {

    private ServerInfoUtil() {}

    public static void log(String serverInfo) {
        int slashPos = serverInfo.indexOf("/");
        String name = (slashPos == -1 ? serverInfo : serverInfo.substring(0, slashPos));
        String version = (slashPos == -1 ? null : serverInfo.substring(slashPos + 1));
        boolean tomcat = name.toLowerCase().contains("tomcat");
        log.info("Container: {} {} (Tomcat: {})", name, version, tomcat);
    }
}
