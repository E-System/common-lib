package com.es.lib.common.model;

import com.es.lib.common.security.HashUtil;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
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
        hash = HashUtil.md5(name + version + date);
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