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

package com.gwos.client.tests;

import com.google.gwt.user.client.ui.Image;
import com.gwos.client.apps.AdministrationApp;
import com.gwos.client.apps.ExplorerApp;
import com.gwos.client.apps.FileEditorApp;
import com.gwos.client.apps.ProcessManagerApp;
import com.gwos.client.apps.ShellApp;
import com.gwos.client.apps.WebBrowserApp;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.packets.AppsManager;
import com.gwos.client.ui.desktop.GwosDesktop;
import com.gwos.client.ui.desktop.components.DesktopIcon;

public class AppInitializer {

	public static void makeConfiguration() {

		AppsManager.getInstance().installApp(new String[] { "explorer" },
				new ExplorerApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("Explorer", new Image(ResourcesBundle
								.getBinaryResources().computer_logo()),
								new Command(CommandType.LAUNCH_PROCESS,
										new String[] { "explorer" },
										CommandSource.DESKTOP)));

		AppsManager.getInstance().installApp(new String[] { "shell" },
				new ShellApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("Shell", new Image(ResourcesBundle
								.getBinaryResources().shell_logo()),
								new Command(CommandType.LAUNCH_PROCESS,
										new String[] { "shell" },
										CommandSource.DESKTOP)));

		AppsManager.getInstance().installApp(new String[] { "editor" },
				new FileEditorApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("File Editor", new Image(
								ResourcesBundle.getBinaryResources()
										.editor_logo()), new Command(
								CommandType.LAUNCH_PROCESS,
								new String[] { "editor" },
								CommandSource.DESKTOP)));

		AppsManager.getInstance().installApp(new String[] { "processmanager" },
				new ProcessManagerApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("Process Manager", new Image(
								ResourcesBundle.getBinaryResources()
										.processes_logo()), new Command(
								CommandType.LAUNCH_PROCESS,
								new String[] { "processmanager" },
								CommandSource.DESKTOP)));

		AppsManager.getInstance().installApp(new String[] { "webbrowser" },
				new WebBrowserApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("Web Browser", new Image(
								ResourcesBundle.getBinaryResources()
										.web_browser_logo()), new Command(
								CommandType.LAUNCH_PROCESS,
								new String[] { "webbrowser" },
								CommandSource.DESKTOP)));

		AppsManager.getInstance().installApp(new String[] { "admin" },
				new AdministrationApp());
		GwosDesktop
				.getInstance()
				.getIconsArea()
				.addIcon(
						new DesktopIcon("Admin Tools", new Image(
								ResourcesBundle.getBinaryResources()
										.tools_logo()),
								new Command(CommandType.LAUNCH_PROCESS,
										new String[] { "admin" },
										CommandSource.DESKTOP)));
	}
}
