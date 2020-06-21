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
import com.froxynetwork.froxynetwork.network.output.data.server.ServerListDataOutput;
import com.froxynetwork.froxynetwork.network.service.ServerService.Type;

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
		// Clear servers
		Froxy.bungee().getServers().clear();
		// Load all servers
		try {
			ServerListDataOutput.ServerList list = Froxy.getNetworkManager().getNetwork().getServerService()
					.syncGetServers(Type.SERVER);
			LOG.info("Registering {} servers", list.getServers().size());
			for (ServerDataOutput.Server srv : list.getServers()) {
				// Register server
				_registerServer(srv);
			}
		} catch (RestException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void registerServer(String id, String type) {
		// Do not continue if it's a bungee
		if ("BUNGEE".equalsIgnoreCase(type))
			return;

		LOG.info("Registering a new server in async mode ! (id = {})", id);
		Froxy.getNetworkManager().getNetwork().getServerService().asyncGetServer(id,
				new Callback<ServerDataOutput.Server>() {

					@Override
					public void onResponse(ServerDataOutput.Server srv) {
						// All is ok
						_registerServer(srv);
					}

					@Override
					public void onFailure(RestException ex) {
						LOG.error("Error while retrieving server {}", id);
						LOG.error("", ex);
					}

					@Override
					public void onFatalFailure(Throwable ex) {
						LOG.error("Fatal Error while retrieving server {}", id);
						LOG.error("", ex);
					}
				});
	}

	private void _registerServer(ServerDataOutput.Server srv) {
		LOG.debug("Registering server {}, host = {}, port = {}", srv.getId(), srv.getIp(), srv.getPort());
		Server server = new Server(srv);
		servers.put(srv.getId(), server);
		ServerInfo info = ProxyServer.getInstance().constructServerInfo(srv.getId(),
				InetSocketAddress.createUnresolved(srv.getIp(), srv.getPort()), "", false);
		Froxy.bungee().getServers().put(srv.getId(), info);
	}

	public void unregisterServer(String id, String type) {
		// Do not continue if it's a bungee
		if ("BUNGEE".equalsIgnoreCase(type))
			return;

		LOG.info("Unregistering server {} !", id);
		servers.remove(id);
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
