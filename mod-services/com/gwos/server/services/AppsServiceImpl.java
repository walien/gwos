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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwos.client.domain.impl.StatObject;
import com.gwos.client.domain.impl.User;
import com.gwos.client.services.AppsService;
import com.gwos.server.services.utils.ServiceUtils;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppsServiceImpl extends RemoteServiceServlet implements
		AppsService {

	private static final long serialVersionUID = -535881530441219366L;
	private ServletConfig config;

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
	// // 1. STATS
	// //////////////////////////

	@Override
	public StatObject getMemoryStats() {
		StatObject stats = new StatObject();

		// The FS root dir
		File rootDir = new File(
				new ServiceUtils(getServletContext()).getFSPath());
		stats.fileSystemTotalSpace = FileUtils.sizeOfDirectory(rootDir);

		// Call the service
		SessionServiceImpl service = new SessionServiceImpl();
		try {
			service.init(this.config);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		// Memory for each of app users
		Map<User, Long> memoryUsages = new HashMap<User, Long>();
		stats.memoryUsages = memoryUsages;
		for (User user : service.getAllUsers()) {
			File userDir = new File(rootDir.getAbsolutePath() + File.separator
					+ user.getUsername());
			if (userDir.exists() == false) {
				continue;
			}
			memoryUsages.put(user, FileUtils.sizeOfDirectory(userDir));
		}

		return stats;
	}
}
