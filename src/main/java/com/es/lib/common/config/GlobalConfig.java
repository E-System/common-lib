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

package com.es.lib.common.config;

import com.es.lib.common.os.OSUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;
import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.07.15
 */
@Slf4j
@Getter
@Deprecated
public class GlobalConfig {

    private String name;
    private String version;
    private OSUtil.OS os;

    private GlobalConfig() { }

    public static GlobalConfig getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    public void init(Supplier<InputStream> resourceSupplier) {
        os = OSUtil.getOS();
        Properties props = new Properties();
        try (InputStream is = resourceSupplier.get()) {
            props.load(is);
            name = props.getProperty("name");
            version = props.getProperty("version");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void log() {
        log.info("OS: {}", os);
        log.info("Application: {}-{}", name, version);
        log.info("Server time zone: {}", TimeZone.getDefault());
    }

    private static class InstanceWrapper {

        final static GlobalConfig INSTANCE = new GlobalConfig();
    }
}
