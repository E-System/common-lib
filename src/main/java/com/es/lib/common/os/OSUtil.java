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

package com.es.lib.common.os;


import java.nio.file.Paths;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 24.05.15
 */
public final class OSUtil {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    public enum OS {
        WINDOWS,
        LINUX,
        MACOS,
        SOLARIS
    }

    private OSUtil() { }

    /**
     * Get path to configuration file
     *
     * @param vendorName     Vendor name
     * @param appName        Application name
     * @param configFileName Configuration file name
     * @return path to configuration file
     */
    public static String getConfigFilePath(String vendorName, String appName, String configFileName) {
        return Paths.get(getAppConfigPath(vendorName, appName), configFileName).toString();
    }

    /**
     * Get path to configuration folder
     *
     * @param vendorName Vendor name
     * @param appName    Application name
     * @return path to configuration folder
     */
    public static String getAppConfigPath(String vendorName, String appName) {
        switch (OSUtil.getOS()) {
            case WINDOWS:
                return Paths.get(System.getenv("LOCALAPPDATA"), vendorName, appName).toString();
            default:
                return Paths.get(System.getenv("HOME"), ".config", vendorName, appName).toString();
        }
    }

    /**
     * Get path to application folder
     *
     * @return path to application folder
     */
    public static String getAppPath() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
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