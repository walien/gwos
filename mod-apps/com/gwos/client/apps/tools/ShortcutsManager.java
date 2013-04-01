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

package com.gwos.client.apps.tools;

import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.ui.desktop.GwosDesktop;
import com.gwos.client.ui.desktop.WindowsArea;

public class ShortcutsManager {

	private static ShortcutsManager _INSTANCE;

	private ShortcutsManager() {
	}

	public static ShortcutsManager getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new ShortcutsManager();
		}
		return _INSTANCE;
	}

	public void initGlobalHandler() {

		Event.addNativePreviewHandler(new NativePreviewHandler() {

			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				NativeEvent ne = event.getNativeEvent();
				if (event.getTypeInt() != Event.ONKEYDOWN) {
					return;
				}
				if (ne.getAltKey() == false && ne.getCtrlKey() == false) {
					return;
				}
				event.cancel();

				// CTRL Shortcuts
				if (ne.getCtrlKey()) {

					// ///////////////////////////
					// / (CTRL) SYSTEM SHORTCUTS
					// ///////////////////////////

					// Session logout
					if (ne.getKeyCode() == (int) 'l'
							|| ne.getKeyCode() == (int) 'L') {
						SessionManager.getInstance().logout();
					}
					// Application switcher shortcut
					if (ne.getKeyCode() == KeyCodes.KEY_TAB) {
						// TODO
					}

					// /////////////////////////
					// / (CTRL) APP SHORTCUTS
					// /////////////////////////

					// Quit application shortcut
					if (ne.getKeyCode() == 'q' || ne.getKeyCode() == 'Q') {
						WindowsArea wArea = GwosDesktop.getInstance()
								.getWindowArea();
						Window activeWin = wArea.getActiveWindow();
						if (activeWin == null) {
							return;
						}
						int pid = wArea.getAsociatePID(activeWin);
						if (pid == -1) {
							return;
						}
						CommandInterpreter.getInstance().handleSystemCommand(
								new Command(CommandType.KILL_PROCESS,
										new String[] { Integer.toString(pid) },
										CommandSource.UI_APP));
					}
				}
				// ALT System Shortcuts
				if (ne.getAltKey()) {
					// TODO
				}
			}
		});
	}
}
