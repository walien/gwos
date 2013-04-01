/*
 * Copyright 2013 - Elian ORIOU <elian.oriou@gmail.com>
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

package com.gwos.server.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwos.client.constants.Constants;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.domain.INode;
import com.gwos.client.domain.impl.Directory;
import com.gwos.client.domain.impl.FileType;
import com.gwos.client.domain.impl.NodeType;
import com.gwos.client.domain.impl.User;
import com.gwos.client.services.FileSystemService;
import com.gwos.server.services.utils.HasParameters;
import com.gwos.server.services.utils.ServiceUtils;

public class FileSystemServiceImpl extends RemoteServiceServlet implements
		FileSystemService, HasParameters {

	private static final long serialVersionUID = -3112528827805470334L;

	private String _LOGGED_USER_ROOT_DIR;

	private Directory _ROOT_DIR;
	private Directory _CURRENT_DIR;

	private ServletConfig config;

	public FileSystemServiceImpl() {
	}

	// //////////////////////////
	// // 0. WEB.XML PARAMETERS
	// //////////////////////////

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	@Override
	public ServletContext getServletContext() {
		if (config == null) {
			return null;
		}
		return config.getServletContext();
	}

	// //////////////////////////
	// // 1. TOOLS
	// //////////////////////////

	/**
	 * Returns the local user storage
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */

	private File getUserSpaceFolder(User user) throws Exception {

		// Retrieves the FS root folder path
		GwosLogger.getLogger().info(
				"FS Root Folder location : "
						+ new ServiceUtils(getServletContext()).getFSPath());
		File root = new File(new ServiceUtils(getServletContext()).getFSPath());

		if (root == null || !root.isDirectory()) {
			throw new Exception("FS Root Folder not found or invalid !");
		}
		// If it doesn't exists : Create it !
		if (root.exists() == false) {
			root.mkdir();
			GwosLogger
					.getLogger()
					.info("FS Root Folder newly created (for the first execution of GwOS) : "
							+ new ServiceUtils(getServletContext()).getFSPath());
		}

		boolean found = false;
		for (File f : root.listFiles()) {
			if (f.isDirectory() && f.getName().equals(user.getUsername())) {
				found = true;
				root = f;
				break;
			}
		}
		// If the user space doesn't exists : create it !
		if (found == false) {
			root = new File(root, user.getUsername());
			root.mkdir();
			GwosLogger
					.getLogger()
					.info("User space newly created for \""
							+ user.getUsername() + "\" in : "
							+ new ServiceUtils(getServletContext()).getFSPath());
		}
		_LOGGED_USER_ROOT_DIR = root.getAbsolutePath();
		return root;
	}

	/**
	 * Retrieves the content of a file
	 * 
	 * @param file
	 * @return
	 */

	private String getFileContent(File file) {

		StringBuilder builder = new StringBuilder();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				builder.append(scanner.nextLine());
				builder.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return builder.toString();
	}

	/**
	 * At logout, recreates a IO (physical on the HDD) node from the session
	 * file system
	 * 
	 * @param node
	 * @return
	 */

	private boolean createIONode(INode node) {

		if (node == null || _LOGGED_USER_ROOT_DIR == null) {
			return false;
		}
		File f = new File(_LOGGED_USER_ROOT_DIR + node.getPath());
		try {
			if (node.getNodeType() == NodeType.FILE) {
				boolean created = f.createNewFile();
				com.gwos.client.domain.impl.File domainFile = (com.gwos.client.domain.impl.File) node;
				if (domainFile.getContent() == null
						|| domainFile.getContent().isEmpty()) {
					return created;
				}
				FileWriter writer = new FileWriter(f);
				writer.append(domainFile.getContent());
				writer.close();
				return created;
			} else if (node.getNodeType() == NodeType.DIR) {
				return f.mkdir();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	// //////////////////////////
	// // 2. LOAD FS PART
	// //////////////////////////

	/**
	 * Returns the IO file system into a session file system exploitable by the
	 * user
	 * 
	 * @param user
	 */

	@Override
	public Directory getSessionFS(User user) {
		try {
			File userFolder = getUserSpaceFolder(user);
			if (userFolder == null) {
				return null;
			}
			_CURRENT_DIR = _ROOT_DIR = new Directory(Constants.PATH_SEPARATOR,
					null, user);
			buildSessionFS(userFolder, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _ROOT_DIR;
	}

	/**
	 * Build the session file system from the IO file system
	 * 
	 * @param root
	 * @param user
	 */

	private void buildSessionFS(File root, User user) {

		for (File f : root.listFiles()) {
			// 1. If the file is a directory, add it to the FS tree, then launch
			// recursive call on it
			if (f.isDirectory()) {
				// Hidden files
				if (f.getName().startsWith(Constants.HIDDEN_FILE_PREFIX)) {
					continue;
				}
				// Create the directory
				Directory dir = new Directory(f.getName(), _CURRENT_DIR, user);
				_CURRENT_DIR.addChildNode(dir);
				_CURRENT_DIR = dir;
				// Explore recursively
				buildSessionFS(f, user);
				_CURRENT_DIR = _CURRENT_DIR.getParent();
			}
			// 2. If the file is a file, add it to the FS tree, then fills the
			// FS node with its content
			else {
				com.gwos.client.domain.impl.File file = new com.gwos.client.domain.impl.File(
						f.getName(), _CURRENT_DIR, user, FileType.TEXT);
				if (f.canRead() == false) {
					continue;
				}
				file.setContent(getFileContent(f));
				_CURRENT_DIR.addChildNode(file);
			}
		}
	}

	// //////////////////////////
	// 3. SAVE FS PART
	// //////////////////////////

	/**
	 * At logout, save the session file system to the IO file system
	 * 
	 * @param user
	 * @param rootFolder
	 */

	@Override
	public boolean saveSessionFS(User user, Directory rootFolder) {

		// If the session FS is null : stop
		if (rootFolder == null) {
			return false;
		}

		try {
			// Get the user storage folder
			File userFolder = getUserSpaceFolder(user);
			if (userFolder == null) {
				return false;
			}
			// Delete all existing files
			for (File file : userFolder.listFiles()) {
				boolean deleted = file.delete();
				if (file.isDirectory() && deleted == false) {
					deleteRecursively(file);
					file.delete();
				}
			}
			// Creates new files from the user session
			buildIOFS(rootFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Delete a folder recursively (because the java deletion method for file,
	 * actually delete if the folder is empty to be sure...)
	 * 
	 * @param f
	 */

	private void deleteRecursively(File f) {

		for (File subFile : f.listFiles()) {
			if (subFile.isDirectory()) {
				deleteRecursively(subFile);
				subFile.delete();
			} else if (subFile.isFile()) {
				subFile.delete();
			}
		}
	}

	/**
	 * Build the IO FS from the session FS
	 * 
	 * @param dir
	 */

	private void buildIOFS(Directory dir) {

		for (INode node : dir.getChildren().values()) {
			if (node.getNodeType() == NodeType.DIR) {
				createIONode(node);
				buildIOFS((Directory) node);
			} else if (node.getNodeType() == NodeType.FILE) {
				createIONode(node);
			}
		}
	}
}
