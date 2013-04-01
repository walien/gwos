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

package com.gwos.client.core.shell;

import com.gwos.client.auth.SessionManager;
import com.gwos.client.domain.impl.User;

public class Command {

	private CommandType type;
	private CommandSource source;
	private String[] params;
	private User owner;

	// The first parameter is the name of the command
	public Command(CommandType type, String[] params, User owner,
			CommandSource source) {
		this.type = type;
		this.params = params;
		this.owner = owner;
		this.source = source;
	}

	public Command(CommandType type, String[] params, CommandSource source) {
		this(type, params, SessionManager.getInstance().loggedUser(), source);
	}

	public CommandType getCommandType() {
		return type;
	}

	public String[] getParams() {
		return params;
	}

	public User getOwner() {
		return owner;
	}
	
	public CommandSource getCommandSource(){
		return source;
	}
}
