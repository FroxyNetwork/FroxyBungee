package com.froxynetwork.froxybungee.websocket.commands;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.websocket.IWebSocketCommander;

/**
 * FroxyBungee Copyright (C) 2019 FroxyNetwork
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
public class ServerRegisterCommander implements IWebSocketCommander {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private Pattern spacePattern = Pattern.compile(" ");

	@Override
	public String name() {
		return "WebSocketRegister";
	}

	@Override
	public String description() {
		return "Register a new Server";
	}

	@Override
	public void onReceive(String message) {
		// message = <id> <type>
		String[] split = spacePattern.split(message);
		if (split.length < 2) {
			// Error
			LOG.error("Invalid message: {}", message);
			return;
		}
		String id = split[0];
		String type = split[1];
		// Do not continue if it's a bungee
		if (type.equalsIgnoreCase("BUNGEE"))
			return;

		// All seams ok
		Froxy.getServerManager().registerServer(id, type);
	}
}
