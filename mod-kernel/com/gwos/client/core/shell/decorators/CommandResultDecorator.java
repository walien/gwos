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

package com.gwos.client.core.shell.decorators;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.NodeType;
import com.gwos.client.domain.impl.SystemProcess;

public class CommandResultDecorator {

	private static int _DEEP = 0;

	// /////////////////////////////
	// // FS
	// /////////////////////////////

	public static String getFSDirListDecorator(List<INode> nodes) {
		return listDir(nodes);
	}

	public static String getFSTreeDecorator(Directory dir) {
		String result = tree(dir);
		_DEEP = 0;
		return result;
	}

	private static String listDir(List<INode> nodes) {
		StringBuilder result = new StringBuilder();

		INode[] sortedArray = nodes.toArray(new INode[] {});
		Arrays.sort(sortedArray);
		List<INode> sortedList = Arrays.asList(sortedArray);

		for (INode node : sortedList) {
			if (node.getNodeType() == NodeType.DIR) {
				result.append("(DIR) - " + node.getName() + "\n");
			} else {
				result.append("(FIL) - " + node.getName() + "\n");
			}
		}

		return result.toString();
	}

	private static String tree(Directory rootDir) {
		if (rootDir == null) {
			// Error
			return null;
		}
		String text = "(DIR) |_ " + rootDir.getName() + "\n";
		_DEEP += 1;
		for (INode node : rootDir.getChildren().values()) {
			for (int i = 0; i < _DEEP; i++) {
				text += "\t";
			}
			if (node.getNodeType() == NodeType.DIR) {
				Directory dir = (Directory) node;
				if (dir.getChildren().size() > 0) {
					text += tree(dir);
					_DEEP -= 1;
				} else {
					text += "(DIR) - " + dir.getName() + "\n";
				}
			} else {
				text += "(FIL) - " + node.getName() + "\n";
			}
		}
		return text;
	}

	// /////////////////////////////
	// // PROCESSES
	// /////////////////////////////

	public static String getProcessesListDecorator(
			Map<Integer, SystemProcess> processes) {

		StringBuilder result = new StringBuilder();
		int i = 0;
		for (SystemProcess process : processes.values()) {
			result.append(process.getPID());
			result.append(" - " + process.getName());
			result.append(" " + process.getPriority());
			result.append(" " + process.getOwner().getUsername());
			if (i < processes.size() - 1) {
				result.append("\n");
			}
		}
		return result.toString();
	}

	// /////////////////////////////
	// // DOCUMENTATION
	// /////////////////////////////

	public static String getHelpDecorator(Map<String, String> help) {
		StringBuilder result = new StringBuilder();
		result.append("---------------------------------------------------------------------\n");
		for (String comm : help.keySet()) {
			result.append(comm + " : " + help.get(comm) + "\n");
		}
		result.append("---------------------------------------------------------------------\n");
		return result.toString();
	}

	public static String getHelpDecorator(String help, String cmd) {
		StringBuilder result = new StringBuilder();
		result.append("--------------------Documentation for command : \""
				+ cmd + "\"--------------------\n");
		result.append(help);
		result.append("---------------------------------------------------------------------\n");
		return result.toString();
	}

}
