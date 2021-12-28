package com.interordi.iomawaba.modules;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.BanData;
import com.interordi.iomawaba.utilities.Database;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
	public boolean tempBanPlayer(String player, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {
		//target can be null if the player is offline
		UUID targetUuid = null;
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target != null)
			targetUuid = target.getUniqueId();

		BanData ban = db.banTarget(targetUuid, player, null, sourceUuid, sourceName, null, endTime, message);

		if (target != null)
			target.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));

		ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return true;
	}


	@Override
	public boolean tempBanIp(String ip, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {

		BanData ban = db.banTarget(null, null, ip, sourceUuid, sourceName, null, endTime, message);

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if (ip.equals(player.getAddress().getHostString())) {
				player.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));
			}
		}

		ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return true;
	}


	@Override
	public boolean banPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		UUID targetUuid = null;
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		if (target != null)
			targetUuid = target.getUniqueId();

		BanData ban = db.banTarget(targetUuid, player, null, sourceUuid, sourceName, null, null, message);

		if (target != null)
			target.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));

		ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));

		return true;
	}


	@Override
	public boolean banIp(String ip, UUID sourceUuid, String sourceName, String message) {

		BanData ban = db.banTarget(null, null, ip, sourceUuid, sourceName, null, null, message);

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if (ip.equals(player.getAddress().getHostString())) {
				player.disconnect(new TextComponent(Bans.formatMessageTarget(ban)));
			}
		}

		ProxyServer.getInstance().broadcast(new TextComponent(Bans.formatMessageGlobal(ban)));
		ProxyServer.getInstance().getLogger().info("|IOBAN|" + Bans.formatMessageGlobal(ban));
		
		return true;
	}


	@Override
	public boolean unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		//TODO: Not implemented at the proxy level
		return false;
	}


	@Override
	public boolean unbanPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		boolean result = db.unbanTarget(null, player, null, sourceUuid, sourceName, null, message);

		CommandSender source = ProxyServer.getInstance().getPlayer(sourceUuid);
		if (source == null)
			source = ProxyServer.getInstance().getConsole();

		if (result)
			source.sendMessage(new ComponentBuilder("Player " + player + " has been unbanned.").color(ChatColor.GREEN).create());
		else
			source.sendMessage(new ComponentBuilder("Player " + player + " has not been found.").color(ChatColor.RED).create());

		return result;
	}


	@Override
	public boolean unbanIp(String ip, UUID sourceUuid, String sourceName, String message) {
		boolean result = db.unbanTarget(null, null, ip, sourceUuid, sourceName, null, message);

		ProxiedPlayer source = ProxyServer.getInstance().getPlayer(sourceUuid);
		if (result)
			source.sendMessage(new ComponentBuilder("The IP address " + ip + " has been unbanned.").color(ChatColor.GREEN).create());
		else
			source.sendMessage(new ComponentBuilder("The IP address " + ip + " has not been found.").color(ChatColor.RED).create());

		return result;
	}
	
}
