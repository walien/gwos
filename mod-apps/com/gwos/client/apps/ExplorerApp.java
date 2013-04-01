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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStoreModel;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.apps.components.ExplorerIcon;
import com.gwos.client.apps.tools.ExplorerDisplayDecorator;
import com.gwos.client.apps.widgets.FSTreeWidget;
import com.gwos.client.constants.Constants;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.events.FileSystemEvent;
import com.gwos.client.core.events.FileSystemEventHandler;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.NodeType;
import com.gwos.client.domain.impl.UserGroup;

public class ExplorerApp extends Application {

	private static final int _MAX_ELEMENTS_PER_LINE = 5;
	private static final int _ELEMENTS_SPACING = 10;

	private Directory displayedDir;

	private String name;
	private int version;
	private String author;

	private LayoutContainer widget;
	private ContentPanel treePanel;
	private ContentPanel listPanel;
	private FSTreeWidget tree;
	private LayoutContainer dirTable;

	public ExplorerApp() {
		this.name = "Explorer";
		this.version = 1;
		this.author = "walien";
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
		return 700;
	}

	@Override
	public int getParentHeight() {
		return 500;
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	public void setParams(String[] params) {
	}

	@Override
	public void start(int pid) {

		// 1. create panels
		widget = new LayoutContainer();
		widget.setLayout(new BorderLayout());

		treePanel = new ContentPanel();
		treePanel.setScrollMode(Scroll.AUTO);
		treePanel.setLayout(new FitLayout());
		listPanel = new ContentPanel();
		listPanel.setScrollMode(Scroll.AUTO);
		listPanel.setLayout(new FitLayout());

		// 2. render tree panel content
		tree = new FSTreeWidget(true);
		tree.addListener(Events.OnClick,
				new Listener<TreePanelEvent<ModelData>>() {
					public void handleEvent(TreePanelEvent<ModelData> be) {
						Directory dir = FileSystemManager
								.getInstance()
								.getDirectory(
										(String) be
												.getNode()
												.getModel()
												.get(Constants.FS_TREE_NODE_PATH));
						displayFolder(dir);
					};
				});
		treePanel.add(tree);

		// 3. Enable DND on tree
		enableTreeDND();

		// 4. Render list panel
		dirTable = new LayoutContainer();

		TableLayout layout = new TableLayout(_MAX_ELEMENTS_PER_LINE);
		layout.setCellSpacing(_ELEMENTS_SPACING);

		dirTable.setLayout(layout);
		listPanel.add(dirTable);

		displayFolder(FileSystemManager.getInstance().getRoot());

		// 5. Layout data
		BorderLayoutData treeLayoutData = new BorderLayoutData(
				LayoutRegion.WEST);
		treeLayoutData.setSplit(true);
		treeLayoutData.setCollapsible(true);

		BorderLayoutData listLayoutData = new BorderLayoutData(
				LayoutRegion.CENTER);
		listLayoutData.setSplit(true);

		// 6. Add the tool bar
		listPanel.setTopComponent(buildExplorerToolbar());

		// 7. Add widgets
		widget.add(treePanel, treeLayoutData);
		widget.add(listPanel, listLayoutData);

		// 8. Define listeners
		setListeners();
	}

	private void setListeners() {
		SystemEventManager.getInstance().addEventHandler(FileSystemEvent.TYPE,
				new FileSystemEventHandler() {

					@Override
					public void onFileSystemEvent(FileSystemEvent event) {
						// Refresh table
						displayFolder(displayedDir);
						// Refresh tree
						tree.refresh();
					}
				});
	}

	private ToolBar buildExplorerToolbar() {

		ToolBar toolBar = new ToolBar();

		// Creates buttons with icons
		Button homeButton = new Button();

		homeButton.setIcon(AbstractImagePrototype.create(ResourcesBundle
				.getBinaryResources().explorer_home_logo()));

		Button parentButton = new Button();
		parentButton.setIcon(AbstractImagePrototype.create(ResourcesBundle
				.getBinaryResources().explorer_up_logo()));

		// Handlers
		homeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				displayFolder(FileSystemManager.getInstance().getRoot());
			}
		});
		parentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (displayedDir == null) {
					return;
				}
				displayFolder(displayedDir.getParent());
			}
		});

		toolBar.add(homeButton);
		toolBar.add(parentButton);

		return toolBar;
	}

	private void displayFolder(Directory dir) {

		displayedDir = dir;
		if (dir == null) {
			return;
		}
		dirTable.removeAll();

		// Sort children (first : directory, and then files)
		List<INode> sortedNodes = new ArrayList<INode>(dir.getChildren()
				.values());
		Collections.sort(sortedNodes,
				ExplorerDisplayDecorator.getFSNodeComparator());

		// Iterate over the node list
		for (INode node : sortedNodes) {
			if (node == null) {
				continue;
			}
			final ExplorerIcon icon = new ExplorerIcon(node);

			// Enable d&d on explorer icon
			enableIconDND(icon);

			icon.addDoubleClickListener(new DoubleClickHandler() {
				@Override
				public void onDoubleClick(DoubleClickEvent event) {

					if (icon.getNode().getNodeType() == NodeType.DIR) {
						displayFolder((Directory) icon.getNode());
					} else {
						CommandInterpreter.getInstance().handleSystemCommand(
								new Command(CommandType.LAUNCH_PROCESS,
										new String[] { "editor",
												icon.getNode().getPath() },
										CommandSource.UI_APP));
					}
				}
			});

			// Add node to the display folder table
			dirTable.add(icon);
		}

		// Refresh the display folder table
		dirTable.layout();

		// Display the current path on the top of the component
		listPanel.setHeading(dir.getPath());
	}

	private void enableIconDND(final ExplorerIcon icon) {

		// ------------------------------
		// 1. DND on folder display view
		// ------------------------------
		new DragSource(icon.getComponent()) {
			@Override
			protected void onDragStart(DNDEvent event) {
				event.setData(icon);
				event.getStatus().update(
						El.fly(icon.getElement()).cloneNode(true));
			}

		};
		new DropTarget(icon.getComponent()) {
			@Override
			protected void onDragDrop(DNDEvent event) {
				ExplorerIcon src = event.getData();
				ExplorerIcon dest = icon;

				if (src == null || dest == null) {
					return;
				}

				CommandInterpreter.getInstance().handleSystemCommand(
						new Command(CommandType.MV_NODE, new String[] {
								src.getNode().getPath(),
								dest.getNode().getPath() },
								CommandSource.UI_APP));
			}
		};

	}

	private void enableTreeDND() {

		// ------------------------------
		// 2. DND between display view and tree and between tree and tree
		// ------------------------------
		new TreePanelDragSource(tree);
		TreePanelDropTarget drop = new TreePanelDropTarget(tree) {
			@Override
			protected void onDragDrop(DNDEvent e) {

				// Stop the event
				e.cancelBubble();
				e.stopEvent();

				// The target node
				TreeNode target = tree.findNode(e.getTarget());

				String srcPath = null;
				// Source node : The node is from directory view
				if (e.getData() instanceof ExplorerIcon) {
					ExplorerIcon source = e.getData();
					srcPath = source.getNode().getPath();
				}
				// Source node : The node is from the tree himself
				else {
					List<Object> data = e.getData();
					ModelData model = ((TreeStoreModel) data.get(0))
							.get("model");
					srcPath = model.get(Constants.FS_TREE_NODE_PATH);
				}

				// Launch the move command
				String destPath = target.getModel().get(
						Constants.FS_TREE_NODE_PATH);
				Command moveCommand = new Command(CommandType.MV_NODE,
						new String[] { srcPath, destPath },
						CommandSource.UI_APP);
				CommandInterpreter.getInstance().handleSystemCommand(
						moveCommand);
			}
		};
		drop.setAllowSelfAsSource(true);
		drop.setFeedback(Feedback.BOTH);
	}

	public ModelData getSelectedNode() {
		return tree.getSelectionModel().getSelectedItem();
	}

	@Override
	public int exit() {
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
