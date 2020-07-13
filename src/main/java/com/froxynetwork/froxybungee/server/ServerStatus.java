package com.froxynetwork.froxybungee.server;

import com.froxynetwork.froxynetwork.network.output.data.server.ServerDataOutput;

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
 * The status of the server
 */
public enum ServerStatus {
	STARTING, //
	WAITING, //
	STARTED, //
	ENDING, //
	ENDED;

	public static ServerStatus fromNetwork(ServerDataOutput.ServerStatus ss) {
		switch (ss) {
		case STARTING:
			return STARTING;
		case WAITING:
			return WAITING;
		case STARTED:
			return STARTED;
		case ENDING:
			return ENDING;
		default:
			return ENDED;
		}
	}

	public static ServerDataOutput.ServerStatus fromBungee(ServerStatus ss) {
		switch (ss) {
		case STARTING:
			return ServerDataOutput.ServerStatus.STARTING;
		case WAITING:
			return ServerDataOutput.ServerStatus.WAITING;
		case STARTED:
			return ServerDataOutput.ServerStatus.STARTED;
		case ENDING:
			return ServerDataOutput.ServerStatus.ENDING;
		default:
			return ServerDataOutput.ServerStatus.ENDED;
		}
	}
}
