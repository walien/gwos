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

package com.gwos.client.apps.widgets;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.gwos.client.apps.tools.ExplorerDisplayDecorator;
import com.gwos.client.constants.Constants;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.NodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FSTreeWidget extends TreePanel<ModelData> {

	private boolean showFiles;

	public FSTreeWidget(boolean showFiles) {
		super(new TreeStore<ModelData>());
		this.showFiles = showFiles;
		init();
		render();
	}

	private void init() {

		setTrackMouseOver(false);
		setCheckStyle(CheckCascade.CHILDREN);
		setDisplayProperty(Constants.FS_TREE_NODE_NAME);

		store.setKeyProvider(new ModelKeyProvider<ModelData>() {
			public String getKey(ModelData model) {
				return model.get(Constants.FS_TREE_NODE_PATH);
			}
		});

		getStyle().setLeafIcon(
				AbstractImagePrototype.create(ResourcesBundle
						.getBinaryResources().file_logo()));
	}

	private void render() {

		// Fill the tree with FS elements
		ModelData m = createNodeModel(FileSystemManager.getInstance().getRoot());
		store.add(m, false);

		// Expand listener
		addListener(Events.BeforeExpand,
				new Listener<TreePanelEvent<ModelData>>() {

					public void handleEvent(TreePanelEvent<ModelData> be) {
						if (be.getNode().getItemCount() != 0) {
							return;
						}

						// Add children to the node
						Map<String, INode> children = FileSystemManager
								.getInstance()
								.getDirectory(
										(String) be
												.getNode()
												.getModel()
												.get(Constants.FS_TREE_NODE_PATH))
								.getChildren();
						if (children == null || children.isEmpty()) {
							return;
						}
						List<ModelData> list = new ArrayList<ModelData>(0);
						for (INode n : children.values()) {
							if (showFiles == false
									&& n.getNodeType() != NodeType.DIR) {
								continue;
							}
							list.add(createNodeModel(n));
						}

						// Sort the list
						Collections.sort(list, ExplorerDisplayDecorator
								.getDisplayTreeNodeComparator());

						// Add filtered elements to the store
						store.insert(be.getNode().getModel(), list, 0, true);
					}
				});

		getSelectionModel().select(store.getRootItems(), true);
		expandAll();
	}

	public void refresh() {
		store.removeAll();
		render();
		recalculate();
	}

	@Override
	protected boolean hasChildren(ModelData model) {
		return model.<Boolean>get(Constants.FS_TREE_NODE_IS_DIR);
	}

	private ModelData createNodeModel(INode node) {
		ModelData m = new BaseModelData();
		m.set(Constants.FS_TREE_NODE_PATH, node.getPath());
		m.set(Constants.FS_TREE_NODE_NAME, node.getName());
		m.set(Constants.FS_TREE_NODE_IS_DIR, node.getNodeType() == NodeType.DIR);
		return m;
	}
}
