package com.froxynetwork.froxybungee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.server.ServerManager;
import com.froxynetwork.froxybungee.websocket.WebSocketManager;
import com.froxynetwork.froxygame.languages.LanguageManager;
import com.froxynetwork.froxynetwork.network.NetworkManager;
import com.froxynetwork.froxynetwork.network.output.data.server.ServerDataOutput.Server;

import net.md_5.bungee.api.plugin.Plugin;

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
public class FroxyBungee extends Plugin {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private Config config;

	@Override
	public void onEnable() {
		try {
			LOG.info("Starting Bungee, please wait");
			// Config
			config = new Config(new File("plugins" + File.separator + "FroxyBungee" + File.separator + "config.yml"));
			LOG.info("Reading auth file ...");
			// Auth file
			String[] auth = readAuthFile();
			if (auth == null || auth.length != 2 || !checkNotNullOrEmpty(auth)) {
				LOG.error("Auth file is null or there is missing lines ! Stopping ...");
				getProxy().stop();
				return;
			}
			LOG.info("Done");

			LOG.info("Contacting REST ...");
			// Contacting rest
			NetworkManager networkManager = new NetworkManager(config.getString("url"), auth[0], auth[1]);
			Server srv = networkManager.getNetwork().getServerService().syncGetServer(auth[0]);
			LOG.info("Done");

			LOG.info("Loading Managers ...");
			LOG.info("WebSocketManager ...");
			WebSocketManager webSocketManager = new WebSocketManager(new URI(config.getString("websocket")));
			LOG.info("LanguageManager ...");
			// Register lang directory. Doing that will load this class
			File lang = new File("plugins" + File.separator + "FroxyBungee" + File.separator + "lang");
			LanguageManager.register(lang);
			LOG.info("ServerManager ...");
			ServerManager serverManager = new ServerManager();
			LOG.info("Done");

			LOG.info("Doing some stuff ...");
			// Initializing api
			Froxy.init(webSocketManager, networkManager, serverManager, getProxy(), srv);
			LOG.info("Done");

			LOG.info("Starting Thread for WebSocket checker ...");
			webSocketManager.load();
			LOG.info("Done");

			LOG.info("Bungee started !");
		} catch (Exception ex) {
			LOG.error("An error has occured while loading the bungee: ", ex);
			// Stop it
			getProxy().stop();
		}
	}

	@Override
	public void onDisable() {
		LOG.info("Stopping Bungee, please wait");
		Froxy.getWebSocketManager().stop();
		LOG.info("Bungee stopped !");
	}

	/**
	 * @return [id, client_secret]
	 */
	private String[] readAuthFile() {
		// The file name
		String fileName = "plugins" + File.separator + "FroxyBungee" + File.separator + "auth";
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String[] result = new String[] { reader.readLine(), reader.readLine() };
			return result;
		} catch (FileNotFoundException ex) {
			LOG.error("auth file not found");
			return new String[] {};
		} catch (IOException ex) {
			LOG.error("Exception while reading auth file:", ex);
			return new String[] {};
		}
	}

	private boolean checkNotNullOrEmpty(String[] arr) {
		if (arr == null)
			return false;
		for (String str : arr)
			if (str == null || "".equalsIgnoreCase(str.trim()))
				return false;
		return true;
	}
}
