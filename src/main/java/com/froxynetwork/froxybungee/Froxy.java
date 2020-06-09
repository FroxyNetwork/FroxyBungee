package com.froxynetwork.froxybungee;

import com.froxynetwork.froxybungee.server.ServerManager;
import com.froxynetwork.froxybungee.websocket.WebSocketManager;
import com.froxynetwork.froxynetwork.network.NetworkManager;
import com.froxynetwork.froxynetwork.network.output.data.server.ServerDataOutput.Server;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

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
/**
 * Represents the API core, for version and API singleton handling
 */
public final class Froxy {
	private static boolean init = false;

	@Getter
	private static WebSocketManager webSocketManager;
	@Getter
	private static NetworkManager networkManager;
	@Getter
	private static ServerManager serverManager;
	@Getter
	private static Server server;

	private static ProxyServer bungee;

	private Froxy() {
	}

	public static void init(WebSocketManager webSocketManager, NetworkManager networkManager,
			ServerManager serverManager, ProxyServer bungee, Server server) {
		if (init)
			throw new UnsupportedOperationException("Cannot call init if already loaded");
		Froxy.webSocketManager = webSocketManager;
		Froxy.networkManager = networkManager;
		Froxy.bungee = bungee;
		Froxy.serverManager = serverManager;
		Froxy.server = server;
		init = true;
	}

	// ------------------------------------------------------------

	/**
	 * @return A {@link ProxyServer} instance for using bungee
	 */
	public static ProxyServer bungee() {
		return bungee;
	}
}
