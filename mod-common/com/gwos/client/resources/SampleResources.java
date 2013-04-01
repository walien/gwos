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

package com.gwos.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface SampleResources extends ClientBundle {

	// DESKTOP

	@Source("computer.png")
	ImageResource computer_logo();

	@Source("shell.png")
	ImageResource shell_logo();

	@Source("processes.png")
	ImageResource processes_logo();

	@Source("webbrowser.png")
	ImageResource web_browser_logo();

	@Source("home.png")
	ImageResource home_logo();

	@Source("users.png")
	ImageResource users_logo();

	@Source("tools.png")
	ImageResource tools_logo();

	// APPS

	// FILE EXPLORER
	@Source("file.png")
	ImageResource file_logo();

	@Source("file_large.png")
	ImageResource file_large_logo();

	@Source("folder_large.png")
	ImageResource folder_large_logo();

	@Source("explorer_home.png")
	ImageResource explorer_home_logo();

	@Source("explorer_up.png")
	ImageResource explorer_up_logo();

	// FILE EDITOR
	@Source("editor.png")
	ImageResource editor_logo();

	@Source("openFile.png")
	ImageResource openFile_logo();

	@Source("saveFile.png")
	ImageResource saveFile_logo();

}
