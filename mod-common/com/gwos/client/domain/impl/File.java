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

import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.Constants;
import com.gwos.client.domain.INode;

public class File implements INode, Serializable, Comparable<INode> {

	private static final long serialVersionUID = -5087074663138299416L;

	private String name;
	private Directory parentFolder;
	private FileType fileType;
	private String content;
	private User owner;

	public File() {
		this(null, null, null, null);
	}

	public File(String name, Directory parentFolder) {
		this(name, parentFolder, SessionManager.getInstance().loggedUser(),
				FileType.TEXT);
	}

	public File(String name, Directory parentFolder, User owner,
			FileType fileType) {
		super();
		this.name = name;
		this.fileType = fileType;
		this.parentFolder = parentFolder;
		this.owner = owner;
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
	public NodeType getNodeType() {
		return NodeType.FILE;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void setParent(Directory d) {
		parentFolder = d;
	}

	@Override
	public Directory getParent() {
		return parentFolder;
	}

	@Override
	public String getPath() {
		return parentFolder.getPath() + Constants.PATH_SEPARATOR + getName();
	}

	@Override
	public User getOwner() {
		return owner;
	}

	@Override
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	@Override
	public String toString() {
		String parentFolderName = "undef";
		if (parentFolder != null) {
			parentFolderName = parentFolder.getName();
		}
		return "File [name=" + name + ", (" + getPath() + "), fileType="
				+ fileType + ", parent = " + parentFolderName + ", owner="
				+ owner.getUsername() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((parentFolder == null) ? 0 : parentFolder.hashCode());
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
		File other = (File) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (fileType != other.fileType)
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
		if (parentFolder == null) {
			if (other.parentFolder != null)
				return false;
		} else if (!parentFolder.equals(other.parentFolder))
			return false;
		return true;
	}

	@Override
	public int compareTo(INode o) {

		if (o.getNodeType() == NodeType.DIR) {
			return 1;
		}
		if (o.getNodeType() == NodeType.FILE) {
			return getName().compareTo(o.getName());
		}
		return 10;
	}
}
