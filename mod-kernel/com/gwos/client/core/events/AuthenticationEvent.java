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
import com.gwos.client.domain.impl.User;

public class AuthenticationEvent extends GwtEvent<AuthenticationEventHandler> {

	public static final Type TYPE = new Type<AuthenticationEventHandler>();

	private User user;
	private Date date;

	public AuthenticationEvent(User user) {
		this.user = user;
		this.date = new Date();
	}

	public User getUser() {
		return user;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public Type getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AuthenticationEventHandler handler) {
		handler.onAuthenticate(this);
	}

	@Override
	public String toString() {
		return "AuthenticationEvent [user=" + user + ", date=" + date + "]";
	}

}
