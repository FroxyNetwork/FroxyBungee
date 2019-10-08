package com.froxynetwork.froxybungee;

import com.froxynetwork.froxybungee.websocket.WebSocketManager;
import com.froxynetwork.froxynetwork.network.NetworkManager;

import net.md_5.bungee.api.ProxyServer;

/**
 * MIT License
 *
 * Copyright (c) 2019 FroxyNetwork
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
