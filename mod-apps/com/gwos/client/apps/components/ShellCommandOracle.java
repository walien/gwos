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

import java.util.ArrayList;
import java.util.List;

import com.gwos.client.core.filesystem.FileSystemManager;

public class ShellCommandOracle {

	private static final List<String> _MATCHES = new ArrayList<String>();

	private static int _COUNTER = 0;

	public static String divineCommand(String command) {

		// /////////////////////////////////////////////
		// 1. Navigation into the match list with TAB
		// /////////////////////////////////////////////

		if (_MATCHES.contains(command)) {
			return _MATCHES.get((_COUNTER++) % _MATCHES.size());
		}

		// /////////////////////////////////////////////
		// 2. First occurence match
		// /////////////////////////////////////////////

		// 2.1 Explore into pwd children
		_MATCHES.clear();
		for (String children : FileSystemManager.getInstance().pwd()
				.getChildren().keySet()) {
			if (children.startsWith(command)) {
				_MATCHES.add(children);
			}
		}
		if (_MATCHES.size() > 0) {
			return _MATCHES.get(0);
		}
		// 2.2 Explorer shell commands

		return "";
	}

}
