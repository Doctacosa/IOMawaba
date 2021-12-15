package com.interordi.iomawaba.listeners.bungee;

import com.interordi.iomawaba.modules.Bans;
import com.interordi.iomawaba.utilities.BanData;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayersListener implements Listener {

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		BanData ban = Bans.getInstance().isBanned(
			event.getPlayer().getUniqueId(),
			event.getPlayer().getAddress().getHostString()
		);

		if (ban != null) {
			String message = Bans.formatMessageTarget(ban);

			event.getPlayer().disconnect(new TextComponent(message));
		}

	}
	
}
