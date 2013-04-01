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

package com.gwos.client.rights;

import com.gwos.client.core.shell.Command;
import com.gwos.client.domain.impl.User;
import com.gwos.client.domain.impl.UserGroup;

public class SystemRightsController {

	public static boolean check(Command command, User user) {
		if (user.getGroup() == UserGroup.ROOT
				|| user.getGroup() == UserGroup.ADMIN) {
			return true;
		}
		if (user.getGroup() == UserGroup.USER) {
			switch (command.getCommandType()) {
			case CAT_FILE:
			case CHANGE_DIRECTORY:
			case CURRENT_PATH:
			case KILL_ALL_PROCESSES:
			case KILL_PROCESS:
			case LAUNCH_PROCESS:
			case LIST_DIR:
			case TREE_DIR:
			case USER_DISCONNECT:
				return true;
			default:
				return false;
			}
		}
		return false;
	}
}
