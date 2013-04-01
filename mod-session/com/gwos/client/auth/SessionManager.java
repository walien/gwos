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

package com.gwos.client.auth;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.core.events.AuthenticationEvent;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.core.system.SystemBootStrap;
import com.gwos.client.domain.impl.User;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.client.services.ServiceBundle;

public class SessionManager {

	private static SessionManager _INSTANCE;
	private User loggedUser;
	private MessageBox waitMessageBox;

	private boolean isFSSavingFinished;
	private boolean isDBConnectionClosingFinished;

	private SessionManager() {
		isFSSavingFinished = false;
		isDBConnectionClosingFinished = false;
	}

	public static SessionManager getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new SessionManager();
		}
		return _INSTANCE;
	}

	public void logIn(String username, String password) {

		// Call the authentication service
		ServiceBundle.getInstance().getSessionService()
				.getUser(username, password, new AsyncCallback<User>() {

					@Override
					public void onSuccess(final User result) {

						// The logged user
						loggedUser = result;

						// Launch system event
						SystemEventManager.getInstance().fireSystemEvent(
								new AuthenticationEvent(loggedUser));

						// If the result is null => access refused
						if (null == result) {
							GwosLogger.getLogger()
									.info("Authentication error : \"" + result
											+ "\"");
							return;
						}

						// Otherwise, init system components => the
						// access is granted
						grantAccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});
	}

	private void grantAccess(User result) {

		// Display wait box while the system is starting
		waitMessageBox = MessageBox.wait(ResourcesBundle.getStrings()
				.loginBoxTitle(), ResourcesBundle.getStrings()
				.loginBoxMessage(), ResourcesBundle.getStrings()
				.loginBoxTitle());
		waitMessageBox.show();

		// Load (asynchronously => on demand) system components
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {

				// Init system components
				SystemBootStrap.init();

				// Close the wait box
				waitMessageBox.close();
			}

			@Override
			public void onFailure(Throwable reason) {
				reason.printStackTrace();
			}
		});

		// Logging
		GwosLogger.getLogger().info(
				"Authentication was a success for user \""
						+ result.getUsername() + "\" (group : "
						+ result.getGroup().name() + ")");

	}

	public boolean isUserLogged() {
		return loggedUser != null;
	}

	public boolean isRoot() {
		return loggedUser.getGroup() == UserGroup.ROOT;
	}

	public boolean isAdmin() {
		return loggedUser.getGroup() == UserGroup.ADMIN;
	}

	public boolean isUser() {
		return loggedUser.getGroup() == UserGroup.USER;
	}

	public User loggedUser() {
		return loggedUser;
	}

	public void logout() {

		if (loggedUser == null) {
			return;
		}

		waitMessageBox = MessageBox.wait(ResourcesBundle.getStrings()
				.logoutBoxTitle(), ResourcesBundle.getStrings()
				.logoutBoxMessage(), ResourcesBundle.getStrings()
				.logoutBoxTitle());
		waitMessageBox.show();

		// Kill all running processes
		CommandInterpreter.getInstance().handleSystemCommand(
				new Command(CommandType.KILL_ALL_PROCESSES, null,
						CommandSource.DESKTOP));

		// Save FS (call the FS service)
		ServiceBundle
				.getInstance()
				.getFSService()
				.saveSessionFS(loggedUser,
						FileSystemManager.getInstance().getRoot(),
						new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
								isFSSavingFinished = true;
								finishSession();
							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									GwosLogger.getLogger().info(
											"Saving FS Successfuly !");
								} else {
									GwosLogger.getLogger().info(
											"Error during FS Save !");
								}
								isFSSavingFinished = true;
								finishSession();
							}
						});

		// Close DB Connection (User aspect)
		ServiceBundle.getInstance().getSessionService()
				.closeUserConnection(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						isDBConnectionClosingFinished = true;
						finishSession();
					}

					@Override
					public void onSuccess(Void result) {
						GwosLogger.getLogger().info(
								"Connection with DB User closed !");
						isDBConnectionClosingFinished = true;
						finishSession();
					}
				});
	}

	/**
	 * When all is done : Restart the app !
	 */

	public void finishSession() {
		if (isFSSavingFinished == false
				|| isDBConnectionClosingFinished == false) {
			return;
		}

		// Close wait message box
		waitMessageBox.close();

		// Restart Application
		restartApp();
	}

	/**
	 * 
	 */

	private native void restartApp() /*-{
		$wnd.location.reload();
	}-*/;

}
