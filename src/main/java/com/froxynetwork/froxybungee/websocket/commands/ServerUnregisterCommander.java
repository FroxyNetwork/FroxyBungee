package com.froxynetwork.froxybungee.websocket.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * FroxyBungee
 * Copyright (C) 2019 FroxyNetwork
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author 0ddlyoko
 */
public class ServerUnregisterCommander implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public String name() {
		return "WebSocketUnregister";
	}

	@Override
	public String description() {
		return "Unregister a new Server";
	}

	@Override
	public From from() {
		return From.WEBSOCKET;
	}

	@Override
	public void onReceive(String from, String message) {
		// message = <serverId>
		String serverId = message;

		LOG.info("Unregistering server {} !", serverId);
		ServerInfo si = ProxyServer.getInstance().getServers().remove(serverId);
		if (si == null) {
			// Whut ?
			LOG.error("Unknown server {} was unregistered", serverId);
		}
	}
}
