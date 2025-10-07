package com.interordi.iomawaba.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class GUnban extends Command implements TabExecutor {

	PlayerActions actions;

	public GUnban(PlayerActions actions) {
		super("GUnban");
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

		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder("You must specify a target name.").color(ChatColor.RED).create());
			return;
		}

		UUID senderUuid = null;
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer pSender = (ProxiedPlayer) sender;
			senderUuid = pSender.getUniqueId();
		}

		String targetRaw = args[0];

		String message = "";
		if (args.length > 1)
			message += StringUtils.strJoin(args, " ", 1);


		ControlCode result = actions.unbanPlayer(targetRaw, senderUuid, sender.getName(), message);

		if (result == ControlCode.SUCCESS) {
			sender.sendMessage(new ComponentBuilder("Player " + targetRaw + " has been unbanned.").color(ChatColor.GREEN).create());
		
		} else if (result == ControlCode.NOT_FOUND) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not banned!").color(ChatColor.RED).create());
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
