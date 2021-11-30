package com.interordi.iomawaba;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import com.interordi.iomawaba.commands.GKick;
import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.listeners.bungee.PlayersListener;
import com.interordi.iomawaba.modules.PlayerActionsBungee;
import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.Database;

public class IOMawabaBungee extends Plugin {

	public static IOMawabaBungee instance;
	
	public Database db = null;

	public Warnings warnings;

	
	public void onEnable() {
		instance = this;
		
		PlayerActions pa = new PlayerActionsBungee();

		getProxy().getPluginManager().registerListener(this, new PlayersListener());

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new GKick(pa));

		getLogger().info("IOMawaba enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IOMawaba disabled");
	}
}
