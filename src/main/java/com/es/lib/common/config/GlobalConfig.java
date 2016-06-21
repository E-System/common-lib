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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 16.07.15
 */
public class GlobalConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalConfig.class);

    private String name;
    private String version;

    public GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        return InstanceWrapper.INSTANCE;
    }

    public void init(Supplier<InputStream> resourceSupplier) throws IOException {
        Properties props = new Properties();
        try (InputStream is = resourceSupplier.get()) {
            props.load(is);
            name = props.getProperty("name");
            version = props.getProperty("version");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void log() {
        LOG.info("Application: {}-{}", name, version);
        LOG.info("Server time zone: {}", TimeZone.getDefault());
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    private static class InstanceWrapper {

        final static GlobalConfig INSTANCE = new GlobalConfig();
    }
}
