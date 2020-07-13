package com.froxynetwork.froxybungee.websocket;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.framing.CloseFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxybungee.websocket.commands.ServerRegisterCommander;
import com.froxynetwork.froxybungee.websocket.commands.ServerStopCommand;
import com.froxynetwork.froxybungee.websocket.commands.ServerUnregisterCommander;
import com.froxynetwork.froxynetwork.network.websocket.WebSocketClientImpl;
import com.froxynetwork.froxynetwork.network.websocket.WebSocketFactory;
import com.froxynetwork.froxynetwork.network.websocket.auth.WebSocketTokenAuthentication;
import com.froxynetwork.froxynetwork.network.websocket.modules.WebSocketAutoReconnectModule;

import lombok.Getter;

/**
 * FroxyBungee
 * Copyright (C) 2020 FroxyNetwork
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

	private URI websocketURI;
	@Getter
	private WebSocketClientImpl client;
	private boolean stop = false;

	public WebSocketManager(URI websocketURI) {
		this.websocketURI = websocketURI;
	}

	public void login() throws URISyntaxException {
		LOG.debug("login()");
		if (client != null && client.isConnected()) {
			LOG.debug("login(): client already connected");
			return;
		}
		client = WebSocketFactory.client(websocketURI, new WebSocketTokenAuthentication(Froxy.getNetworkManager()));
		client.registerWebSocketAuthentication(() -> {
			// TODO
		});

		// Commands
		client.registerCommand(new ServerRegisterCommander());
		client.registerCommand(new ServerUnregisterCommander());
		client.registerCommand(new ServerStopCommand());

		WebSocketAutoReconnectModule wsarm = new WebSocketAutoReconnectModule(5000);
		client.registerWebSocketDisconnection(remote -> {
			if (!stop)
				return;
			wsarm.unload();
		});
		client.addModule(wsarm);

		// Commands

		LOG.debug("login() ok");
	}

	private boolean loaded = false;

	public void load() throws URISyntaxException {
		if (loaded)
			return;
		login();
		loaded = true;
	}

	public void stop() {
		stop = true;
		client.disconnect(CloseFrame.NORMAL, "");
		client.closeAll();
	}
}
