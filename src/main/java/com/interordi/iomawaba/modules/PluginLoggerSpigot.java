package com.interordi.iomawaba.modules;

import com.interordi.iomawaba.interfaces.PluginLogger;

import org.bukkit.Bukkit;

public class PluginLoggerSpigot implements PluginLogger {

	public PluginLoggerSpigot() {}


	@Override
	public void info(String message) {
		Bukkit.getLogger().info(message);
	}

	@Override
	public void warning(String message) {
		Bukkit.getLogger().warning(message);
	}

	@Override
	public PluginLogger getInstance() {
		return new PluginLoggerSpigot();
	}

}
