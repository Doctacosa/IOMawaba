package com.interordi.iomawaba;

import net.md_5.bungee.api.plugin.Plugin;

import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.Database;

public class IOMawabaBungee extends Plugin {

	public static IOMawabaBungee instance;
	
	public Database db = null;

	public Warnings warnings;

	
	public void onEnable() {
		instance = this;

	}
	
	
	public void onDisable() {
		getLogger().info("IOMawaba disabled");
	}
}
