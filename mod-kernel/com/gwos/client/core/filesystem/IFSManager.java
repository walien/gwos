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

package com.gwos.client.core.filesystem;

import java.util.List;

import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Directory;

public interface IFSManager {
	
	Directory cd(String path);

	Directory pwd();

	boolean mkdir(String path);

	boolean touch(String path);

	boolean rm(String path);

	boolean mv(String src, String dest);
	
	boolean rename(String path, String newName);

	boolean cp(String src, String dest);

	List<INode> ls(String path);

	String cat(String path);
}
