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

package com.gwos.client.ui.desktop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.gwos.client.auth.SessionManager;
import com.gwos.client.constants.Constants;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.core.events.AppUIEvent;
import com.gwos.client.core.events.AppUIEventHandler;
import com.gwos.client.core.events.AppUIEventType;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemProcessEvent;
import com.gwos.client.core.events.SystemProcessEventHandler;
import com.gwos.client.domain.impl.SystemProcess;
import com.gwos.client.domain.impl.User;
import com.gwos.client.ui.desktop.components.ContextMenuBundle;
import com.gwos.client.ui.desktop.components.ContextMenuBundle.MenuContext;

public class TasksBar extends Composite {

	private HorizontalPanel rootPanel;
	private ToolBar taskBar;
	private Button startButton;
	private Label systemHour;

	private Map<SystemProcess, ToggleButton> launchedProcesses;

	public TasksBar() {
		launchedProcesses = new HashMap<SystemProcess, ToggleButton>();

		rootPanel = new HorizontalPanel();
		taskBar = new ToolBar();
		render();
		initWidget(rootPanel);

		setHandlers();
	}

	private void render() {

		// Start Button
		startButton = new Button();
		startButton.setSize(32, 32);
		startButton.setIcon(AbstractImagePrototype.create(ResourcesBundle
				.getBinaryResources().home_logo()));
		startButton.setMenu(getStartMenu());

		// Hour
		systemHour = new Label();
		systemHour.setText(Constants.TIME_FORMAT.format(new Date()));
		systemHour.setStyleAttribute("font-size", Constants.DESKTOP_HOUR_SIZE
				+ "px");
		launchActiveRefresh();

		// Add elements to the task bar
		taskBar.add(startButton);
		taskBar.add(new SeparatorToolItem());
		taskBar.add(systemHour);
		taskBar.add(new SeparatorToolItem());

		taskBar.setSize(Constants.TASK_BAR_WIDTH_OFFSET,
				Constants.TASK_BAR_SIZE);
		rootPanel.add(taskBar);
	}

	private void setHandlers() {

		SystemEventManager.getInstance().addEventHandler(
				SystemProcessEvent.TYPE, new SystemProcessEventHandler() {

					@Override
					public void onSystemProcessEvent(SystemProcessEvent event) {
						if (event.getProcess() == null) {
							return;
						}
						switch (event.getProcessStatus()) {
						case STARTED:
							addProcessButton(event.getProcess());
							GwosLogger.getLogger().info(
									"TaskBar button named : \""
											+ event.getProcess().getName()
											+ "\" added !");
							break;
						case KILLED:
							removeProcessButton(event.getProcess());
							GwosLogger.getLogger().info(
									"TaskBar button named : \""
											+ event.getProcess().getName()
											+ "\" removed !");
							break;
						}
					}
				});

		SystemEventManager.getInstance().addEventHandler(AppUIEvent.TYPE,
				new AppUIEventHandler() {

					@Override
					public void onAppUIEvent(AppUIEvent event) {
						if (event.getProcess() == null) {
							return;
						}
						switch (event.getUIEventType()) {
						case APP_MINIMIZE:
							launchedProcesses.get(event.getProcess()).toggle(
									false);
							break;
						case APP_FOCUS:
							launchedProcesses.get(event.getProcess()).toggle(
									true);
							break;
						}
					}
				});
	}

	private void addProcessButton(final SystemProcess process) {

		final ToggleButton b = new ToggleButton();
		b.setText(process.getName());
		b.toggle(true);

		b.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!b.isPressed()) {
					b.toggle(false);
					SystemEventManager.getInstance().fireSystemEvent(
							new AppUIEvent(AppUIEventType.APP_MINIMIZE_REQUEST,
									process));
				} else {
					b.toggle(true);
					SystemEventManager.getInstance().fireSystemEvent(
							new AppUIEvent(AppUIEventType.APP_FOCUS_REQUEST,
									process));
				}
			}
		});

		Menu contextMenu = ContextMenuBundle
				.getContextMenu(MenuContext.TASK_BAR_CONTEXT);
		MenuItem restoreProcess = (MenuItem) contextMenu.getItem(0);
		MenuItem closeProcess = (MenuItem) contextMenu.getItem(1);

		restoreProcess.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				b.toggle(true);
				SystemEventManager.getInstance().fireSystemEvent(
						new AppUIEvent(AppUIEventType.APP_FOCUS_REQUEST,
								process));
			}
		});

		closeProcess.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				SystemEventManager.getInstance().fireSystemEvent(
						new AppUIEvent(AppUIEventType.APP_CLOSE, process));
			}
		});

		b.setContextMenu(contextMenu);

		launchedProcesses.put(process, b);
		taskBar.add(b);
	}

	private void removeProcessButton(SystemProcess process) {
		ToggleButton button = launchedProcesses.get(process);
		if (button == null) {
			return;
		}

		taskBar.remove(button);
		launchedProcesses.remove(process);
	}

	private Menu getStartMenu() {

		Menu menu = new Menu();

		// Items
		MenuItem settings = new MenuItem(ResourcesBundle.getStrings()
				.settingsButton());
		MenuItem logout = new MenuItem(ResourcesBundle.getStrings()
				.logoutButton());
		final Label loggedUserLabel = new Label();

		User loggedUser = SessionManager.getInstance().loggedUser();
		loggedUserLabel.setText(loggedUser.getFirstName() + " "
				+ loggedUser.getLastName());

		// Listeners
		settings.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				// TODO
			}
		});

		logout.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				SessionManager.getInstance().logout();
			}
		});

		// Add items to the menu
		menu.add(settings);
		menu.add(logout);
		menu.add(loggedUserLabel);

		return menu;
	}

	private void launchActiveRefresh() {
		final Timer timer = new Timer() {
			public void run() {
				systemHour.setText(Constants.TIME_FORMAT.format(new Date()));
			}
		};

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				timer.scheduleRepeating(Constants.TIME_REFRESHING_PERIOD);
			}
		});
	}

	public void setBarSize(int size) {
		taskBar.setSize(Constants.TASK_BAR_WIDTH_OFFSET, size);
	}

}
