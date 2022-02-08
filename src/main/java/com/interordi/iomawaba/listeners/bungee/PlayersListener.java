package com.interordi.iomawaba.listeners.bungee;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.interordi.iomawaba.modules.Bans;
import com.interordi.iomawaba.utilities.BanData;
import com.interordi.iomawaba.utilities.Database;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayersListener implements Listener {

	Set< UUID > knownIds = new HashSet< UUID >();
	Database db;

	@EventHandler
	public void onLoginEvent(LoginEvent event) {

		//Record this player if unknown
		//This will trigger only once per execution to save on speed
		if (!knownIds.contains(event.getConnection().getUniqueId())) {
			new Thread(() -> {
				db.savePlayerRecord(
					event.getConnection().getUniqueId(),
					event.getConnection().getName(),
					event.getConnection().getAddress().getHostString()
				);
			}).start();
		}

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
		//System.out.println("Trying to join " + event.getTarget().getName());
	}


	public void setDatabase(Database db) {
		this.db = db;
	}
}
