package com.froxynetwork.froxybungee.server;

import java.util.Date;

import com.froxynetwork.froxynetwork.network.output.data.server.ServerDataOutput;

import lombok.Getter;
import lombok.Setter;

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
@Getter
public class Server {
	private ServerDataOutput.Server restServer;
	private String id;
	private String name;
	private String type;
	private int port;
	@Setter
	private ServerStatus status;
	private Date creationTime;
	@Setter
	private Date endTime;

	public Server(ServerDataOutput.Server restServer) {
		this.restServer = restServer;
		this.id = restServer.getId();
		this.name = restServer.getName();
		this.type = restServer.getType();
		this.port = restServer.getPort();
		this.status = ServerStatus.fromNetwork(restServer.getStatus());
		this.creationTime = restServer.getCreationTime();
		this.endTime = restServer.getEndTime();
	}
}
