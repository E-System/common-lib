/*
 * Copyright (c) Extended System - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Extended System team (https://ext-system.com), 2015
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
