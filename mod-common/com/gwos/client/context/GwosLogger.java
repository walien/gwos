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

import java.util.logging.Logger;

public class GwosLogger {

	private static Logger _LOGGER;

	private GwosLogger() {

	}

	public static Logger getLogger() {
		if (null == _LOGGER) {
			_LOGGER = Logger.getLogger("gwos");
		}
		return _LOGGER;
	}
}
