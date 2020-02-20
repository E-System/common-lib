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

package com.es.lib.common.security;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
public final class BCryptUtil {

    private BCryptUtil() { }

    public static String hash(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt(12));
    }

    public static boolean isValid(String text, String hash) {
        try {
            return text != null && BCrypt.checkpw(text, hash);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
