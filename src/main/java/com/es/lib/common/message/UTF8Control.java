/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2019
 */

package com.es.lib.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 13.09.19
 */
public class UTF8Control extends ResourceBundle.Control {

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }

    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
        if (baseName == null || locale == null) {
            throw new NullPointerException();
        }
        return null;
    }
}
