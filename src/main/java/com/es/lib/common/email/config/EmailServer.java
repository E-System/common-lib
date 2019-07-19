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

package com.es.lib.common.email.config;

import java.io.Serializable;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class EmailServer implements Cloneable, Serializable {

    private String host;
    private int port;
    private boolean ssl;
    private boolean tls;
    private int timeout;

    public EmailServer() {
        timeout = 20000;
    }

    public EmailServer(String host, int port, boolean ssl) {
        this();
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.tls = false;
    }

    public EmailServer(String host, int port, boolean ssl, int timeout) {
        this(host, port, ssl);
        this.timeout = timeout;
    }

    public EmailServer(String host, int port, boolean ssl, boolean tls) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.tls = tls;
    }

    public EmailServer(String host, int port, boolean ssl, boolean tls, int timeout) {
        this(host, port, ssl, tls);
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isTls() {
        return tls;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EmailServer object = (EmailServer) super.clone();
        object.host = host;
        object.port = port;
        object.ssl = ssl;
        object.tls = tls;
        object.timeout = timeout;
        return object;
    }

    @Override
    public String toString() {
        return "EmailServer{" +
               "host='" + host + '\'' +
               ", port=" + port +
               ", ssl=" + ssl +
               ", tls=" + tls +
               ", timeout=" + timeout +
               '}';
    }
}
