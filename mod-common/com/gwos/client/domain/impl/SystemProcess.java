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

package com.gwos.client.domain.impl;

import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.Constants;

public class SystemProcess {

	private static int _PID = Constants.MIN_PID_VALUE;

	private int pid;
	private String name;
	private Application app;
	private SystemProcessPriority priority;
	private User owner;

	public SystemProcess(String name, Application app,
			SystemProcessPriority priority, User owner) {
		this.pid = ++_PID;
		this.name = name;
		this.app = app;
		this.priority = priority;
		this.owner = owner;
	}

	public SystemProcess(String name, Application app) {
		this(name, app, SystemProcessPriority.LOW, SessionManager.getInstance()
				.loggedUser());
	}

	public int getPID() {
		return pid;
	}

	public String getName() {
		return name;
	}

	public Application getApplication() {
		return app;
	}

	public SystemProcessPriority getPriority() {
		return priority;
	}

	public User getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "SystemProcess [pid=" + pid + ", name=" + name + ", app=" + app
				+ ", priority=" + priority + ", owner=" + owner + "]";
	}

}
