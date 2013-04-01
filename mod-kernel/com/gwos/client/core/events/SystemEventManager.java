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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;

public class SystemEventManager {

	private static SystemEventManager _INSTANCE;

	private HandlerManager handlerManager;

	private SystemEventManager() {
		handlerManager = new HandlerManager(this);
	}

	public static SystemEventManager getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new SystemEventManager();
		}
		return _INSTANCE;
	}

	public void fireSystemEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public void addEventHandler(Type<EventHandler> eventType,
			EventHandler handler) {
		handlerManager.addHandler(eventType, handler);
	}
}
