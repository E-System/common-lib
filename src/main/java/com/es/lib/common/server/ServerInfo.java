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

package com.es.lib.common.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
@Deprecated
public final class ServerInfo {

    private String name;
    private String version;
    private boolean tomcat;

    private ServerInfo() { }

    public static ServerInfo getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    public void init(String serverInfo) {
        int slashPos = serverInfo.indexOf("/");
        name = (slashPos == -1 ? serverInfo : serverInfo.substring(0, slashPos));
        version = (slashPos == -1 ? null : serverInfo.substring(slashPos + 1));
        tomcat = name.toLowerCase().contains("tomcat");
    }

    public void log() {
        log.info("Container: {} {} (Tomcat: {})", name, version, tomcat);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isTomcat() {
        return tomcat;
    }

    private static class InstanceWrapper {

        final static ServerInfo INSTANCE = new ServerInfo();
    }
}
