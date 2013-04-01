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

package com.gwos.client.core.system;

import com.google.gwt.user.client.ui.RootPanel;
import com.gwos.client.apps.tools.ShortcutsManager;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.core.process.SystemProcessManager;
import com.gwos.client.packets.AppsManager;
import com.gwos.client.services.ServiceBundle;
import com.gwos.client.tests.AppInitializer;
import com.gwos.client.ui.auth.AuthenticationScreen;
import com.gwos.client.ui.desktop.GwosDesktop;

public class SystemBootStrap {

	/**
	 * Init system components
	 */

	public static void init() {

		// 0. Init services bundle
		ServiceBundle.getInstance();

		// 1. Init User FS
		FileSystemManager.getInstance();

		// 2. Init process manager
		SystemProcessManager.getInstance();

		// 3. Init apps manager
		AppsManager.getInstance();

		// 4. Init apps and configure them
		AppInitializer.makeConfiguration();

		// 5. Init the system event manager
		SystemEventManager.getInstance();

		// 6. Init the shortcuts manager
		ShortcutsManager.getInstance();

		// 7. Init UI
		RootPanel.get().remove(AuthenticationScreen.getInstance());
		RootPanel.get().add(GwosDesktop.getInstance());
	}
}
