package com.froxynetwork.froxybungee.websocket.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

import net.md_5.bungee.api.ProxyServer;

/**
 * FroxyBungee
 * 
 * Copyright (C) 2020 FroxyNetwork
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author 0ddlyoko
 */
public class ServerStopCommand implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public String name() {
		return "stop";
	}

	@Override
	public String description() {
		return "Stop this server";
	}

	@Override
	public void onReceive(String message) {
		LOG.info("Got stop command, stopping ...");
		// TODO Find a better way to shutdown this server
		ProxyServer.getInstance().stop("Stopping ...");
	}
}
