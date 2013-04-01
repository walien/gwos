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

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Composite;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.core.events.AppUIEvent;
import com.gwos.client.core.events.AppUIEventHandler;
import com.gwos.client.core.events.AppUIEventType;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemProcessEvent;
import com.gwos.client.core.events.SystemProcessEventHandler;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.SystemProcess;

public class WindowsArea extends Composite {

	private Map<Integer, Window> windows;
	private boolean isMinimized;
	private Window activeWin;

	public WindowsArea() {
		windows = new HashMap<Integer, Window>();
		init();
		setHandlers();
	}

	private void init() {
		SystemEventManager.getInstance().addEventHandler(
				SystemProcessEvent.TYPE, new SystemProcessEventHandler() {
					@Override
					public void onSystemProcessEvent(SystemProcessEvent event) {
						SystemProcess process = event.getProcess();
						Application app = process.getApplication();
						switch (event.getProcessStatus()) {
						case STARTED:
							Window win = new Window();
							win.setHeading(app.getName() + " - v"
									+ app.getVersion());
							win.setLayout(new FitLayout());
							win.setFocusWidget(app.getWidget());
							win.add(app.getWidget());
							win.setSize(app.getParentWidth(),
									app.getParentHeight());
							win.setClosable(true);
							win.setMaximizable(true);
							win.setMinimizable(true);
							windows.put(process.getPID(), win);
							setWindowListeners(process, win);
							win.setVisible(true);
							activeWin = win;
							GwosLogger.getLogger().info(
									"New app window named : \""
											+ event.getProcess().getName()
											+ "\" shown !");
							break;
						case KILLED:
							win = windows.remove(process.getPID());
							if (win == null) {
								break;
							}
							win.setVisible(false);
							activeWin = null;
							GwosLogger.getLogger().info(
									"App window named : \""
											+ event.getProcess().getName()
											+ "\" destroyed !");
							break;
						}
					}
				});
	}

	private void setWindowListeners(final SystemProcess proc, final Window w) {
		w.addWindowListener(new WindowListener() {

			@Override
			public void windowHide(WindowEvent we) {
				if (isMinimized == false) {
					CommandInterpreter.getInstance()
							.handleSystemCommand(
									new Command(CommandType.KILL_PROCESS,
											new String[] { Integer
													.toString(proc.getPID()) },
											CommandSource.DESKTOP));
				}
				isMinimized = false;
			}

			@Override
			public void windowMinimize(WindowEvent we) {
				SystemEventManager.getInstance().fireSystemEvent(
						new AppUIEvent(AppUIEventType.APP_MINIMIZE, proc));
				isMinimized = true;
				w.hide();
			}

			@Override
			public void windowActivate(WindowEvent we) {
				activeWin = w;
			}
		});
	}

	private void setHandlers() {
		SystemEventManager.getInstance().addEventHandler(AppUIEvent.TYPE,
				new AppUIEventHandler() {

					@Override
					public void onAppUIEvent(AppUIEvent event) {
						if (event.getProcess() == null) {
							return;
						}
						Window focusWin = windows.get(event.getProcess()
								.getPID());
						switch (event.getUIEventType()) {
						case APP_FOCUS_REQUEST:
							focusWin.show();
							focusWin.setActive(true);
							activeWin = focusWin;
							GwosLogger.getLogger().info(
									"Focus requested on app window named : \""
											+ event.getProcess().getName()
											+ "\"!");
							break;
						case APP_MINIMIZE_REQUEST:
							if (focusWin.isVisible() == true) {
								isMinimized = true;
								focusWin.hide();
								GwosLogger.getLogger().info(
										"Minimizing requested on app window named : \""
												+ event.getProcess().getName()
												+ "\"!");
							}
							break;
						case APP_CLOSE:
							isMinimized = false;
							focusWin.hide();
							activeWin = null;
							GwosLogger.getLogger().info(
									"Closing requested on app window named : \""
											+ event.getProcess().getName()
											+ "\"!");
							break;
						}
					}
				});
	}

	public int getAsociatePID(Window win) {
		for (Integer pid : windows.keySet()) {
			if (windows.get(pid) == win) {
				return pid;
			}
		}
		return -1;
	}

	public Window getActiveWindow() {
		return activeWin;
	}
}