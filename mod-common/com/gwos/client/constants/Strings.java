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

import com.google.gwt.i18n.client.Constants;

public interface Strings extends Constants {

	// ------------------------------------
	// -------------- GENERAL -------------
	// ------------------------------------

	@DefaultStringValue("GwOS")
	String appTitle();

	@DefaultStringValue("by Walien <eoriou@gmail.com>")
	String appAuthor();

	@DefaultStringValue("1.0")
	String appVersion();

	// ------------------------------------
	// ----------- FS MESSAGES-------------
	// ------------------------------------

	String fsMissingDir();

	String fsError();

	// ------------------------------------
	// ------------- SYSTEM ---------------
	// ------------------------------------

	String systemRightViolationMessage();

	String unknownCommandOwner();

	String badArgumentMessage();

	String unrecognizableCommand();

	// ------------------------------------
	// ----------- UI : AUTH --------------
	// ------------------------------------

	String authenticationPanelTitle();

	String usernameFieldLabel();

	String passwordFieldLabel();

	String logButtonLabel();

	String accessGrantedTitle();

	String accessGrantedLabel();

	String accessRefusedTitle();

	String accessRefusedLabel();

	// ------------------------------------
	// ----------- UI : DESKTOP -----------
	// ------------------------------------

	String logoutButton();

	String settingsButton();

	String loginBoxTitle();

	String loginBoxMessage();

	String logoutBoxTitle();

	String logoutBoxMessage();

	// ------------------------------------
	// ---------UI : CONTEXT MENUS---------
	// ------------------------------------

	String alignOnGrid();

	String customizeDesktop();

	String restoreApp();

	String closeApp();

	String renameExplorerNode();

	String deleteExplorerNode();

	// ------------------------------------
	// ----------- UI : APPS --------------
	// ------------------------------------

	// File editor app

	String editorOpenFile();

	String editorSaveFile();

	// Process manager app

	String processIDColumnLabel();

	String processNameColumnLabel();

	String processOwnerColumnLabel();

	String processPriorityColumnLabel();

	String killProcessButtonLabel();

	// Admin App

	String deleteUser();
}
