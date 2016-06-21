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

package com.es.lib.common.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class ServerInfo {

	private static final Logger LOG = LoggerFactory.getLogger(ServerInfo.class);

	private String name;
	private String version;
	private boolean tomcat;

	public ServerInfo() {
	}

	public static ServerInfo getInstance() {
		return InstanceWrapper.INSTANCE;
	}

	public void init(String serverInfo) {
		int slashPos = serverInfo.indexOf("/");
		name = (slashPos == -1 ? serverInfo : serverInfo.substring(0, slashPos));
		version = (slashPos == -1 ? null : serverInfo.substring(slashPos + 1));
		tomcat = name.toLowerCase().contains("tomcat");
	}

	public void log() {
		LOG.info("Container: {}", name);
		LOG.info("Container version: {}", version);
		LOG.info("Is Tomcat: {}", tomcat);
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public boolean isTomcat() {
		return tomcat;
	}

	private static class InstanceWrapper {

		final static ServerInfo INSTANCE = new ServerInfo();
	}
}
