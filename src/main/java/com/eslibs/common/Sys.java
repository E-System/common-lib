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

package com.eslibs.common;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 24.05.15
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Sys {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    public enum OS {
        WINDOWS,
        LINUX,
        MACOS,
        SOLARIS
    }

    public static <T> T measure(String prefix, Supplier<T> supplier) {
        return measure(prefix, false, supplier);
    }

    public static <T> T measure(String prefix, boolean skipLogResult, Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T result = supplier.get();
        if (skipLogResult) {
            log.trace("{}: {} ms", prefix, System.currentTimeMillis() - start);
        } else {
            log.trace("{}: {} ms - [{}]", prefix, System.currentTimeMillis() - start, result);
        }
        return result;
    }

    public static void measure(String prefix, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        log.trace("{}: {} ms", prefix, System.currentTimeMillis() - start);
    }

    /**
     * Sleep thread for timeout (Interrupt current thread if InterruptedException thrown in sleep)
     *
     * @param timeout Timeout to sleep
     */
    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get throwable root cause
     *
     * @param t Throwable
     * @return Root cause ot throwable (Top)
     */
    public static Throwable rootCause(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null) {
            cur = cur.getCause();
        }
        return cur;
    }

    /**
     * Get path to configuration file
     *
     * @param vendorName     Vendor name
     * @param appName        Application name
     * @param configFileName Configuration file name
     * @return path to configuration file
     */
    public static Path getConfigFilePath(String vendorName, String appName, String configFileName) {
        return getAppConfigPath(vendorName, appName).resolve(configFileName);
    }

    /**
     * Get path to configuration folder
     *
     * @param vendorName Vendor name
     * @param appName    Application name
     * @return path to configuration folder
     */
    public static Path getAppConfigPath(String vendorName, String appName) {
        if (Objects.requireNonNull(Sys.getOS()) == OS.WINDOWS) {
            if (StringUtils.isBlank(System.getenv("LOCALAPPDATA"))) {
                return Paths.get(System.getenv("APPDATA"), vendorName, appName);
            }
            return Paths.get(System.getenv("LOCALAPPDATA"), vendorName, appName);
        }
        return Paths.get(System.getenv("HOME"), ".config", vendorName, appName);
    }

    /**
     * Get path to application folder
     *
     * @return path to application folder
     */
    public static Path getAppPath() {
        return Paths.get(".").toAbsolutePath().normalize();
    }

    public static OS getOS() {
        return getOS(OS_NAME);
    }

    private static OS getOS(String property) {
        if (property.contains("win")) {
            return OS.WINDOWS;
        } else if (property.contains("nix") || property.contains("nux") || property.contains("aix")) {
            return OS.LINUX;
        } else if (property.contains("mac")) {
            return OS.MACOS;
        } else if (property.contains("sunos")) {
            return OS.SOLARIS;
        }
        return null;
    }
}
