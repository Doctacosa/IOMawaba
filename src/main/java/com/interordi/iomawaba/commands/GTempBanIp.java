package com.interordi.iomawaba.commands;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.modules.Bans;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GTempBanIp extends Command {

	PlayerActions actions;

	public GTempBanIp(PlayerActions actions) {
		super("GTempBanIp");
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
			sender.sendMessage(new ComponentBuilder("You must specify a target address then a duration.").color(ChatColor.RED).create());
			return;
		}

		UUID senderUuid = null;
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer pSender = (ProxiedPlayer) sender;
			senderUuid = pSender.getUniqueId();
		}

		String targetIp = args[0];
		ZonedDateTime endTime = Bans.parseDuration(args[1]);

		if (endTime == null) {
			sender.sendMessage(new ComponentBuilder("Invalid duration specified: " + args[1]).color(ChatColor.RED).create());
			return;
		}

		String message = "";
		if (args.length > 2)
			message += StringUtils.strJoin(args, " ", 2);


		ControlCode result = actions.tempBanIp(targetIp, senderUuid, sender.getName(), endTime, message);

		if (result == ControlCode.SUCCESS) {
			sender.sendMessage(new ComponentBuilder("Operation successful.").color(ChatColor.GREEN).create());

		}

	}
	
}
