package com.froxynetwork.froxybungee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

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
public class Config {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private File fichierConfig = null;
	private Configuration config = null;

	public Config(File file) {
		this.fichierConfig = file;
		loadConfig();
	}

	public void save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fichierConfig);
		} catch (IOException e) {
			LOG.error("An error has occured while saving file {}", fichierConfig.getPath());
		}
	}

	public void loadConfig() {
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fichierConfig);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void set(String path, Object obj) {
		config.set(path, obj);
		save();
	}

	public String getString(String path) {
		return getString(path, new Object[] {});
	}

	public String getString(String path, Object... lists) {
		String name = config.getString(path);
		if (name != null && lists != null)
			for (int i = 0; i < lists.length; i++)
				name = name.replace("{" + i + "}", lists[i].toString());

		return name == null ? null : name.replace("&", "§");
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public long getLong(String path) {
		return config.getLong(path);
	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public double getDouble(String path) {
		return config.getDouble(path);
	}

	public ArrayList<String> getStringList(String path) {
		ArrayList<String> name = new ArrayList<String>();
		for (String nom : config.getStringList(path))
			name.add(nom.replace("&", "§"));
		return name;
	}

	public ArrayList<Integer> getIntegerList(String path) {
		ArrayList<Integer> name = new ArrayList<Integer>();
		for (int nom : config.getIntList(path))
			name.add(nom);
		return name;
	}

	public ArrayList<String> getKeys(String path) {
		ArrayList<String> list = new ArrayList<>();
		if ("".equalsIgnoreCase(path)) {
			for (String section : config.getKeys())
				list.add(section);
		} else {
			Configuration cs = config.getSection(path);
			if (cs == null)
				return list;
			for (String section : cs.getKeys())
				list.add(section);
		}
		return list;
	}

	public boolean exist(String path) {
		return config.contains(path);
	}
	
	public void reload() {
		loadConfig();
	}
}
