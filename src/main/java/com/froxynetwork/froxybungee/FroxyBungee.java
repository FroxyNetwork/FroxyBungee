package com.froxynetwork.froxybungee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.froxynetwork.froxybungee.websocket.CustomInteractionImpl;
import com.froxynetwork.froxybungee.websocket.WebSocketManager;
import com.froxynetwork.froxygame.languages.LanguageManager;
import com.froxynetwork.froxynetwork.network.NetworkManager;
import com.froxynetwork.froxynetwork.network.output.RestException;

import net.md_5.bungee.api.plugin.Plugin;

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
			try {
				// Used to generate the token
				networkManager.getNetwork().getServerConfigService().syncGetServerConfig();
			} catch (RestException ex) {
				LOG.error("Error {} while contacting REST server:", ex.getError().getErrorId());
				LOG.error("", ex);
				getProxy().stop();
				return;
			} catch (Exception ex) {
				LOG.error("Error while contacting REST server:", ex);
				getProxy().stop();
				return;
			}
			LOG.info("Done");

			LOG.info("Contacting WebSocket server");
			WebSocketManager webSocketManager;
			try {
				webSocketManager = new WebSocketManager(config.getString("websocket"), auth[0],
						new CustomInteractionImpl());
			} catch (URISyntaxException ex) {
				LOG.error("Invalid url while initializing WebSocket: ", ex);
				getProxy().stop();
				return;
			}
			LOG.info("Doing some stuff ...");
			// Initializing api
			Froxy.init(webSocketManager, networkManager, getProxy());
			LOG.info("Done");

			LOG.info("Starting Thread for WebSocket checker ...");
			webSocketManager.startThread();
			LOG.info("Done");

			// Register lang directory
			File lang = new File("plugins" + File.separator + "FroxyBungee" + File.separator + "lang");
			LanguageManager.register(lang);
			LOG.info("Bungee started !");
		} catch (Exception ex) {
			LOG.error("An error has occured while loading the bungee: ", ex);
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
	 * @return [id, client_id, client_secret]
	 */
	private String[] readAuthFile() {
		// The file name
		String fileName = "plugins" + File.separator + "FroxyCore" + File.separator + "auth";
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
