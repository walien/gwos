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

package com.gwos.client.domain.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.Constants;
import com.gwos.client.domain.INode;

public class Directory implements INode, Serializable, Comparable<INode> {

	private static final long serialVersionUID = 5381003375493794040L;

	private String name;
	private Directory parentDir;
	private Map<String, INode> children;
	private User owner;

	public Directory() {
		this(null, null, null);
	}

	public Directory(String name, Directory parentDir, User owner) {
		super();
		this.name = name;
		this.parentDir = parentDir;
		this.children = new HashMap<String, INode>();
	}

	public Directory(String name, Directory parentDir) {
		this(name, parentDir, SessionManager.getInstance().loggedUser());
	}

	public Map<String, INode> getChildren() {
		return children;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setParent(Directory d) {
		parentDir = d;
	}

	@Override
	public Directory getParent() {
		return parentDir;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.DIR;
	}

	@Override
	public String getPath() {
		if (isRoot()) {
			return name;
		}
		if (parentDir.isRoot()) {
			return parentDir.getPath() + getName();
		}
		return parentDir.getPath() + Constants.PATH_SEPARATOR + getName();
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isRoot() {
		return parentDir == null;
	}

	public void addChildNode(INode node) {
		children.put(node.getName(), node);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((parentDir == null) ? 0 : parentDir.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Directory other = (Directory) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (parentDir == null) {
			if (other.parentDir != null)
				return false;
		} else if (!parentDir.equals(other.parentDir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String own = "undef";
		String dir = "undef";
		if (owner != null) {
			own = owner.getUsername();
		}
		if (parentDir != null) {
			dir = parentDir.getName();
		}
		return "Directory [name=" + name + ", (" + getPath() + "), parentDir="
				+ dir + ", owner=" + own + "children = " + children + "]";
	}

	@Override
	public int compareTo(INode o) {

		if (o.getNodeType() == NodeType.FILE) {
			return -1;
		}
		if (o.getNodeType() == NodeType.DIR) {
			return getName().compareTo(o.getName());
		}
		return 10;
	}
}
