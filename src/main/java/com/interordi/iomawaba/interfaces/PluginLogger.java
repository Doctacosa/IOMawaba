package com.interordi.iomawaba.interfaces;

public interface PluginLogger {

	static PluginLogger instance = null;

	abstract void info(String message);
	abstract void warning(String message);
	abstract PluginLogger getInstance();
	
}
