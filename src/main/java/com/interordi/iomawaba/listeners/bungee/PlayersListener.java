package com.interordi.iomawaba.listeners.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayersListener implements Listener {

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			player.sendMessage(new TextComponent(event.getPlayer().getName() + " has joined the network."));
		}
	}
	
}
