package com.froxynetwork.froxybungee.websocket;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;

import lombok.Getter;

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
 * A manager for the WebSocket
 */
public class WebSocketManager {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private String url;
	private String clientId;

	@Getter
	private com.froxynetwork.froxynetwork.network.websocket.WebSocketManager webSocketManager;

	private boolean runThread;
	private int timeout = 10;
	private Thread connectThread;
	private Thread thread;

	public WebSocketManager(String url, String clientId, CustomInteractionImpl customInteractionImpl)
			throws URISyntaxException {
		this.webSocketManager = new com.froxynetwork.froxynetwork.network.websocket.WebSocketManager(url,
				customInteractionImpl);
		this.clientId = clientId;

		// This Thread is used to reconnect to the WebSocket if there is a problem (like
		// a disconnection)
		this.thread = new Thread(() -> {
			// First loop if we interrupt the Thread
			while (!Thread.interrupted() && runThread) {
				// If we are not connected, we try to connect to the WebSocket
				if (!webSocketManager.isConnected()) {
					LOG.info("WebSocket not connected, initializing connection ...");
					// Not connected
					if (connectThread != null) {
						// Already connecting ...
						LOG.info("Checking again in 2 seconds");
						try {
							// Wait 2 seconds
							Thread.sleep(2000);
						} catch (InterruptedException ex) {
							// No need to catch this exception
						}
						if (webSocketManager.isConnected()) {
							LOG.info("Nevermind, we're now connected");
						} else {
							// Still not connected
							LOG.error("Still not connected, disconnecting ...");
							// We interrupt the Thread
							if (connectThread != null && connectThread.isAlive())
								connectThread.interrupt();
							// Disconnecting ...
							webSocketManager.disconnect();
							connectThread = null;
						}
					} else {
						// Connect
						connect();
					}
				} else if (!webSocketManager.isAuthentified()) {
					LOG.info("The bungee is connected to the WebSocket but not authentified, checking in 2 seconds");
					try {
						// Wait 2 seconds
						Thread.sleep(2000);
					} catch (InterruptedException ex) {
						// No need to catch this exception
					}
					if (webSocketManager.isAuthentified()) {
						LOG.info("Nevermind, we're now connected");
					} else {
						// Not authentified
						LOG.error("Still not authentified, disconnecting ...");
						// We interrupt the Thread
						if (connectThread != null && connectThread.isAlive())
							connectThread.interrupt();
						webSocketManager.disconnect();
						connectThread = null;
					}
				}
				try {
					// We timeout here to avoid always checking
					Thread.sleep(timeout * 1000);
				} catch (InterruptedException ex) {
					// No need to catch this exception
				}
			}
		}, "WebSocketManager - AutoReconnect");
	}

	public void startThread() {
		if (runThread)
			return;
		// Start
		this.runThread = true;
		thread.start();
	}

	/**
	 * Connects to the WebSocket and authentificates itself<br />
	 * This method create a new Thread ! To be notified, see
	 * {@link com.froxynetwork.froxynetwork.network.websocket.WebSocketManager#registerWebSocketAuthentified(Runnable)}
	 */
	private void connect() {
		connectThread = new Thread(() -> {
			if (Froxy.getNetworkManager().isTokenExpired()) {
				LOG.info("Expired token, asking another one");
				// Expired token, asking another one
				try {
					// Used to get a new token
					Froxy.getNetworkManager().getNetwork().getServerConfigService().syncGetServerConfig();
				} catch (Exception ex) {
					// We don't have to handle exceptions (even if an exception here is strange)
					// because we only need the token
				}
				// Test if token is still expired
				if (Froxy.getNetworkManager().isTokenExpired()) {
					// Error
					LOG.error("Token is still expired !");
					return;
				}
			}
			webSocketManager.connect("bungeecord", clientId, Froxy.getNetworkManager().getAuthenticationToken());
		}, "WebSocketManager-core-connect");
		connectThread.start();
	}

	public void disconnect() {
		webSocketManager.disconnect();
		connectThread = null;
	}

	public void stop() {
		disconnect();
		runThread = false;
	}
}
