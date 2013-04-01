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

import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwos.client.context.ClientContext;

public class Constants {

	// //////////////////////////
	// ////// SYSTEM CONSTANTS
	// //////////////////////////

	public static final int TIME_REFRESHING_PERIOD = 10000;

	public static final DateTimeFormat TIME_FORMAT = DateTimeFormat
			.getFormat("HH:mm");

	// //////////////////////////
	// ////// PROCESSES CONSTANTS
	// //////////////////////////

	public static final int MIN_PID_VALUE = 999;

	// //////////////////////////
	// ////// FS CONSTANTS
	// //////////////////////////

	public static final String HIDDEN_FILE_PREFIX = ".";

	public static final String PATH_SEPARATOR = "/";

	public static final String CURRENT_DIR_PATH = ".";

	public static final String PARENT_DIR_PATH = "..";

	public static final String DIR_NAME_PATTERN = "^(\\p{Alpha})+(p{Alnum})*$";

	public static final String FILE_NAME_PATTERN = "^(\\p{Alpha})+(p{Alnum})*[.]{1}(p{Alnum})+$";

	// /////////////////////////////////
	// ////// EXPLORER APP CONSTANTS
	// /////////////////////////////////

	public static final String FS_TREE_NODE_PATH = "path";

	public static final String FS_TREE_NODE_NAME = "name";

	public static final String FS_TREE_NODE_IS_DIR = "isDir";

	// //////////////////////////
	// ////// SHELL CONSTANTS
	// //////////////////////////

	public static final String SHELL_HEAD_FILE_PATH = "/settings/shellHead.cfg";

	public static final String GWOS_PROMPT = "gwos:";

	public static final String GWOS_PROMPT_SEPARATOR = "@";

	public static final String USER_PROMPT_SUFFIX = "$";

	public static final String ROOT_PROMPT_SUFFIX = "#";

	// //////////////////////////
	// ////// UI CONSTANTS
	// //////////////////////////

	public static final int TASK_BAR_SIZE = 35;

	public static final int TASK_BAR_WIDTH_OFFSET = ClientContext.getInstance()
			.getBrowserWidth() - 20;

	public static final int TASK_BAR_HEIGHT_OFFSET = TASK_BAR_SIZE + 20;

	public static final int DESKTOP_HOUR_SIZE = 12;

	public static final int _DESKTOP_ICONS_X_START = 20;

	public static final int _DESKTOP_ICONS_Y_START = 20;

	public static final int _DESKTOP_ICONS_X_SPACE = 100;

	public static final int _DESKTOP_ICONS_Y_SPACE = 80;

}
