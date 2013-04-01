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

package com.gwos.client.domain;

import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.NodeType;
import com.gwos.client.domain.impl.User;

public interface INode {

	String getName();

	void setName(String name);

	void setParent(Directory d);

	Directory getParent();

	NodeType getNodeType();

	String getPath();

	User getOwner();

	void setOwner(User owner);
}
