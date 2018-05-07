package com.es.lib.common.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 07.05.2018
 */
public final class ServerInfoUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ServerInfoUtil.class);

    private ServerInfoUtil() {}

    public static void log(String serverInfo) {
        int slashPos = serverInfo.indexOf("/");
        String name = (slashPos == -1 ? serverInfo : serverInfo.substring(0, slashPos));
        String version = (slashPos == -1 ? null : serverInfo.substring(slashPos + 1));
        boolean tomcat = name.toLowerCase().contains("tomcat");
        LOG.info("Container: {} {} (Tomcat: {})", name, version, tomcat);
    }
}
