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

package com.gwos.client.domain.impl.models;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.gwos.client.domain.impl.UserGroup;

public class UserGroupModel extends BaseModel {

	private static final long serialVersionUID = -1792906082271786419L;
	private UserGroup group;

	public UserGroupModel(UserGroup group) {
		set("name", group.name());
		this.group = group;
	}

	public UserGroup getGroup() {
		return group;
	}

}
