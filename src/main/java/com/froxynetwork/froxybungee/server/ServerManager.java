package com.froxynetwork.froxybungee.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.output.Callback;
import com.froxynetwork.froxynetwork.network.output.RestException;
import com.froxynetwork.froxynetwork.network.output.data.server.ServerDataOutput;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

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
public class ServerManager {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private Map<String, Server> servers;

	public ServerManager() {
		servers = new HashMap<>();
	}

	public void registerServer(String id, String host, int port, String motd) {
		LOG.info("Registering a new server in async mode ! (id = {}, host = {}, port = {}, motd = {})", id, host, port,
				motd);
		Froxy.getNetworkManager().getNetwork().getServerService().asyncGetServer(id,
				new Callback<ServerDataOutput.Server>() {

					@Override
					public void onResponse(ServerDataOutput.Server srv) {
						// All is ok
						LOG.info("Ok for server {}", id);
						Server server = new Server(srv);
						servers.put(id, server);
						LOG.info("Registering server {} in Bungee", id);
						ServerInfo info = ProxyServer.getInstance().constructServerInfo(id,
								InetSocketAddress.createUnresolved(host, port), motd, false);
						Froxy.bungee().getServers().put(id, info);
					}

					@Override
					public void onFailure(RestException ex) {
						// TODO
					}

					@Override
					public void onFatalFailure(Throwable ex) {
						// TODO
					}
				});
	}

	public void unregisterServer(String id) {
		LOG.info("Unregistering server {} !", id);
		ServerInfo si = ProxyServer.getInstance().getServers().remove(id);
		if (si == null) {
			// Whut ?
			LOG.error("Unknown server {} was unregistered", id);
		}
	}

	public Server getServer(String id) {
		return servers.get(id);
	}
}
