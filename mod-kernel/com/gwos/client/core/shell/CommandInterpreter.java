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

package com.gwos.client.core.shell;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.constants.Strings;
import com.gwos.client.core.events.FileSystemEvent;
import com.gwos.client.core.events.FileSystemEventType;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemMessageEvent;
import com.gwos.client.core.events.SystemMessageType;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.core.process.SystemProcessManager;
import com.gwos.client.core.shell.decorators.CommandResultDecorator;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.SystemProcessPriority;
import com.gwos.client.domain.impl.User;
import com.gwos.client.packets.AppsManager;
import com.gwos.client.rights.SystemRightsController;
import com.gwos.client.services.ServiceBundle;

public class CommandInterpreter {

	public static final String _EMPTY_RETURN = "";
	private static CommandInterpreter _INSTANCE;
	private static final Strings _STRINGS_BUNDLE = ResourcesBundle.getStrings();

	private CommandInterpreter() {
		super();
	}

	public static CommandInterpreter getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new CommandInterpreter();
		}
		return _INSTANCE;
	}

	public String handleSystemCommand(Command command) {

		// The command initiator
		User commandOwner = SessionManager.getInstance().loggedUser();
		if (commandOwner == null) {
			sendSystemMessage(SystemMessageType.RIGHTS_VIOLATION,
					_STRINGS_BUNDLE.systemRightViolationMessage(),
					command.getCommandSource());
			return _STRINGS_BUNDLE.systemRightViolationMessage();
		}

		// Check if the user had permission to execute the command
		if (SystemRightsController.check(command, commandOwner) == false) {
			// Launch system message
			sendSystemMessage(SystemMessageType.RIGHTS_VIOLATION,
					_STRINGS_BUNDLE.systemRightViolationMessage(),
					command.getCommandSource());
			return _STRINGS_BUNDLE.systemRightViolationMessage();
		}

		switch (command.getCommandType()) {

		// Process Commands
		case LAUNCH_PROCESS:
			Application app = AppsManager.getInstance().getApp(
					command.getParams()[0]);
			if (null == app) {
				sendSystemMessage(SystemMessageType.INVALID_ARG,
						"Application \"" + command.getParams()[0]
								+ "\" not found !", command.getCommandSource());
				return _STRINGS_BUNDLE.badArgumentMessage();
			}
			// Check if the user has enought rights to execute the application
			if (app.isEnoughtRights(commandOwner.getGroup()) == false) {
				sendSystemMessage(SystemMessageType.RIGHTS_VIOLATION,
						_STRINGS_BUNDLE.systemRightViolationMessage(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.systemRightViolationMessage();
			}
			// Start the app
			app.setParams(command.getParams());
			SystemProcessManager.getInstance().startProcess(
					SystemProcessPriority.MEDIUM, app);
			return _EMPTY_RETURN;
		case KILL_PROCESS:
			try {
				int pid = Integer.parseInt(command.getParams()[0]);
				SystemProcessManager.getInstance().killProcess(pid);
			} catch (NumberFormatException e) {
				sendSystemMessage(SystemMessageType.INVALID_ARG,
						e.getMessage(), command.getCommandSource());
			}
			return _EMPTY_RETURN;
		case KILL_ALL_PROCESSES:
			SystemProcessManager.getInstance().killAllProcesses();
			return _EMPTY_RETURN;

			// FS Commands
		case LIST_DIR:
			String path = command.getParams()[0];
			List<INode> nodeList = FileSystemManager.getInstance().ls(path);
			if (nodeList == null) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsMissingDir(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.fsMissingDir();
			}
			return CommandResultDecorator.getFSDirListDecorator(nodeList);
		case CHANGE_DIRECTORY:
			path = command.getParams()[0];
			if (FileSystemManager.getInstance().cd(path) == null) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsMissingDir(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.fsMissingDir();
			}
			return _EMPTY_RETURN;
		case CURRENT_PATH:
			Directory dir = FileSystemManager.getInstance().pwd();
			if (dir != null) {
				return dir.getPath();
			}
			sendSystemMessage(SystemMessageType.FS_ERROR,
					_STRINGS_BUNDLE.fsError(), command.getCommandSource());
			return _STRINGS_BUNDLE.fsError();
		case MAKE_FILE:
			path = command.getParams()[0];
			if (FileSystemManager.getInstance().touch(path) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			return _EMPTY_RETURN;
		case RM_NODE:
			path = command.getParams()[0];
			if (FileSystemManager.getInstance().rm(path) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			return _EMPTY_RETURN;
		case RENAME_NODE:
			path = command.getParams()[0];
			String name = command.getParams()[1];
			if (FileSystemManager.getInstance().rename(path, name) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			return _EMPTY_RETURN;
		case CAT_FILE:
			path = command.getParams()[0];
			String result = FileSystemManager.getInstance().cat(path);
			if (null == result) {
				return _EMPTY_RETURN;
			}
			return result;
		case MAKE_DIR:
			path = command.getParams()[0];
			if (FileSystemManager.getInstance().mkdir(path) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			return _EMPTY_RETURN;
		case TREE_DIR:
			path = command.getParams()[0];
			if (path == null) {
				path = ".";
			}
			dir = FileSystemManager.getInstance().getDirectory(path);
			if (dir == null) {
				sendSystemMessage(SystemMessageType.INVALID_ARG,
						_STRINGS_BUNDLE.badArgumentMessage(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.badArgumentMessage();
			}
			return CommandResultDecorator.getFSTreeDecorator(dir);
		case MV_NODE:
			String src = command.getParams()[0];
			String dest = command.getParams()[1];
			if (src == null || dest == null) {
				sendSystemMessage(SystemMessageType.INVALID_ARG,
						_STRINGS_BUNDLE.badArgumentMessage(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.badArgumentMessage();
			}
			if (FileSystemManager.getInstance().mv(src, dest) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			SystemEventManager.getInstance()
					.fireSystemEvent(
							new FileSystemEvent(FileSystemEventType.MV_NODE,
									null, null));
			return _EMPTY_RETURN;
		case CP_NODE:
			src = command.getParams()[0];
			dest = command.getParams()[1];
			if (src == null || dest == null) {
				sendSystemMessage(SystemMessageType.INVALID_ARG,
						_STRINGS_BUNDLE.badArgumentMessage(),
						command.getCommandSource());
				return _STRINGS_BUNDLE.badArgumentMessage();
			}
			if (FileSystemManager.getInstance().cp(src, dest) == false) {
				sendSystemMessage(SystemMessageType.FS_ERROR,
						_STRINGS_BUNDLE.fsError(), command.getCommandSource());
				return _STRINGS_BUNDLE.fsError();
			}
			return _EMPTY_RETURN;

			// Session
		case USER_CREATE:
			String username = command.getParams()[0];
			String password = command.getParams()[1];
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);

			// For asynchronous answer, a system message is sent
			ServiceBundle.getInstance().getSessionService()
					.addUser(user, new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							// TODO
							SystemEventManager.getInstance().fireSystemEvent(
									new SystemMessageEvent(null, "", null));
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO
							SystemEventManager.getInstance().fireSystemEvent(
									new SystemMessageEvent(null, "", null));
						}
					});
			return _EMPTY_RETURN;
		case USER_DELETE:
			username = command.getParams()[0];
			// TODO
			break;
		case USER_CHANGE_PASSWORD:
			// TODO
			break;
		case USER_DISCONNECT:
			// TODO
			break;
		}
		return null;
	}

	private void sendSystemMessage(SystemMessageType type, String message,
			CommandSource source) {
		SystemEventManager.getInstance().fireSystemEvent(
				new SystemMessageEvent(type, message, source));
	}
}
