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

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.gwos.client.apps.settings.DesktopSettings;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.constants.Strings;
import com.gwos.client.core.process.SystemProcessManager;
import com.gwos.client.domain.impl.SystemProcessPriority;
import com.gwos.client.ui.desktop.GwosDesktop;

public class ContextMenuBundle {

	public enum MenuContext {
		DESKTOP_CONTEXT, TASK_BAR_CONTEXT, EXPLORER_CONTEXT
	};

	private static final Strings _STRINGS = ResourcesBundle.getStrings();

	public static Menu getContextMenu(MenuContext context) {

		switch (context) {
		case DESKTOP_CONTEXT:
			Menu contextMenu = new Menu();
			contextMenu.setWidth(140);

			MenuItem alignOnGrid = new MenuItem();
			alignOnGrid.setText(_STRINGS.alignOnGrid());
			contextMenu.add(alignOnGrid);

			MenuItem customize = new MenuItem();
			customize.setText(_STRINGS.customizeDesktop());
			contextMenu.add(customize);

			alignOnGrid
					.addSelectionListener(new SelectionListener<MenuEvent>() {

						@Override
						public void componentSelected(MenuEvent ce) {
							GwosDesktop.getInstance().getIconsArea()
									.resetLayout();
						}
					});

			customize.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					SystemProcessManager.getInstance()
							.startProcess(SystemProcessPriority.MEDIUM,
									new DesktopSettings());
				}
			});

			return contextMenu;
		case TASK_BAR_CONTEXT:

			contextMenu = new Menu();
			contextMenu.setWidth(140);

			MenuItem restoreApp = new MenuItem();
			restoreApp.setText(_STRINGS.restoreApp());
			contextMenu.add(restoreApp);

			MenuItem closeApp = new MenuItem();
			closeApp.setText(_STRINGS.closeApp());
			contextMenu.add(closeApp);

			return contextMenu;

		case EXPLORER_CONTEXT:

			contextMenu = new Menu();
			contextMenu.setWidth(140);

			MenuItem renameItem = new MenuItem();
			renameItem.setText(_STRINGS.renameExplorerNode());
			contextMenu.add(renameItem);

			MenuItem deleteItem = new MenuItem();
			deleteItem.setText(_STRINGS.deleteExplorerNode());
			contextMenu.add(deleteItem);

			return contextMenu;

		}

		return null;
	}

}
