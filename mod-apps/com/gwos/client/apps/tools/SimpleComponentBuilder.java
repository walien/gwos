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

package com.gwos.client.apps.tools;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwos.client.domain.impl.User;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.client.domain.impl.models.UserModel;
import com.gwos.client.services.ServiceBundle;

public class SimpleComponentBuilder {

	public static Grid createUsersGrid() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig("username", "UserName", 100));
		columns.add(new ColumnConfig("firstname", "First Name", 100));
		columns.add(new ColumnConfig("lastname", "Last Name", 100));
		columns.add(new ColumnConfig("email", "Last Name", 100));
		columns.add(new ColumnConfig("group", "Group", 50));

		final ColumnModel cm = new ColumnModel(columns);
		final ListStore store = new ListStore<UserModel>();

		final Grid grid = new Grid(store, cm);
		grid.setBorders(true);
		grid.setAutoHeight(true);
		grid.setAutoWidth(true);
		grid.setAutoExpandColumn("username");

		// Set selection model
		GridSelectionModel<UserModel> model = new GridSelectionModel<UserModel>();
		model.setSelectionMode(SelectionMode.SINGLE);

		// Populate the grid
		populateUserGrid(grid);

		return grid;
	}

	public static void populateUserGrid(final Grid grid) {

		final ListStore<UserModel> store = grid.getStore();
		store.removeAll();
		final ColumnModel cm = grid.getColumnModel();

		// Populate store
		ServiceBundle.getInstance().getSessionService()
				.getAllUsers(new AsyncCallback<List<User>>() {

					@Override
					public void onSuccess(List<User> result) {
						for (User user : result) {
							// Ignore root user
							if (user.getGroup() == UserGroup.ROOT) {
								continue;
							}
							store.add(new UserModel(user));
						}
						grid.reconfigure(store, cm);
					}

					@Override
					public void onFailure(Throwable caught) {
						// nothing
					}
				});
	}

}
