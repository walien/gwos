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

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;

public class LogoutEvent extends GwtEvent<LogoutEventHandler> {

	public enum LogoutState {
		START, END
	};

	public static final Type TYPE = new Type<LogoutEventHandler>();

	private LogoutState state;
	private Date date;

	public LogoutEvent(LogoutState state) {
		this.state = state;
		date = new Date();
	}

	public LogoutState getState() {
		return state;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LogoutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogoutEventHandler handler) {
		handler.onLogoutEvent(this);
	}

}
