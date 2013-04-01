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

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.gwos.client.context.GwosLogger;
import com.gwos.client.core.events.SystemEventManager;
import com.gwos.client.core.events.SystemMessageEvent;
import com.gwos.client.core.events.SystemMessageHandler;

public class SystemMessageDisplayer {

    public static void listenToMessages() {

        SystemEventManager.getInstance().addEventHandler(
                SystemMessageEvent.TYPE, new SystemMessageHandler() {

            @Override
            public void onSystemMessageEvent(SystemMessageEvent event) {

                GwosLogger.getLogger().warning(
                        "SystemMessage (" + event.getMessageType()
                                + ") : " + event.getMessage());

                switch (event.getSource()) {
                    case NON_UI_APP:
                        return;
                }
                switch (event.getMessageType()) {
                    case RIGHTS_VIOLATION:
                    case FS_ERROR:
                    case INVALID_ARG:
                        MessageBox.alert("Error !", event.getMessage(),
                                null);
                        break;
                }
            }
        });
    }

}
