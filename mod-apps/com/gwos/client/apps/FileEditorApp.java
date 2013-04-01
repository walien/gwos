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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.gwos.client.apps.widgets.FSTreeWidget;
import com.gwos.client.constants.Constants;
import com.gwos.client.constants.ResourcesBundle;
import com.gwos.client.core.filesystem.FileSystemManager;
import com.gwos.client.domain.impl.Application;
import com.gwos.client.domain.impl.File;
import com.gwos.client.domain.impl.UserGroup;

public class FileEditorApp extends Application {

	private String name;
	private String author;
	private int version;

	private String[] params;

	private ContentPanel rootPanel;
	private HtmlEditor editor;
	private Dialog fsTreeDialog;

	private File currentFile;

	public FileEditorApp() {
		name = "FileEditor";
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
	public void setParams(String[] params) {
		this.params = params;
	}

	@Override
	public Widget getWidget() {
		return rootPanel;
	}

	@Override
	public void start(int pid) {

		rootPanel = new ContentPanel(new FitLayout());
		rootPanel.setHeaderVisible(false);

		// Render the tool bar
		ToolBar toolBar = buildEditorToolbar();
		rootPanel.setTopComponent(toolBar);

		// Render the HTML editor
		editor = new HtmlEditor();

		// If a file is provided as a parameter, load its content
		if (params.length > 1) {
			openFile(params[1]);
		}

		rootPanel.add(editor);
	}

	private ToolBar buildEditorToolbar() {

		ToolBar toolBar = new ToolBar();

		// Open/Save a file
		Button openFile = new Button(ResourcesBundle.getStrings()
				.editorOpenFile());
		openFile.setIcon(AbstractImagePrototype.create(ResourcesBundle
				.getBinaryResources().openFile_logo()));

		Button saveFile = new Button(ResourcesBundle.getStrings()
				.editorSaveFile());
		saveFile.setIcon(AbstractImagePrototype.create(ResourcesBundle
				.getBinaryResources().saveFile_logo()));

		// Sets handlers
		openFile.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				// The FS Tree
				final FSTreeWidget widget = new FSTreeWidget(true);

				// Build the dialog window
				fsTreeDialog = new Dialog();
				fsTreeDialog.setButtons(Dialog.CANCEL);
				fsTreeDialog.setHideOnButtonClick(true);

				// Add the widget to the dialog and show
				fsTreeDialog.add(widget);
				fsTreeDialog.show();

				Button button = new Button("Open");
				button.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						// Hide the dialog
						fsTreeDialog.hide();

						// Retrieves the selected model into the tree
						ModelData data = widget.getSelectionModel()
								.getSelectedItem();

						// Load content if the selected node is a file
						boolean isDir = data.<Boolean>get(Constants.FS_TREE_NODE_IS_DIR);
						if (isDir == true) {
							return;
						}

						// Open the file by its path
						openFile((String) data.get(Constants.FS_TREE_NODE_PATH));
					}
				});
				fsTreeDialog.addButton(button);
			}
		});

		saveFile.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				saveOpenedFile();
			}
		});

		// Add components to the tool bar
		toolBar.add(openFile);
		toolBar.add(saveFile);
		toolBar.add(new SeparatorToolItem());

		return toolBar;
	}

	private void openFile(String path) {
		currentFile = FileSystemManager.getInstance().getFile(path);
		if (currentFile == null) {
			return;
		}
		String content = currentFile.getContent();
		if (content == null) {
			return;
		}
		editor.setValue(content);
	}

	private void saveOpenedFile() {
		if (currentFile == null) {
			return;
		}
		currentFile.setContent(editor.getValue());
	}

	@Override
	public int exit() {
		if (null != fsTreeDialog) {
			fsTreeDialog.hide();
		}
		return 0;
	}

	@Override
	public boolean isEnoughtRights(UserGroup group) {
		return true;
	}
}
