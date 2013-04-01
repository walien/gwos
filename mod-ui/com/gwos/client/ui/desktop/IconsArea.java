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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.gwos.client.constants.Constants;
import com.gwos.client.context.ClientContext;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.ui.desktop.components.ContextMenuBundle;
import com.gwos.client.ui.desktop.components.ContextMenuBundle.MenuContext;
import com.gwos.client.ui.desktop.components.DesktopIcon;
import com.gwos.client.ui.desktop.components.IContextMenuCapable;

public class IconsArea extends Composite implements IContextMenuCapable {

	private int iconX;
	private int iconY;
	private LayoutContainer rootPanel;
	private List<DesktopIcon> icons;

	public IconsArea() {
		this.iconX = Constants._DESKTOP_ICONS_X_START;
		this.iconY = Constants._DESKTOP_ICONS_Y_START;
		this.icons = new ArrayList<DesktopIcon>();
		render();
		initWidget(rootPanel);
		setAreaSize();
	}

	private void render() {
		rootPanel = new LayoutContainer(new AbsoluteLayout());
		rootPanel.setLayoutOnChange(true);
		setContextualMenu(rootPanel);
	}

	private void addIconOnDesktop(DesktopIcon icon) {
		rootPanel.add(icon, new AbsoluteData(iconX, iconY));
		if (iconY > ClientContext.getInstance().getBrowserHeight()
				- Constants._DESKTOP_ICONS_Y_SPACE) {
			iconY = Constants._DESKTOP_ICONS_Y_START;
			iconX += Constants._DESKTOP_ICONS_X_SPACE;
		} else {
			iconY += Constants._DESKTOP_ICONS_Y_SPACE;
		}
		setListeners(icon);
		new Draggable(icon);
	}

	public void addIcon(DesktopIcon icon) {
		icons.add(icon);
		addIconOnDesktop(icon);
	}

	public void resetLayout() {

		this.iconX = Constants._DESKTOP_ICONS_X_START;
		this.iconY = Constants._DESKTOP_ICONS_Y_START;

		// Remove all childs
		rootPanel.removeAll();

		// Re-add them
		for (DesktopIcon icon : icons) {
			icon.removeAllListeners();
			addIconOnDesktop(icon);
		}
	}

	private void setListeners(final DesktopIcon icon) {
		icon.addListener(Events.OnDoubleClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				CommandInterpreter.getInstance().handleSystemCommand(
						icon.getCommand());
			}
		});
	}

	private void setAreaSize() {
		rootPanel.setSize(Window.getClientWidth(), Window.getClientHeight()
				- Constants.TASK_BAR_HEIGHT_OFFSET);
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				rootPanel.setSize(event.getWidth(), event.getHeight()
						- Constants.TASK_BAR_HEIGHT_OFFSET);
			}
		});
	}

	@Override
	public void setContextualMenu(Component component) {
		Menu contextMenu = ContextMenuBundle
				.getContextMenu(MenuContext.DESKTOP_CONTEXT);
		component.setContextMenu(contextMenu);
	}
}
