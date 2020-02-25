package com.froxynetwork.froxybungee;

import com.froxynetwork.froxybungee.websocket.WebSocketManager;
import com.froxynetwork.froxynetwork.network.NetworkManager;

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

	private static WebSocketManager webSocketManager;
	private static NetworkManager networkManager;

	private static ProxyServer server;

	private Froxy() {
	}

	public static void init(WebSocketManager webSocketManager, NetworkManager networkManager, ProxyServer server) {
		if (init)
			throw new UnsupportedOperationException("Cannot call init if already loaded");
		Froxy.webSocketManager = webSocketManager;
		Froxy.networkManager = networkManager;
		Froxy.server = server;
		init = true;
	}

	public static WebSocketManager getWebSocketManager() {
		return webSocketManager;
	}

	public static NetworkManager getNetworkManager() {
		return networkManager;
	}

	// ------------------------------------------------------------

	/**
	 * @return A {@link ProxyServer} instance for using bungee
	 */
	public static ProxyServer bungee() {
		return server;
	}
}
