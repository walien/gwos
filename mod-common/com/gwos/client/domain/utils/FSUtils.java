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

package com.gwos.client.domain.utils;

import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.File;

public class FSUtils {

	public static INode cloneNode(INode node) {

		switch (node.getNodeType()) {
		case DIR:
			return FSUtils.cloneDir((Directory) node);
		case FILE:
			return FSUtils.cloneFile((File) node);
		}

		return null;
	}

	public static File cloneFile(File f) {
		File newFile = new File(f.getName(), null, f.getOwner(),
				f.getFileType());
		newFile.setContent(f.getContent());
		return newFile;
	}

	public static Directory cloneDir(Directory dir) {

		Directory newDir = new Directory();
		newDir.setName(dir.getName());
		newDir.setOwner(dir.getOwner());

		for (INode node : dir.getChildren().values()) {
			switch (node.getNodeType()) {
			case DIR:
				Directory childDir = cloneDir((Directory) node);
				childDir.setParent(newDir);
				newDir.addChildNode(childDir);
			case FILE:
				File childFile = cloneFile((File) node);
				childFile.setParent(newDir);
				newDir.addChildNode(childFile);
			}
		}

		return newDir;
	}
}
