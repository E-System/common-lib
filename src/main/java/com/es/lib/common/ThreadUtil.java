/*
 * Copyright (c) E-System LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by E-System team (https://ext-system.com), 2017
 */

package com.es.lib.common;

/**
 * @author Vitaliy Savchenko - savchenko.v@ext-system.com
 * @since 12.06.15
 */
public class ThreadUtil {

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
