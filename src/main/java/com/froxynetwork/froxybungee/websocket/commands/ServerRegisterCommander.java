package com.froxynetwork.froxybungee.websocket.commands;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
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
public class ServerRegisterCommander implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public String name() {
		return "WebSocketRegister";
	}

	@Override
	public String description() {
		return "Register a new Server";
	}

	@Override
	public From from() {
		return From.WEBSOCKET;
	}

	@Override
	public void onReceive(String from, String message) {
		// message = <serverId | host | port | motd>
		String[] split = message.split(" ");
		if (split.length != 4) {
			// Error
			LOG.error("Invalid message: {}", message);
			return;
		}
		String serverId = split[0];
		String host = split[1];
		String strPort = split[2];
		int port = -1;
		String motd = split[3];
		try {
			port = Integer.parseInt(strPort);
		} catch (NumberFormatException ex) {
			LOG.error("Cannot parse port {}", strPort);
		}

		// All seams ok
		LOG.info("Registering a new server ! (id = {}, host = {}, port = {}, motd = {})", serverId, host, port, motd);
		ServerInfo info = ProxyServer.getInstance().constructServerInfo(serverId,
				InetSocketAddress.createUnresolved(host, port), motd, false);
		Froxy.bungee().getServers().put(serverId, info);
	}
}
