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

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.apps.components.SystemProcessGridModel;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.constants.Strings;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemProcessEvent;
import com.gwos.client.core.events.SystemProcessEventHandler;
import com.gwos.client.core.process.SystemProcessManager;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.SystemProcess;
import com.gwos.client.domain.impl.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerApp extends Application {

	private static final String _PROCESS_PID_BINDING = "pid";
	private static final String _PROCESS_NAME_BINDING = "name";
	private static final String _PROCESS_OWNER_BINDING = "owner";
	private static final String _PROCESS_PRIORITY_BINDING = "priority";

	private static final Strings _STRINGS = ResourcesBundle.getStrings();

	private String name;
	private String author;
	private int version;

	private LayoutContainer rootPanel;

	private ListStore<SystemProcessGridModel> store;
	private ColumnModel cm;
	private Grid<SystemProcessGridModel> grid;
	private Button killButton;

	public ProcessManagerApp() {
		name = "Process Manager";
		author = "walien";
		version = 1;
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
	public int getParentWidth() {
		return 500;
	}

	@Override
	public int getParentHeight() {
		return 300;
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

		// Render root panel
		rootPanel = new LayoutContainer(new BorderLayout());

		// Render subcomponents
		grid = renderAndPopulateGrid();

		killButton = new Button(_STRINGS.killProcessButtonLabel());
		killButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<SystemProcessGridModel> selection = grid
						.getSelectionModel().getSelectedItems();
				for (SystemProcessGridModel model : selection) {
					// The selected process ID
					int pid = model.<Integer>get(_PROCESS_PID_BINDING);

					// Kill the selected process
					CommandInterpreter.getInstance().handleSystemCommand(
							new Command(CommandType.KILL_PROCESS,
									new String[] { Integer.toString(pid) },
									CommandSource.UI_APP));
				}
			}
		});

		// Add components to the root panel
		BorderLayoutData top = new BorderLayoutData(LayoutRegion.CENTER, 300,
				100, 250);
		rootPanel.add(grid, top);
		BorderLayoutData bottom = new BorderLayoutData(LayoutRegion.SOUTH, 30,
				100, 250);
		rootPanel.add(killButton, bottom);

		// Set listeners
		setListeners();
	}

	private Grid renderAndPopulateGrid() {

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig(_PROCESS_PID_BINDING,
				_STRINGS.processIDColumnLabel(), 50);
		configs.add(column);

		column = new ColumnConfig(_PROCESS_NAME_BINDING,
				_STRINGS.processNameColumnLabel(), 200);
		configs.add(column);

		column = new ColumnConfig(_PROCESS_OWNER_BINDING,
				_STRINGS.processOwnerColumnLabel(), 75);
		configs.add(column);

		column = new ColumnConfig(_PROCESS_PRIORITY_BINDING,
				_STRINGS.processPriorityColumnLabel(), 100);
		configs.add(column);

		store = new ListStore<SystemProcessGridModel>();

		// Fill store with data
		fillStore();

		// The grid
		cm = new ColumnModel(configs);
		final Grid<SystemProcessGridModel> grid = new Grid<SystemProcessGridModel>(
				store, cm);
		grid.setBorders(false);
		grid.setStripeRows(true);
		grid.setColumnLines(true);
		grid.setColumnReordering(true);

		return grid;
	}

	private void fillStore() {
		store.removeAll();
		// Populate
		for (SystemProcess process : SystemProcessManager.getInstance()
				.getProcesses().values()) {
			store.add(convertEntityToModel(process));
		}
	}

	private void setListeners() {
		SystemEventManager.getInstance().addEventHandler(
				SystemProcessEvent.TYPE, new SystemProcessEventHandler() {
					@Override
					public void onSystemProcessEvent(SystemProcessEvent event) {
						switch (event.getProcessStatus()) {
						case STARTED:
						case KILLED:
							fillStore();
							grid.reconfigure(store, cm);
							break;
						}
						GwosLogger.getLogger().info(
								"Refreshing process manager list...");
					}
				});
	}

	private SystemProcessGridModel convertEntityToModel(SystemProcess process) {
		return new SystemProcessGridModel(process);
	}

	@Override
	public int exit() {
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
