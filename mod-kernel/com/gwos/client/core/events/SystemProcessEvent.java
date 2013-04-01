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

package com.gwos.client.core.events;

import com.google.gwt.event.shared.GwtEvent;
import com.gwos.client.domain.impl.SystemProcess;
import com.gwos.client.domain.impl.SystemProcessStatus;

public class SystemProcessEvent extends GwtEvent<SystemProcessEventHandler> {

	public static final Type TYPE = new Type<SystemProcessEventHandler>();

	private SystemProcess process;
	private SystemProcessStatus status;

	public SystemProcessEvent(SystemProcess process, SystemProcessStatus status) {
		this.process = process;
		this.status = status;
	}

	public SystemProcess getProcess() {
		return process;
	}

	public SystemProcessStatus getProcessStatus() {
		return status;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SystemProcessEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SystemProcessEventHandler handler) {
		handler.onSystemProcessEvent(this);
	}

	@Override
	public String toString() {
		return "ProcessEvent [process=" + process + ", status=" + status + "]";
	}
}
