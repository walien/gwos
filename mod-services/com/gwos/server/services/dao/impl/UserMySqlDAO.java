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

package com.gwos.server.services.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletContext;

import com.gwos.client.context.GwosLogger;
import com.gwos.client.domain.impl.User;
import com.gwos.client.domain.impl.UserGroup;
import com.gwos.server.services.dao.UserDAO;
import com.gwos.server.services.utils.ServiceUtils;

public class UserMySqlDAO implements UserDAO {

	private static ServiceUtils _SERVICE_UTILS;
	private static final String _MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	private static String _DB_URL;
	private static String _DB_USER;
	private static String _DB_PASSWORD;
	private static Connection _CONNECTION;

	private static final String _ADD_USER_QUERY = "INSERT INTO app_users VALUES (?, ?, ?, ?, ?, ?);";
	private static final String _GET_USER_QUERY = "SELECT * FROM app_users WHERE username = ? AND password = ?;";
	private static final String _GET_ALL_USERS_QUERY = "SELECT * FROM app_users;";
	private static final String _DELETE_USER_QUERY = "DELETE FROM app_users WHERE username = ?;";

	private static void loadSettings() {

		if (_SERVICE_UTILS == null) {
			GwosLogger.getLogger().log(Level.SEVERE,
					"The Service Util is not properly initialized !");
			return;
		}
		_DB_URL = _SERVICE_UTILS.getDBUrl();
		_DB_USER = _SERVICE_UTILS.getDBLogin();
		_DB_PASSWORD = _SERVICE_UTILS.getDBPass();

		GwosLogger.getLogger().info(
				"DB Settings are : \"" + _DB_URL + "(" + _DB_USER + "/"
						+ _DB_PASSWORD + ")");
	}

	private static Connection getConnection() {
		if (_CONNECTION == null) {
			loadSettings();
			try {
				Class.forName(_MYSQL_DRIVER);
				_CONNECTION = DriverManager.getConnection(_DB_URL, _DB_USER,
						_DB_PASSWORD);
				GwosLogger.getLogger().info(
						"Connected to database : \"" + _DB_URL
								+ "\" (logged in : \"" + _DB_USER + "\")");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _CONNECTION;
	}

	@Override
	public void closeConnection() {
		if (_CONNECTION == null) {
			return;
		}
		try {
			_CONNECTION.close();
			_CONNECTION = null;
			GwosLogger.getLogger().info("Connection closed on user database !");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setCallingContext(ServletContext context) {
		_SERVICE_UTILS = new ServiceUtils(context);
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>(0);
		try {
			ResultSet rs = getConnection().prepareStatement(
					_GET_ALL_USERS_QUERY).executeQuery();
			while (rs.next()) {
				users.add(new User(rs.getString(3), rs.getString(4), rs
						.getString(1), rs.getString(2), rs.getString(5),
						UserGroup.fromCode(rs.getInt(6))));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User getUser(String username, String passwd) {
		try {
			PreparedStatement stmt = getConnection().prepareStatement(
					_GET_USER_QUERY);
			stmt.setString(1, username);
			stmt.setString(2, passwd);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return new User(rs.getString(3), rs.getString(4),
						rs.getString(1), rs.getString(2), rs.getString(5),
						UserGroup.fromCode(rs.getInt(6)));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addUser(User user) {
		try {
			PreparedStatement stmt = getConnection().prepareStatement(
					_ADD_USER_QUERY);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getGroup().getCode());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteUser(String username) {

		try {
			PreparedStatement stmt = getConnection().prepareStatement(
					_DELETE_USER_QUERY);
			stmt.setString(1, username);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
