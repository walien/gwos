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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.apps.tools.SimpleComponentBuilder;
import com.gwos.client.apps.widgets.MemoryUsageWidget;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.StatObject;
import com.gwos.client.domain.impl.User;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.client.domain.impl.models.UserGroupModel;
import com.gwos.client.domain.impl.models.UserModel;
import com.gwos.client.services.ServiceBundle;

public class AdministrationApp extends Application {

	private TabPanel rootPanel;
	private VerticalPanel usersManagementPanel;

	private Grid grid;
	private Button deleteButton;

	public AdministrationApp() {

	}

	@Override
	public String getName() {
		return "Admin Tools";
	}

	@Override
	public String getAuthor() {
		return "walien";
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public int getParentWidth() {
		return 500;
	}

	@Override
	public int getParentHeight() {
		return 430;
	}

	@Override
	public Widget getWidget() {
		return rootPanel;
	}

	@Override
	public void setParams(String[] params) {

	}

	@Override
	public void start(int pid) {

		// Panels
		rootPanel = new TabPanel();
		usersManagementPanel = new VerticalPanel();

		// ////////////////////////////////
		// 1. User management
		// ////////////////////////////////

		// The content is different according to the group of the user : ROOT &
		// ADMIN mode
		if (SessionManager.getInstance().isRoot()
				|| SessionManager.getInstance().isAdmin()) {

			// Add Form Panel
			final FormPanel form = createAddUserForm();
			usersManagementPanel.add(form);

			// The users grid
			grid = SimpleComponentBuilder.createUsersGrid();

			// The user delete button
			deleteButton = new Button(ResourcesBundle.getStrings().deleteUser());
			deleteButton
					.addSelectionListener(new SelectionListener<ButtonEvent>() {

						@Override
						public void componentSelected(ButtonEvent ce) {
							ModelData item = grid.getSelectionModel()
									.getSelectedItem();
							if (item == null) {
								return;
							}
							UserModel model = (UserModel) item;
							final String username = model
									.get(UserModel._USERNAME);
							ServiceBundle
									.getInstance()
									.getSessionService()
									.deleteUser(username,
											new AsyncCallback<Boolean>() {

												@Override
												public void onFailure(
														Throwable caught) {
													caught.printStackTrace();
												}

												@Override
												public void onSuccess(
														Boolean result) {
													if (result) {
														GwosLogger
																.getLogger()
																.info("User "
																		+ username
																		+ "successfuly deleted !");
														Info.display(
																"User deleted",
																"The user "
																		+ username
																		+ " was successfuly deleted !");

														// Clear the form
														form.clear();

														// Refresh the grid
														refreshGrid();
													}
												}
											});
						}
					});

			// Grid Panel
			ContentPanel gridPanel = new ContentPanel();
			gridPanel.setHeading("Users List");
			gridPanel.setAutoHeight(true);
			gridPanel.setAutoWidth(true);
			gridPanel.add(grid);
			gridPanel.add(deleteButton);

			// Add it to the root panel
			usersManagementPanel.add(gridPanel);
		}
		// The content is different according to the group of the user : USER
		// mode
		if (SessionManager.getInstance().isUser()) {
			FormPanel panel = createDisplayUserForm();
			panel.setAutoHeight(true);
			panel.setAutoWidth(true);

			usersManagementPanel.add(panel);
		}

		// Add the item to the tab bar
		TabItem userManagementItem = new TabItem("User Management");
		userManagementItem.add(usersManagementPanel);
		rootPanel.add(userManagementItem);

		// ////////////////////////////////
		// 2. Memory Usage
		// ////////////////////////////////

		// Add the item to the tab bar
		final TabItem memoryUsageItem = new TabItem("Memory Usage");
		rootPanel.add(memoryUsageItem);

		ServiceBundle.getInstance().getAppsService()
				.getMemoryStats(new AsyncCallback<StatObject>() {

					@Override
					public void onSuccess(StatObject result) {
						GwosLogger.getLogger().info(
								"Stats result returned by the server : "
										+ result);
						memoryUsageItem.add(new MemoryUsageWidget()
								.render(result));
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});

	}

	private FormPanel createAddUserForm() {

		FormPanel form = new FormPanel();
		form.setHeading("Add User Form");
		form.setAutoHeight(true);
		form.setAutoWidth(true);

		final TextField<String> usernameTF = new TextField<String>();
		usernameTF.setFieldLabel("Username");

		final TextField<String> firstNameTF = new TextField<String>();
		firstNameTF.setFieldLabel("First Name");
		final TextField<String> lastNameTF = new TextField<String>();
		lastNameTF.setFieldLabel("Last Name");

		final TextField<String> emailTF = new TextField<String>();
		emailTF.setFieldLabel("Email");

		final TextField<String> passwordTF = new TextField<String>();
		passwordTF.setPassword(true);
		passwordTF.setFieldLabel("Password");

		ListStore<UserGroupModel> store = new ListStore<UserGroupModel>();
		for (UserGroup group : UserGroup.values()) {
			store.add(new UserGroupModel(group));
		}
		final ComboBox userGroupCB = new ComboBox();
		userGroupCB.setDisplayField("name");
		userGroupCB.setStore(store);
		userGroupCB.setFieldLabel("Group");

		form.add(usernameTF);
		form.add(firstNameTF);
		form.add(lastNameTF);
		form.add(passwordTF);
		form.add(emailTF);
		form.add(userGroupCB);

		Button createButton = new Button("Create");
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				UserGroupModel selectedUG = (UserGroupModel) userGroupCB
						.getValue();
				createUser(usernameTF.getValue(), firstNameTF.getValue(),
						lastNameTF.getValue(), passwordTF.getValue(),
						emailTF.getValue(), selectedUG.getGroup());
			}

		});
		form.add(createButton);

		return form;
	}

	private FormPanel createDisplayUserForm() {

		FormPanel form = new FormPanel();
		form.setHeading("Display User Infos");

		final TextField<String> usernameTF = new TextField<String>();
		usernameTF.setFieldLabel("Username");

		final TextField<String> firstNameTF = new TextField<String>();
		firstNameTF.setFieldLabel("First Name");
		final TextField<String> lastNameTF = new TextField<String>();
		lastNameTF.setFieldLabel("Last Name");

		final TextField<String> emailTF = new TextField<String>();
		emailTF.setFieldLabel("Email");

		final TextField<String> passwordTF = new TextField<String>();
		passwordTF.setFieldLabel("Password");

		ListStore<UserGroupModel> store = new ListStore<UserGroupModel>();
		for (UserGroup group : UserGroup.values()) {
			store.add(new UserGroupModel(group));
		}
		final ComboBox userGroupCB = new ComboBox();
		userGroupCB.setDisplayField("name");
		userGroupCB.setStore(store);
		userGroupCB.setFieldLabel("Group");

		form.add(usernameTF);
		form.add(firstNameTF);
		form.add(lastNameTF);
		form.add(passwordTF);
		form.add(emailTF);
		form.add(userGroupCB);

		// Read only mode
		usernameTF.setEnabled(false);
		firstNameTF.setEnabled(false);
		lastNameTF.setEnabled(false);
		emailTF.setEnabled(false);
		passwordTF.setEnabled(false);
		userGroupCB.setEnabled(false);

		// Fill Data
		User user = SessionManager.getInstance().loggedUser();
		usernameTF.setValue(user.getUsername());
		firstNameTF.setValue(user.getFirstName());
		lastNameTF.setValue(user.getLastName());
		passwordTF.setValue(user.getPassword());
		emailTF.setValue(user.getEmail());
		userGroupCB.setValue(new UserGroupModel(user.getGroup()));

		return form;
	}

	private void createUser(String username, String firstname, String lastname,
			String password, String email, UserGroup group) {

		// Create the new user
		User user = new User(firstname, lastname, username, password, email);
		GwosLogger.getLogger().info("Creating new user : " + user);

		// Call the service
		ServiceBundle.getInstance().getSessionService()
				.addUser(user, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Info.display("Error !", "Error !");
					}

					@Override
					public void onSuccess(Boolean result) {
						Info.display("OK !", "User created !");
						refreshGrid();
					}
				});
	}

	private void refreshGrid() {
		SimpleComponentBuilder.populateUserGrid(grid);
	}

	@Override
	public int exit() {
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return group == UserGroup.ROOT || group == UserGroup.ADMIN;
	}
}
