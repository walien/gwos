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

package com.gwos.client.ui.auth;

import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.events.AuthenticationEvent;
import com.gwos.client.core.events.AuthenticationEventHandler;
import com.gwos.client.core.events.SystemEventManager;

public class AuthenticationScreen extends Composite {

	private static AuthenticationScreen _INSTANCE;

	private AbsolutePanel rootPanel;
	private HTML appTitle;
	private HTML appAuthor;
	private AuthenticationPanel authPanel;

	private static final String _APP_TITLE_HTML = "<h1 style=\"font-size: 50pt;\">"
			+ ResourcesBundle.getStrings().appTitle()
			+ " "
			+ ResourcesBundle.getStrings().appVersion() + "</h1></br>";
	private static final String _APP_AUTHOR_HTML = "<h3 style=\"font-size: 20pt; color:blue; margin:auto; text-align:center;\">"
			+ ResourcesBundle.getStrings().appAuthor() + "</h3>";

	private AuthenticationScreen() {
		render();
		initWidget(rootPanel);
	}

	public static AuthenticationScreen getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new AuthenticationScreen();
		}
		return _INSTANCE;
	}

	private void render() {
		rootPanel = new AbsolutePanel();

		appTitle = new HTML(_APP_TITLE_HTML);
		appAuthor = new HTML(_APP_AUTHOR_HTML);
		authPanel = new AuthenticationPanel();

		rootPanel.add(appTitle);
		rootPanel.add(appAuthor);
		rootPanel.add(authPanel);

		setListeners();
	}

	private void setListeners() {
		SystemEventManager.getInstance().addEventHandler(
				AuthenticationEvent.TYPE, new AuthenticationEventHandler() {

					@Override
					public void onAuthenticate(AuthenticationEvent event) {
						if (event.getUser() != null) {
							return;
						}
						Info.display("Error",
								"Authentication Error ! Please retry.");
						authPanel.resetForm();
					}
				});
	}
}
