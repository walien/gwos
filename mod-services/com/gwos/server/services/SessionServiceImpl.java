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

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwos.client.domain.impl.User;
import com.gwos.client.services.SessionService;
import com.gwos.server.services.dao.DAOFactory;
import com.gwos.server.services.dao.UserDAO;
import com.gwos.server.services.utils.HasParameters;

public class SessionServiceImpl extends RemoteServiceServlet implements
		SessionService, HasParameters {

	private static final long serialVersionUID = -8166183092055617548L;
	private static final int _DB_TYPE = DAOFactory.MYSQL_FACTORY;

	private ServletConfig config;

	// //////////////////////////
	// // 1. WEB.XML PARAMETERS
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

	// ////////////////////////////////////
	// // 2. DB CONNECTION MANAGEMENT
	// ////////////////////////////////////

	@Override
	public void closeUserConnection() {
		DAOFactory.getDAOFactory(_DB_TYPE).getUserDAO().closeConnection();
	}

	// //////////////////////////
	// // 3. DATA ACCESS
	// //////////////////////////

	@Override
	public List<User> getAllUsers() {
		UserDAO dao = DAOFactory.getDAOFactory(_DB_TYPE).getUserDAO();
		dao.setCallingContext(getServletContext());
		List<User> users = dao.getAllUsers();
		closeUserConnection();
		return users;
	}

	@Override
	public boolean addUser(User user) {
		UserDAO dao = DAOFactory.getDAOFactory(_DB_TYPE).getUserDAO();
		dao.setCallingContext(getServletContext());
		boolean result = dao.addUser(user);
		closeUserConnection();
		return result;
	}

	@Override
	public User getUser(String username, String passwd) {
		UserDAO dao = DAOFactory.getDAOFactory(_DB_TYPE).getUserDAO();
		dao.setCallingContext(getServletContext());
		User user = dao.getUser(username, passwd);
		closeUserConnection();
		return user;
	}

	@Override
	public boolean deleteUser(String username) {
		UserDAO dao = DAOFactory.getDAOFactory(_DB_TYPE).getUserDAO();
		dao.setCallingContext(getServletContext());
		boolean result = dao.deleteUser(username);
		closeUserConnection();
		return result;
	}
}
