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

package com.gwos.client.apps;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.UserGroup;

public class WebBrowserApp extends Application {

	private String name;
	private String author;
	private int version;

	private String currentURL;

	private ContentPanel rootPanel;

	public WebBrowserApp() {
		this.name = "Web Browser";
		this.author = "walien";
		this.version = 1;

		this.currentURL = "http://www.cnet.com";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getParentWidth() {
		return 500;
	}

	@Override
	public int getParentHeight() {
		return 300;
	}

	@Override
	public Widget getWidget() {
		return rootPanel;
	}

	@Override
	public void setParams(String[] params) {
	}

	@Override
	public void start(int pid) {

		rootPanel = new ContentPanel();
		rootPanel.setUrl(currentURL);
		rootPanel.setHeaderVisible(false);
		rootPanel.setBorders(false);
		rootPanel.setBodyBorder(false);

		ToolBar toolBar = buildBrowserToolbar();
		rootPanel.setTopComponent(toolBar);
	}

	private ToolBar buildBrowserToolbar() {
		ToolBar bar = new ToolBar();

		Button back = new Button("back");
		Button next = new Button("next");
		Button ok = new Button("ok");

		final TextField<String> url = new TextField<String>();
		url.setValue(currentURL);

		bar.add(back);
		bar.add(next);
		bar.add(url);
		bar.add(ok);

		back.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub

			}
		});

		next.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub

			}
		});

		ok.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (url.getValue().isEmpty()) {
					return;
				}
				loadURL(url.getValue());
			}
		});

		return bar;
	}

	private void loadURL(String url) {
		currentURL = url;
		rootPanel.setUrl(currentURL);
		rootPanel.layout(true);
	}

	@Override
	public int exit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
