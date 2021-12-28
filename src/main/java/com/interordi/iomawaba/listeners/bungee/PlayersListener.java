package com.interordi.iomawaba.listeners.bungee;

import com.interordi.iomawaba.modules.Bans;
import com.interordi.iomawaba.utilities.BanData;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayersListener implements Listener {

	@EventHandler
	public void onLoginEvent(LoginEvent event) {
		BanData ban = Bans.getInstance().isBanned(
			event.getConnection().getUniqueId(),
			event.getConnection().getAddress().getHostString()
		);

		if (ban != null) {
			String message = Bans.formatMessageTarget(ban);

			event.setCancelled(true);
			event.setCancelReason(new TextComponent(message));
		}

	}


	@EventHandler
	public void onServerConnectEvent(ServerConnectEvent event) {
		//TODO: Server-specific check
	}
}
