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

package com.gwos.client.constants;

import com.google.gwt.core.client.GWT;
import com.gwos.client.resources.SampleResources;

public class ResourcesBundle {

	private static final Strings _UI_STRINGS = GWT.create(Strings.class);

	private static final SampleResources BINARY_RESOURCES = GWT
			.create(SampleResources.class);

	public static Strings getStrings() {
		return _UI_STRINGS;
	}

	public static SampleResources getBinaryResources() {
		return BINARY_RESOURCES;
	}
}
