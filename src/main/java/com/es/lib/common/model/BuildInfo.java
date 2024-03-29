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
package com.es.lib.common.model;

import com.es.lib.common.reflection.Reflects;
import com.es.lib.common.security.Hash;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Zuzoev - zuzoev.d@ext-system.com
 * @since 29.06.17
 */
@Getter
@ToString
public class BuildInfo implements Serializable {

    private final String name;
    private final String version;
    private final String date;
    private final String hash;

    public BuildInfo(String name, String version, String date) {
        this.name = name;
        this.version = version;
        this.date = date;
        hash = Hash.md5().get(name + version + date);
    }

    public Map<String, String> asMap() {
        return new LinkedHashMap<String, String>() {{
            put("name", name);
            put("version", version);
            put("date", date);
            put("hash", hash);
        }};
    }

    public static final String UNDEFINED = "UNDEFINED";
    public static final String UNDEFINED_VERSION = "UNDEFINED_VERSION";

    public static Collection<BuildInfo> list(String prefix) {
        return Reflects.getResources(prefix, s -> s.endsWith("build.properties")).stream()
                       .map(v -> (v.startsWith("/") ? "" : "/") + v).map(BuildInfo::create).collect(Collectors.toList());
    }

    public static BuildInfo create() {
        return create("/com/es/build.properties");
    }

    public static BuildInfo create(String name) {
        return create(() -> BuildInfo.class.getResourceAsStream(name));
    }

    public static BuildInfo create(Supplier<InputStream> buildInfoSupplier) {
        try (InputStream is = buildInfoSupplier.get()) {
            Properties props = loadProps(is);
            return new BuildInfo(
                props.getProperty("name", UNDEFINED),
                props.getProperty("version", UNDEFINED_VERSION),
                props.getProperty("date", UNDEFINED)
            );
        } catch (Exception e) {
            return new BuildInfo(UNDEFINED, UNDEFINED_VERSION, UNDEFINED);
        }
    }

    private static Properties loadProps(InputStream stream) throws IOException {
        Properties props = new Properties();
        props.load(stream);
        return props;
    }
}