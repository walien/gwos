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

package com.gwos.client.apps.components;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.gwos.client.domain.impl.SystemProcess;

public class SystemProcessGridModel extends BaseModel {

	private static final long serialVersionUID = 1L;
	private SystemProcess process;

	public SystemProcessGridModel(SystemProcess process) {
		this.process = process;

		set("pid", process.getPID());
		set("name", process.getName());
		set("owner", process.getOwner().getUsername());
		set("priority", process.getPriority());
	}

	@Override
	public String toString() {
		return "SystemProcessGridModel [process=" + process + "]";
	}
}
