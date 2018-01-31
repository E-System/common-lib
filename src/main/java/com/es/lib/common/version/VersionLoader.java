package com.es.lib.common.version;

import com.es.lib.common.exception.ESRuntimeException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 02.08.15
 */
public interface VersionLoader {

    String UNDEFINED = "UNDEFINED";
    String UNDEFINED_VERSION = "UNDEFINED_VERSION";
    Map<String, String> VERSIONS = new HashMap<>();

    static BuildInfo readBuildInfo() {
        return readBuildInfo(() -> VersionLoader.class.getResourceAsStream("/com/es/build.properties"));
    }

    static BuildInfo readBuildInfo(Supplier<InputStream> buildInfoSupplier) {
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

    static Map.Entry<String, Collection<String>> readVersionAndModules(Supplier<InputStream> streamSupplier) throws IOException {
        try (InputStream is = streamSupplier.get()) {
            Properties props = loadProps(is);
            return Pair.of(
                props.getProperty("version", UNDEFINED_VERSION),
                Arrays.asList(props.getProperty("modules", "").split(","))
            );
        }
    }

    static String readVersion(Supplier<InputStream> streamSupplier) {
        try (InputStream is = streamSupplier.get()) {
            return loadProps(is).getProperty("version", UNDEFINED_VERSION);
        } catch (IOException e) {
            throw new ESRuntimeException(e);
        }
    }

    static Properties loadProps(InputStream stream) throws IOException {
        Properties props = new Properties();
        props.load(stream);
        return props;
    }

    static void loadVersions(Supplier<InputStream> mainSupplier, Supplier<InputStream> moduleSupplier) throws IOException {
        Map.Entry<String, Collection<String>> versionAndModules = readVersionAndModules(mainSupplier);
        VersionLoader.VERSIONS.put("core", versionAndModules.getKey());
        versionAndModules.getValue().forEach(v -> VersionLoader.VERSIONS.put(v, readVersion(moduleSupplier)));
    }
}
