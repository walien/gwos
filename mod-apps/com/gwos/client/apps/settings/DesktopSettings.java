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

package com.gwos.client.apps.settings;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.constants.Constants;
import com.gwos.client.core.shell.Command;
import com.gwos.client.core.shell.CommandInterpreter;
import com.gwos.client.core.shell.CommandSource;
import com.gwos.client.core.shell.CommandType;
import com.gwos.client.domain.impl.SettingsScreen;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.client.ui.desktop.GwosDesktop;

public class DesktopSettings extends SettingsScreen {

	private LayoutContainer widget;

	private ContentPanel wallpaperSection;
	private FormPanel toolbarSection;

	private RadioGroup desktopRadioGroup;
	private Radio wallpaperBlueRadio;
	private Radio wallpaperLionRadio;

	private Button applyButton;
	private Button closeButton;

	public DesktopSettings() {

	}

	private void render() {

		// The root container
		widget = new LayoutContainer(new BorderLayout());

		// Render root panel
		wallpaperSection = new ContentPanel();
		wallpaperSection.setHeading("Wallpaper");
		toolbarSection = new FormPanel();
		toolbarSection.setHeading("Toolbar");

		applyButton = new Button("Apply");
		closeButton = new Button("Close");

		// Render sections
		renderWallpaperSection();

		renderToolBarCustomizingSection();

		// Sections widget layout
		BorderLayoutData top = new BorderLayoutData(LayoutRegion.NORTH, 50, 50,
				250);
		widget.add(wallpaperSection, top);
		BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER,
				130, 100, 250);
		widget.add(toolbarSection, center);
		BorderLayoutData south = new BorderLayoutData(LayoutRegion.SOUTH, 20,
				100, 250);
		widget.add(applyButton, south);
	}

	private void renderWallpaperSection() {

		desktopRadioGroup = new RadioGroup("Wallpapers");

		wallpaperBlueRadio = new Radio();
		wallpaperBlueRadio.setBoxLabel("Tux");
		wallpaperBlueRadio.setValue(true);
		wallpaperLionRadio = new Radio();
		wallpaperLionRadio.setBoxLabel("Rainbow");

		desktopRadioGroup.add(wallpaperBlueRadio);
		desktopRadioGroup.add(wallpaperLionRadio);

		wallpaperSection.add(desktopRadioGroup);
	}

	private void renderToolBarCustomizingSection() {

		// 1. Task bar spinner
		final SpinnerField spinner = new SpinnerField();

		spinner.setMaxValue(50);
		spinner.setMinValue(20);
		spinner.setValue(Constants.TASK_BAR_SIZE);

		spinner.addListener(Events.Change, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				GwosDesktop.getInstance().getTaskBar()
						.setBarSize(spinner.getValue().intValue());
			}
		});

		spinner.setFieldLabel("Task bar size (px)");

		// 2. Task bar color
		// TODO

		// Add controls to the panel
		toolbarSection.add(spinner);
	}

	private void setListeners(final int pid) {
		applyButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Radio selectedRadio = desktopRadioGroup.getValue();
				if (selectedRadio.getBoxLabel().equals("Tux")) {
					GwosDesktop.getInstance().setWallpaper(
							"desktop-wallpaper-tux", false);
				} else if (selectedRadio.getBoxLabel().equals("Rainbow")) {
					GwosDesktop.getInstance().setWallpaper(
							"desktop-wallpaper-rainbow", false);
				}
			}
		});

		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				CommandInterpreter.getInstance().handleSystemCommand(
						new Command(CommandType.KILL_PROCESS,
								new String[] { Integer.toString(pid) },
								CommandSource.UI_APP));
			}
		});
	}

	@Override
	public String getName() {
		return "Desktop Settings";
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
		return 300;
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	public void setParams(String[] params) {

	}

	@Override
	public void start(int pid) {
		// Rendering the screen
		render();
		// Set listeners
		setListeners(pid);
	}

	@Override
	public int exit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
