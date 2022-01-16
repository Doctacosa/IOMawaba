package com.interordi.iomawaba.modules;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.BanData;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.Database;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerActionsBungee implements PlayerActions {

	Database db;
	boolean useBroadcast = true;
	

	public PlayerActionsBungee(Database db) {
		this.db = db;
	}


	@Override
	public ControlCode warnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxyServer.getInstance().getLogger().warning("The warnings aren't defined at the proxy level.");
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode kickPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target == null)
			return ControlCode.NOT_FOUND;
		else if (target.hasPermission("iomawaba.admin"))
			return ControlCode.IS_ADMIN;

		target.disconnect(new ComponentBuilder(message).create());
		
		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode tempBanPlayer(String player, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {
		//target can be null if the player is offline
		UUID targetUuid = null;
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target != null) {
			targetUuid = target.getUniqueId();
		
			if (target.hasPermission("iomawaba.admin"))
				return ControlCode.IS_ADMIN;
		}

		BanData ban = db.banTarget(targetUuid, player, null, sourceUuid, sourceName, null, endTime, message);

		if (target != null)
			target.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));

		if (useBroadcast)
			ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode tempBanIp(String ip, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {

		BanData ban = db.banTarget(null, null, ip, sourceUuid, sourceName, null, endTime, message);

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if (ip.equals(player.getAddress().getHostString()) &&
				!player.hasPermission("iomawaba.admin")) {
				player.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));
			}
		}

		if (useBroadcast)
			ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode banPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		UUID targetUuid = null;
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target != null)
			targetUuid = target.getUniqueId();
		
		if (target.hasPermission("iomawaba.admin"))
			return ControlCode.IS_ADMIN;

		BanData ban = db.banTarget(targetUuid, player, null, sourceUuid, sourceName, null, null, message);

		if (target != null)
			target.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));

		if (useBroadcast)
			ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode banIp(String ip, UUID sourceUuid, String sourceName, String message) {

		BanData ban = db.banTarget(null, null, ip, sourceUuid, sourceName, null, null, message);

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if (ip.equals(player.getAddress().getHostString()) &&
				!player.hasPermission("iomawaba.admin")) {
				player.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));
			}
		}

		if (useBroadcast)
			ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));
		
		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		//TODO: Not implemented at the proxy level
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode unbanPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		boolean result = db.unbanTarget(null, player, null, sourceUuid, sourceName, null, message);

		CommandSender source = ProxyServer.getInstance().getPlayer(sourceUuid);
		if (source == null)
			source = ProxyServer.getInstance().getConsole();

		if (result)
			return ControlCode.SUCCESS;
		else
			return ControlCode.NOT_FOUND;
	}


	@Override
	public ControlCode unbanIp(String ip, UUID sourceUuid, String sourceName, String message) {
		boolean result = db.unbanTarget(null, null, ip, sourceUuid, sourceName, null, message);

		if (result)
			return ControlCode.SUCCESS;
		else
			return ControlCode.NOT_FOUND;
	}


	@Override
	public void useBroadcast(boolean setting) {
		this.useBroadcast = setting;
	}
	
}
