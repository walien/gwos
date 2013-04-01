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

package com.gwos.client.core.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gwos.client.auth.SessionManager;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemProcessEvent;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.SystemProcess;
import com.gwos.client.domain.impl.SystemProcessPriority;
import com.gwos.client.domain.impl.SystemProcessStatus;

public class SystemProcessManager {

	private static SystemProcessManager _INSTANCE;
	private Map<Integer, SystemProcess> processes;

	private SystemProcessManager() {
		processes = new HashMap<Integer, SystemProcess>();
	}

	public static SystemProcessManager getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new SystemProcessManager();
		}
		return _INSTANCE;
	}

	public Map<Integer, SystemProcess> getProcesses() {
		return processes;
	}

	/**
	 * Starts a process with a priority (LOW, MEDIUM, HIGH) which integrate the
	 * specified application
	 * 
	 * @param priority
	 * @param app
	 * @return
	 */

	public int startProcess(SystemProcessPriority priority, Application app) {
		SystemProcess proc = new SystemProcess(app.getName(), app, priority,
				SessionManager.getInstance().loggedUser());
		processes.put(proc.getPID(), proc);
		app.start(proc.getPID());
		SystemEventManager.getInstance().fireSystemEvent(
				new SystemProcessEvent(proc, SystemProcessStatus.STARTED));
		return proc.getPID();
	}

	/**
	 * Kills the process with the pid specified in parameter
	 * 
	 * @param pid
	 */

	public void killProcess(int pid) {
		SystemProcess proc = processes.get(pid);
		if (proc != null && proc.getApplication() != null) {
			proc.getApplication().exit();
		}
		SystemEventManager.getInstance().fireSystemEvent(
				new SystemProcessEvent(proc, SystemProcessStatus.KILLED));

		processes.remove(pid);
	}

	/**
	 * Kill a processes
	 */

	public void killAllProcesses() {
		// Iterate on a copy of the set in order to avoid concurrent
		// modification exception (ugly but functional)
		Set<Integer> pids = new HashSet<Integer>(processes.keySet());
		for (int pid : pids) {
			killProcess(pid);
		}
	}
}
