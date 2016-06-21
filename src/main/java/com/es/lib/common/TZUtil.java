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

package com.es.lib.common;

import java.util.*;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class TZUtil {

	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

	private TZUtil() {
	}

	public static boolean isCorrect(String timeZoneID) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
		return timeZone != null && timeZone.getID().equals(timeZoneID);
	}

	public static Collection<TimeZone> getAvailable() {
		List<TimeZone> timeZones = new ArrayList<>();
		for (final String id : TimeZone.getAvailableIDs()) {
			if (id.matches(TIMEZONE_ID_PREFIXES)) {
				timeZones.add(TimeZone.getTimeZone(id));
			}
		}
		Collections.sort(timeZones, (a, b) -> a.getID().compareTo(b.getID()));
		return timeZones;
	}
}
