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

package com.gwos.client.ui.desktop;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.Composite;
import com.gwos.client.apps.tools.ShortcutsManager;
import com.gwos.client.auth.SessionManager;

public class GwosDesktop extends Composite {

	private static GwosDesktop _INSTANCE;
	private String wallpaperName;

	private ContentPanel rootPanel;
	private final WindowsArea wArea;
	private final IconsArea iArea;
	private final TasksBar tBar;

	private GwosDesktop() {

		rootPanel = new ContentPanel();
		wArea = new WindowsArea();
		iArea = new IconsArea();
		tBar = new TasksBar();
		render();
		initWidget(rootPanel);
	}

	public static GwosDesktop getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new GwosDesktop();
		}
		return _INSTANCE;
	}

	private final void render() {
		// Rendering the desktop
		rootPanel.setHeaderVisible(false);
		rootPanel.setBodyBorder(false);
		setWallpaper("desktop-wallpaper-tux", true);

		// 1. The center : icons area (on background) & windows area (on
		// foreground)
		rootPanel.add(iArea);

		// 2. The bottom : task bar
		rootPanel.add(tBar);

		// 3. Initialize shortcuts
		ShortcutsManager.getInstance().initGlobalHandler();

		// 4. Initialize the browser handler
		setBrowserClosingHandler();

		// 5. Initialize the message displayer
		SystemMessageDisplayer.listenToMessages();
	}

	private void setBrowserClosingHandler() {

		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				if (SessionManager.getInstance().isUserLogged() == false) {
					return;
				}
				// Request confirmation to the user when he close his browser
				event.setMessage("Changes on your session will be lost. Are you sure ?");
			}
		});

		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
				if (SessionManager.getInstance().isUserLogged() == false) {
					return;
				}
				// Before the browser is closed, save the session !
				SessionManager.getInstance().logout();
			}
		});
	}

	public void setWallpaper(String styleName, boolean init) {
		// Initialization phase
		if (init) {
			rootPanel.setBodyStyleName(styleName);
		}
		// When the root panel is already rendered => change body style directly
		else {
			rootPanel.getBody().setStyleName(styleName);
		}
	}

	public String getWallPaperName() {
		return wallpaperName;
	}

	public ContentPanel getRootPanel() {
		return rootPanel;
	}

	public WindowsArea getWindowArea() {
		return wArea;
	}

	public IconsArea getIconsArea() {
		return iArea;
	}

	public TasksBar getTaskBar() {
		return tBar;
	}
}
