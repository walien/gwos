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

package com.gwos.client.apps.components;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Composite;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.File;
import com.gwos.client.domain.impl.NodeType;
import com.gwos.client.ui.desktop.components.ContextMenuBundle;
import com.gwos.client.ui.desktop.components.ContextMenuBundle.MenuContext;
import com.gwos.client.ui.desktop.components.IContextMenuCapable;

public class ExplorerIcon extends Composite implements IContextMenuCapable {

	private INode node;

	private VerticalPanel rootPanel;
	private Image icon;
	private Label text;

	public ExplorerIcon(INode node) {
		this.node = node;
		render();
		initComponent(rootPanel);
	}

	private void render() {
		rootPanel = new VerticalPanel();

		if (node.getNodeType() == NodeType.FILE) {
			File f = (File) node;
			switch (f.getFileType()) {
			case TEXT:
				icon = new Image(ResourcesBundle.getBinaryResources()
						.file_large_logo());
				break;
			case MUSIC:
				break;
			case BINARY:
				break;
			}

		} else {
			icon = new Image(ResourcesBundle.getBinaryResources()
					.folder_large_logo());
		}
		text = new Label(node.getName());
		text.setStylePrimaryName("apps-explorer-element-text");

		rootPanel.add(icon);
		rootPanel.add(text);

		setContextualMenu(rootPanel);
	}

	public void addDoubleClickListener(DoubleClickHandler handler) {
		icon.addDoubleClickHandler(handler);
		text.addDoubleClickHandler(handler);
	}

	public INode getNode() {
		return node;
	}

	@Override
	public void setContextualMenu(Component component) {
		Menu context = ContextMenuBundle
				.getContextMenu(MenuContext.EXPLORER_CONTEXT);

		MenuItem renameItem = (MenuItem) context.getItem(0);
		MenuItem deleteItem = (MenuItem) context.getItem(1);

		renameItem.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {

				final MessageBox box = MessageBox.prompt("Rename a node", "");
				box.getTextBox().setValue(node.getName());
				box.addCallback(new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						CommandInterpreter.getInstance().handleSystemCommand(
								new Command(CommandType.RENAME_NODE,
										new String[] { node.getPath(),
												box.getTextBox().getValue() },
										CommandSource.UI_APP));
					}
				});
				box.show();
			}
		});

		deleteItem.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				final MessageBox box = MessageBox.confirm(
						"Deletion ?",
						"Do you confirm the deletion for the node \""
								+ node.getName() + "\" ?",
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								if (Dialog.YES.equalsIgnoreCase(be
										.getButtonClicked().getItemId()) == false) {
									return;
								}
								CommandInterpreter
										.getInstance().handleSystemCommand(
												new Command(
														CommandType.RM_NODE,
														new String[] { node
																.getPath() },
														CommandSource.UI_APP));
							}
						});
				box.show();
			}
		});

		component.setContextMenu(context);
	}
}
