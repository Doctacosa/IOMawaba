package com.interordi.iomawaba.modules;

import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.Database;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerActionsBungee implements PlayerActions {

	Database db;
	

	public PlayerActionsBungee(Database db) {
		this.db = db;
	}


	@Override
	public boolean warnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxyServer.getInstance().getLogger().warning("The warnings aren't defined at the proxy level.");
		return false;
	}


	@Override
	public boolean kickPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}

		target.disconnect(new ComponentBuilder(message).create());
		
		return true;
	}


	@Override
	public boolean tempBanPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}

		target.disconnect(new ComponentBuilder(message).create());

		return true;
	}


	@Override
	public boolean banPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}

		db.banTarget(target.getUniqueId(), null, sourceUuid, sourceName, null, null, message);

		target.disconnect(new ComponentBuilder(message).create());

		return true;
	}


	@Override
	public boolean banIp(String ip, UUID sourceUuid, String sourceName, String message) {
		//TODO: Scan all current players
		return false;
	}


	@Override
	public boolean unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		/*
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}
		*/
		//TODO: Scan database and unwarn if possible
		return false;
	}


	@Override
	public boolean unbanPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		/*
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}
		*/



		//TODO: Scan database and unban if possible
		return false;
	}


	@Override
	public boolean unbanIp(String ip, UUID sourceUuid, String sourceName, String message) {
		//TODO: Scan database and unban if possible
		return false;
	}
	
}
