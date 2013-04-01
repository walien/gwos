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

package com.gwos.server.services.utils;

import com.gwos.server.services.conf.ConfigurationConstants;

import javax.servlet.ServletContext;

public class ServiceUtils {

    private ServletContext context;

    public ServiceUtils(ServletContext context) {
        this.context = context;
    }

    public ServletContext getServletContext() {
        return context;
    }

    public String getFSPath() {
        return getClass().getResource(getServletContext().getInitParameter(
                ConfigurationConstants.GWOS_FS_ROOT_PATH)).getPath();
    }

    public String getDBUrl() {
        return getServletContext().getInitParameter(
                ConfigurationConstants.GWOS_DB_URL);
    }

    public String getDBLogin() {
        return getServletContext().getInitParameter(
                ConfigurationConstants.GWOS_DB_LOGIN);
    }

    public String getDBPass() {
        return getServletContext().getInitParameter(
                ConfigurationConstants.GWOS_DB_PASS);
    }

}
