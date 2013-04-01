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

package com.gwos.client.apps.tools;

import com.extjs.gxt.ui.client.data.ModelData;
import com.gwos.client.constants.Constants;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.NodeType;

import java.util.Comparator;

public class ExplorerDisplayDecorator {

	public static final Comparator<INode> getFSNodeComparator() {
		return new Comparator<INode>() {
			@Override
			public int compare(INode o1, INode o2) {
				if (o1.getNodeType() == NodeType.DIR
						&& o2.getNodeType() != NodeType.DIR) {
					return -1;
				}
				if (o1.getNodeType() == NodeType.FILE
						&& o2.getNodeType() != NodeType.FILE) {
					return 1;
				}
				if (o1.getNodeType() == o2.getNodeType()) {
					return o1.getName().compareTo(o2.getName());
				}
				return 0;
			}
		};
	}

	public static final Comparator<ModelData> getDisplayTreeNodeComparator() {
		return new Comparator<ModelData>() {
			@Override
			public int compare(ModelData o1, ModelData o2) {

				boolean o1IsDir = o1.<Boolean>get(Constants.FS_TREE_NODE_IS_DIR);
				boolean o2IsDir = o2.<Boolean>get(Constants.FS_TREE_NODE_IS_DIR);

				if (o1IsDir == true && o2IsDir == false) {
					return -1;
				}
				if (o1IsDir == false && o2IsDir == true) {
					return 1;
				}
				if (o1IsDir == true && o2IsDir == true) {
					return o1
							.get(Constants.FS_TREE_NODE_NAME)
							.toString()
							.compareTo(
									o2.get(Constants.FS_TREE_NODE_NAME)
											.toString());
				}
				return 0;
			}
		};
	}
}
