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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.Composite;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.ResourcesBundle;

public class AuthenticationPanel extends Composite {

	private static final int _PANEL_HEIGHT = 120;
	private static final int _PANEL_WIDTH = 310;
	private FormPanel rootPanel;

	private TextField<String> tf_username;
	private TextField<String> tf_password;
	private Button bt_log;

	public AuthenticationPanel() {
		render();
		setListeners();
		initWidget(rootPanel);
	}

	private void render() {
		rootPanel = new FormPanel();
		rootPanel.setStyleName("authPanel");

		rootPanel.setSize(_PANEL_WIDTH, _PANEL_HEIGHT);
		rootPanel.setHeading(ResourcesBundle.getStrings()
				.authenticationPanelTitle());

		tf_username = new TextField<String>();
		tf_username.focus();
		tf_password = new TextField<String>();

		tf_username.setFieldLabel(ResourcesBundle.getStrings()
				.usernameFieldLabel());
		tf_password.setFieldLabel(ResourcesBundle.getStrings()
				.passwordFieldLabel());
		tf_password.setPassword(true);

		bt_log = new Button(ResourcesBundle.getStrings().logButtonLabel());

		rootPanel.add(tf_username);
		rootPanel.add(tf_password);
		rootPanel.add(bt_log);
	}

	public void resetForm() {
		tf_username.reset();
		tf_password.reset();
		tf_username.focus();
	}

	private void setListeners() {
		bt_log.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManager.getInstance().logIn(tf_username.getValue(),
						tf_password.getValue());
			}
		});
	}
}
