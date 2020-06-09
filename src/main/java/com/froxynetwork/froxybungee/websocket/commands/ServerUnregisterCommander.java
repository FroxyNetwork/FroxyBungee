package com.froxynetwork.froxybungee.websocket.commands;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

/**
 * FroxyBungee Copyright (C) 2019 FroxyNetwork
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author 0ddlyoko
 */
public class ServerUnregisterCommander implements IWebSocketCommander {

	@Override
	public String name() {
		return "WebSocketUnregister";
	}

	@Override
	public String description() {
		return "Unregister a new Server";
	}

	@Override
	public void onReceive(String message) {
		// <serverId>
		String serverId = message;
		Froxy.getServerManager().unregisterServer(serverId);
	}
}
