package com.froxynetwork.froxybungee.websocket;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;

import lombok.Getter;

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
