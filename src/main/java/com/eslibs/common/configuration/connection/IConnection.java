package com.eslibs.common.configuration.connection;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 15.10.2023
 * Marker for all connections
 */
public interface IConnection extends Serializable {

    long DEFAULT_CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(20);

    long DEFAULT_RW_TIMEOUT = TimeUnit.SECONDS.toMillis(60);
}
