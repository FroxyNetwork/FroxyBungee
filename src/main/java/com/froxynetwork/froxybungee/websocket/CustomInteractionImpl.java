package com.froxynetwork.froxybungee.websocket;

import com.froxynetwork.froxybungee.Froxy;
import com.froxynetwork.froxynetwork.network.websocket.CustomInteraction;

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
 * Implementation of {@link CustomInteraction}
 */
public class CustomInteractionImpl implements CustomInteraction {

	@Override
	public void stop(String msg) {
		// Stop the server
		// TODO Kick players
		Froxy.bungee().stop();
	}
}
