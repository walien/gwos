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

public class AppUIEvent extends GwtEvent<AppUIEventHandler> {

	public static final Type TYPE = new Type<AppUIEventHandler>();

	private AppUIEventType type;
	private SystemProcess process;

	public AppUIEvent(AppUIEventType type, SystemProcess process) {
		this.type = type;
		this.process = process;
	}

	public AppUIEventType getUIEventType() {
		return type;
	}

	public SystemProcess getProcess() {
		return process;
	}

	@Override
	protected void dispatch(AppUIEventHandler handler) {
		handler.onAppUIEvent(this);

	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppUIEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return "AppUIEvent [type=" + type + ", process=" + process + "]";
	}
}
