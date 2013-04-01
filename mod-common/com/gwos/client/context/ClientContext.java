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

package com.gwos.client.context;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

public class ClientContext {

	private static ClientContext _INSTANCE;

	private int browserWidth;
	private int browserHeight;

	public static Object _CLIPBOARD_OBJECT;

	private ClientContext() {
		browserWidth = Window.getClientWidth();
		browserHeight = Window.getClientHeight() - 30;

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				browserWidth = event.getWidth();
				browserHeight = event.getHeight();
			}
		});
	}

	public static ClientContext getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new ClientContext();
		}
		return _INSTANCE;
	}

	public int getBrowserWidth() {
		return browserWidth;
	}

	public int getBrowserHeight() {
		return browserHeight;
	}

}
