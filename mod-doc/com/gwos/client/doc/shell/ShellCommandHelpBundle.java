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

package com.gwos.client.doc.shell;

import java.util.HashMap;
import java.util.Map;

import com.gwos.client.core.shell.ShellCommandsBundle;

public class ShellCommandHelpBundle {

	private static ShellCommandHelpBundle _INSTANCE;
	private Map<String, String> helpMap;

	private ShellCommandHelpBundle() {
		helpMap = new HashMap<String, String>();
		initHelp();
	}

	public static ShellCommandHelpBundle getInstance() {
		if (null == _INSTANCE) {
			_INSTANCE = new ShellCommandHelpBundle();
		}
		return _INSTANCE;
	}

	private void initHelp() {
		helpMap.put(
				ShellCommandsBundle._CAT_FILE,
				"Affiche le contenu d'un fichier présent sur le système de fichier. \nUsage : \"cat chemin_fichier\".");
		helpMap.put(ShellCommandsBundle._CLEAR_SHELL,
				"Efface le contenu du shell.");
		helpMap.put(ShellCommandsBundle._FS_CHANGE_DIR,
				"Change le dossier courant.");
		helpMap.put(ShellCommandsBundle._FS_CURRENT_DIR,
				"Retourne le chemin du dossier courant.");
		helpMap.put(ShellCommandsBundle._FS_TREE,
				"Affiche l'arborescence du système de fichier");
		helpMap.put(ShellCommandsBundle._HELP_SHELL, "Affiche l'aide");
		helpMap.put(ShellCommandsBundle._KILL_PROCESS_COMMAND,
				"Detruit un processus. \nUsage : \"kill pid\"");
		helpMap.put(
				ShellCommandsBundle._LAUNCH_PROCESS_COMMAND,
				"Lance une application. \nUsage : ! shell. \n"
						+ "La liste des applications installées est disponible par la commande applist.");
		helpMap.put(ShellCommandsBundle._INSTALLED_APP_COMMAND,
				"Liste les applications installées sur le système.");
		helpMap.put(
				ShellCommandsBundle._LIST_DETAILED_DIR,
				"Liste les fichiers du dossier de manière détaillée. \nUsage :  \"ll\" ou \"ll /Music\"");
		helpMap.put(
				ShellCommandsBundle._LIST_SHORT_DIR,
				"List les fichiers du dossier de manière simple. \nUsage :  \"ls\" ou \"ls /Music\"");
		helpMap.put(ShellCommandsBundle._PROCESSES_LIST_COMMAND,
				"Liste les processus en cours d'éxecution.");
		helpMap.put(ShellCommandsBundle._SESSION_LOGOUT_COMMAND,
				"Ferme la session en cours.");
	}

	public String getCommandHelp(String command) {
		return helpMap.get(command);
	}

	public Map<String, String> getCommandsHelp() {
		return helpMap;
	}
}
