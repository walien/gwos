/*
 * Copyright 2013 - Elian ORIOU
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

package com.gwos.client.packets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwos.client.domain.impl.Application;

public class AppsManager {

	private static AppsManager _INSTANCE;
	private Map<String, Application> installedApps;

	private AppsManager() {
		installedApps = new HashMap<String, Application>();
	}

	public static AppsManager getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new AppsManager();
		}
		return _INSTANCE;
	}

	public void installApp(String[] availableCommands, Application app) {
		for (String command : availableCommands) {
			installedApps.put(command, app);
		}
	}

	public List<String> getInstalledAppsCommands() {
		return new ArrayList<String>(installedApps.keySet());
	}

	public Application getApp(String command) {
		return installedApps.get(command);
	}

	public Collection<Application> getInstalledApplication() {
		return installedApps.values();
	}
}
