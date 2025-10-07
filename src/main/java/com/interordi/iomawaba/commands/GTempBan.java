package com.interordi.iomawaba.commands;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.modules.Bans;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class GTempBan extends Command implements TabExecutor {

	PlayerActions actions;

	public GTempBan(PlayerActions actions) {
		super("GTempBan");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] rawArgs) {

		if (!sender.hasPermission("iomawaba.admin")) {
			sender.sendMessage(new ComponentBuilder("You don't have permission to use this command.").color(ChatColor.RED).create());
			return;
		}

		//Remove empty arguments
		String[] args =
			Stream.of(rawArgs)
				.filter(item -> item != null && !"".equals(item))
				.collect(Collectors.toList())
				.toArray(new String[0]);

		if (args.length < 2) {
			sender.sendMessage(new ComponentBuilder("You must specify a target name then a duration.").color(ChatColor.RED).create());
			return;
		}

		UUID senderUuid = null;
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer pSender = (ProxiedPlayer) sender;
			senderUuid = pSender.getUniqueId();
		}

		String targetRaw = args[0];
		ZonedDateTime endTime = Bans.parseDuration(args[1]);

		if (endTime == null) {
			sender.sendMessage(new ComponentBuilder("Invalid duration specified: " + args[1]).color(ChatColor.RED).create());
			return;
		}

		String message = "";
		if (args.length > 2)
			message += StringUtils.strJoin(args, " ", 2);


		ControlCode result = actions.tempBanPlayer(targetRaw, senderUuid, sender.getName(), endTime, message);

		if (result == ControlCode.ERROR) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not online!").color(ChatColor.RED).create());

		} else if (result == ControlCode.IS_ADMIN) {
			sender.sendMessage(new ComponentBuilder("You can't target other staff.").color(ChatColor.RED).create());
		
		} else if (result == ControlCode.SUCCESS) {
			sender.sendMessage(new ComponentBuilder("Operation successful.").color(ChatColor.GREEN).create());

		}
		
	}


	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 0)
			return ImmutableSet.of();

		Set< String > matches = new HashSet< String >();
		if (args.length == 1) {
			String search = args[0].toLowerCase();
			
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
				if (player.getName().toLowerCase().startsWith(search)) {
					matches.add(player.getName());
				}
			}
		}

		return matches;
	}
}
