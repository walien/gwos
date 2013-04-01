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

package com.gwos.client.ui.desktop.components;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwos.client.core.shell.Command;

public class DesktopIcon extends Component {

	private VerticalPanel rootPanel;
	private String s_label;
	private Label l_label;
	private Image icon;
	private Command command;

	public DesktopIcon(String label, Image icon, Command command) {
		super();
		this.s_label = label;
		this.command = command;
		this.icon = icon;
		this.icon.setSize("50px", "50px");
		render();
		setElement(rootPanel.getElement());
	}

	private void render() {
		rootPanel = new VerticalPanel();
		rootPanel.add(icon);
		l_label = new Label(s_label);
		l_label.setStylePrimaryName("desktopIcon-text");
		rootPanel.add(l_label);
	}

	public String getLabel() {
		return s_label;
	}

	public Image getIcon() {
		return icon;
	}

	public Command getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return "DesktopIcon [label=" + s_label + ", icon=" + icon + "]";
	}
}
