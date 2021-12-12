package com.interordi.iomawaba.modules;

import com.interordi.iomawaba.interfaces.PluginLogger;

public class PluginLoggerBungee implements PluginLogger {

	public PluginLoggerBungee() {}


	@Override
	public void info(String message) {
		System.out.println(message);
	}

	@Override
	public void warning(String message) {
		System.err.println(message);
	}

	@Override
	public PluginLogger getInstance() {
		return new PluginLoggerBungee();
	}

}
