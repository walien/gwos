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
import com.gwos.client.core.shell.CommandSource;

public class SystemMessageEvent extends GwtEvent<SystemMessageHandler> {

	public static final Type TYPE = new Type<SystemMessageHandler>();
	private SystemMessageType messageType;
	private String message;
	private CommandSource source;

	public SystemMessageEvent(SystemMessageType messageType, String message,
			CommandSource source) {
		this.messageType = messageType;
		this.message = message;
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public SystemMessageType getMessageType() {
		return messageType;
	}

	public CommandSource getSource() {
		return source;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SystemMessageHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SystemMessageHandler handler) {
		handler.onSystemMessageEvent(this);
	}

	@Override
	public String toString() {
		return "SystemMessageEvent [message=" + message + "]";
	}
}
