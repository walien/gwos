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

package com.gwos.client.apps;

import java.util.LinkedList;
import java.util.Map;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.apps.components.ShellCommandOracle;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.Constants;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.constants.Strings;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.core.process.SystemProcessManager;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.core.shell.ShellCommandsBundle;
import com.gwos.client.core.shell.decorators.CommandResultDecorator;
import com.gwos.client.doc.shell.ShellCommandHelpBundle;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.client.packets.AppsManager;

public class ShellApp extends Application {

	private static final CommandInterpreter _INTERPRETER = CommandInterpreter
			.getInstance();
	private static final Strings _STRINGS_BUNDLE = ResourcesBundle.getStrings();
	private static final char _NEW_LINE_SEPARATOR = '\n';

	private String name;
	private int version;
	private String author;

	private TextArea area;
	private static final LinkedList<String> COMMAND_HISTORY = new LinkedList<String>();
	private static int HISTORY_COUNTER = 0;

	public ShellApp() {
		this.name = "Shell";
		this.version = 1;
		this.author = "walien";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public Widget getWidget() {
		return area;
	}

	@Override
	public int getParentWidth() {
		return 500;
	}

	@Override
	public int getParentHeight() {
		return 300;
	}

	@Override
	public void setParams(String[] params) {
	}

	@Override
	public void start(int pid) {

		// render
		area = new TextArea();
		area.focus();

		// Set listeners
		setTextAreaListeners();

		// Initialize the shell text with the head text (
		String headText = FileSystemManager.getInstance().cat(
				Constants.SHELL_HEAD_FILE_PATH);
		appendText(headText);

		// Initialize the field with the prompt text
		appendText(getPrompt());

		// Set focus and disable spell checking
		if (area.isRendered()) {
			area.focus();
			area.setCursorPos(getRelativeLineStartCursorPos()
					+ getPrompt().length());
		}
		area.setStyleAttribute("spellcheck", "false");
	}

	@Override
	public int exit() {
		return 0;
	}

	/**
	 * Handles shell command
	 * 
	 * @param line
	 */

	private void handleCommand(String line) {

		String[] args = line.split("\\s");
		String command = args[0];

		// FileSystem
		// cd
		if (ShellCommandsBundle._FS_CHANGE_DIR.equals(command)
				&& args.length == 2) {
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.CHANGE_DIRECTORY, new String[] { args[1] },
					CommandSource.NON_UI_APP)));
		}
		// pwd
		else if (ShellCommandsBundle._FS_CURRENT_DIR.equals(command)) {
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.CURRENT_PATH, null, CommandSource.NON_UI_APP)));
		}
		// ls || ll
		else if (ShellCommandsBundle._LIST_SHORT_DIR.equals(command)
				|| ShellCommandsBundle._LIST_DETAILED_DIR.equals(command)) {
			String path = null;
			if (args.length == 2) {
				path = args[1];
			}
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.LIST_DIR, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// cat
		else if (ShellCommandsBundle._CAT_FILE.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			String path = args[1];
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.CAT_FILE, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// touch
		else if (ShellCommandsBundle._CREATE_FILE.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			String path = args[1];
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.MAKE_FILE, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// rm
		else if (ShellCommandsBundle._REMOVE_NODE.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			String path = args[1];
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.RM_NODE, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// mkdir
		else if (ShellCommandsBundle._CREATE_DIR.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			String path = args[1];
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.MAKE_DIR, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// tree
		else if (ShellCommandsBundle._FS_TREE.equals(command)) {
			String path = null;
			if (args.length == 2) {
				path = args[1];
			}
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.TREE_DIR, new String[] { path },
					CommandSource.NON_UI_APP)));
		}
		// mv
		else if (ShellCommandsBundle._MOVE_NODE.equals(command)) {
			String src = null;
			String dest = null;
			if (args.length != 3) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			src = args[1];
			dest = args[2];

			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.MV_NODE, new String[] { src, dest },
					CommandSource.NON_UI_APP)));
		}
		// cp
		else if (ShellCommandsBundle._COPY_NODE.equals(command)) {
			String src = null;
			String dest = null;
			if (args.length != 3) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			src = args[1];
			dest = args[2];

			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.CP_NODE, new String[] { src, dest },
					CommandSource.NON_UI_APP)));
		}
		// rnm
		else if (ShellCommandsBundle._RENAME_NODE.equals(command)) {
			String path = null;
			if (args.length == 2) {
				path = args[1];
			}
			appendText(_INTERPRETER.handleSystemCommand(new Command(
					CommandType.TREE_DIR, new String[] { path },
					CommandSource.NON_UI_APP)));
		}

		// Session Commands
		else if (ShellCommandsBundle._SESSION_LOGOUT_COMMAND.equals(command)) {
			SessionManager.getInstance().logout();
		}

		// Process Commands
		else if (ShellCommandsBundle._INSTALLED_APP_COMMAND.equals(command)) {
			for (String appName : AppsManager.getInstance()
					.getInstalledAppsCommands()) {
				appendText(appName);
			}
		} else if (ShellCommandsBundle._PROCESSES_LIST_COMMAND.equals(command)) {
			appendText(CommandResultDecorator
					.getProcessesListDecorator(SystemProcessManager
							.getInstance().getProcesses()));
		} else if (ShellCommandsBundle._LAUNCH_PROCESS_COMMAND.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			_INTERPRETER.handleSystemCommand(new Command(
					CommandType.LAUNCH_PROCESS, new String[] { args[1] },
					CommandSource.NON_UI_APP));
		} else if (ShellCommandsBundle._KILL_PROCESS_COMMAND.equals(command)) {
			if (args.length < 2) {
				appendText(_STRINGS_BUNDLE.badArgumentMessage());
				return;
			}
			String arg = args[1];
			if ("all".equals(arg)) {
				_INTERPRETER.handleSystemCommand(new Command(
						CommandType.KILL_ALL_PROCESSES, null,
						CommandSource.NON_UI_APP));
			} else {
				appendText(_INTERPRETER.handleSystemCommand(new Command(
						CommandType.KILL_PROCESS, new String[] { arg },
						CommandSource.NON_UI_APP)));
			}
		}

		// Shell commands
		else if (ShellCommandsBundle._CLEAR_SHELL.equals(command)) {
			clearShell();
		} else if (ShellCommandsBundle._HELP_SHELL.equals(command)) {
			// All commands documentation
			if (args.length == 1) {
				Map<String, String> help = ShellCommandHelpBundle.getInstance()
						.getCommandsHelp();
				appendText(CommandResultDecorator.getHelpDecorator(help));
			}
			// Command specific help
			if (args.length == 2) {
				String help = ShellCommandHelpBundle.getInstance()
						.getCommandHelp(args[1]);
				if (null == help) {
					appendText(_STRINGS_BUNDLE.unrecognizableCommand());
					return;
				}
				appendText(CommandResultDecorator.getHelpDecorator(help,
						args[1]));
			}
		} else {
			appendText(_STRINGS_BUNDLE.unrecognizableCommand());
		}
	}

	/**
	 * Sets area listeners
	 */

	private void setTextAreaListeners() {
		area.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyDown(ComponentEvent event) {
				// Before
				switch (event.getKeyCode()) {
				case KeyCodes.KEY_ENTER:
					// Validate command
					event.stopEvent();
					String command = area.getValue().substring(
							getRelativeLineStartCursorPos()
									+ getPrompt().length());
					// Handle the command
					handleCommand(command);
					// Append the prompt text
					appendText(getPrompt());
					// Add the command to the history
					COMMAND_HISTORY.add(command);
					HISTORY_COUNTER = 0;
					break;
				case KeyCodes.KEY_BACKSPACE:
					// Delete a character
					if (area.getCursorPos() < getRelativeLineStartCursorPos()
							+ getPrompt().length() + 1) {
						event.stopEvent();
					}
					break;
				case KeyCodes.KEY_DELETE:
					// Delete a character
					if (area.getCursorPos() < getRelativeLineStartCursorPos()
							|| area.getCursorPos() < getRelativeLineStartCursorPos()
									+ getPrompt().length()) {
						event.stopEvent();
					}
					break;
				case KeyCodes.KEY_HOME:
					// Go back to the begin of the command
					event.stopEvent();
					area.setCursorPos(getRelativeLineStartCursorPos()
							+ getPrompt().length());
					break;
				case KeyCodes.KEY_UP:
					// History Support
					event.stopEvent();
					if (HISTORY_COUNTER >= COMMAND_HISTORY.size()) {
						HISTORY_COUNTER = 0;
					}
					String lastCommand = COMMAND_HISTORY.get(HISTORY_COUNTER++);
					if (lastCommand == null || lastCommand.isEmpty()) {
						break;
					}
					setText(lastCommand, getRelativeLineStartCursorPos()
							+ getPrompt().length(), -1);
					break;
				case KeyCodes.KEY_DOWN:
					// History Support
					event.stopEvent();
					if (HISTORY_COUNTER == 0) {
						HISTORY_COUNTER = COMMAND_HISTORY.size() - 1;
					} else {
						HISTORY_COUNTER--;
					}
					lastCommand = COMMAND_HISTORY.get(HISTORY_COUNTER);
					if (lastCommand == null || lastCommand.isEmpty()) {
						break;
					}
					setText(lastCommand, getRelativeLineStartCursorPos()
							+ getPrompt().length(), -1);
					break;
				case KeyCodes.KEY_LEFT:
					// Avoid the cursor to be on the prompt area
					if (area.getCursorPos() < (getRelativeLineStartCursorPos()
							+ getPrompt().length() + 1)) {
						event.stopEvent();
					}
					break;
				case KeyCodes.KEY_TAB:
					// Auto Complete
					event.stopEvent();
					command = area.getValue().substring(
							getRelativeLineStartCursorPos()
									+ getPrompt().length());
					// List the content of the current dir
					if (command.isEmpty()) {
						appendText(_INTERPRETER
								.handleSystemCommand(new Command(
										CommandType.LIST_DIR,
										new String[] { FileSystemManager
												.getInstance().pwd().getPath() },
										CommandSource.NON_UI_APP)));
						appendText(getPrompt());
					} else {
						String oracle = ShellCommandOracle
								.divineCommand(command);
						setText(oracle, getRelativeLineStartCursorPos()
								+ getPrompt().length(), area.getValue()
								.length());
					}
					break;
				}

				// Scrolling support
				area.getElement()
						.getFirstChildElement()
						.setScrollTop(
								area.getElement().getFirstChildElement()
										.getScrollHeight());
			}
		});

		area.addListener(Events.OnClick, new Listener<BoxComponentEvent>() {
			@Override
			public void handleEvent(BoxComponentEvent be) {
				be.setCancelled(true);
				area.setCursorPos(getRelativeLineStartCursorPos()
						+ getPrompt().length());
			}
		});

		area.addListener(Events.OnFocus, new Listener<BoxComponentEvent>() {
			@Override
			public void handleEvent(BoxComponentEvent be) {
				area.setCursorPos(getRelativeLineStartCursorPos()
						+ getPrompt().length());
			}
		});
	}

	private void appendText(String text) {
		if (text == null) {
			return;
		}
		String value = "";
		if (area.getValue() != null) {
			value = area.getValue() + _NEW_LINE_SEPARATOR;
		}
		area.setValue(value + text);
	}

	private void setText(String text, int start, int end) {

		if (text == null || text.isEmpty()) {
			return;
		}
		String value = area.getValue();
		if (value == null || value.isEmpty()) {
			appendText(text);
			return;
		}
		String before = value.substring(0, start);
		String after = "";
		if (end > -1) {
			after = value.substring(end);
		}
		if (before == null || after == null) {
			return;
		}
		area.setValue(before + text + after);
	}

	private void clearShell() {
		area.clear();
	}

	private String getPrompt() {
		String value = SessionManager.getInstance().loggedUser().getUsername()
				+ Constants.GWOS_PROMPT_SEPARATOR
				+ Constants.GWOS_PROMPT
				+ _INTERPRETER.handleSystemCommand(new Command(
						CommandType.CURRENT_PATH, null,
						CommandSource.NON_UI_APP));
		if (SessionManager.getInstance().loggedUser().getGroup() == UserGroup.ROOT) {
			value += Constants.ROOT_PROMPT_SUFFIX;
		} else {
			value += Constants.USER_PROMPT_SUFFIX;
		}
		value += " ";
		return value;
	}

	private int getRelativeLineStartCursorPos() {
		return area.getValue().lastIndexOf(_NEW_LINE_SEPARATOR) + 1;
	}

	@Override
	public String toString() {
		return "ShellApp [name=" + name + ", version=" + version + ", author="
				+ author + ", widget=" + area + "]";
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
