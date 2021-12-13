package com.interordi.iomawaba.commands;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GTempBan extends Command {

	PlayerActions actions;

	public GTempBan(PlayerActions actions) {
		super("GTempBan");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("iomawaba.admin")) {
			sender.sendMessage(new ComponentBuilder("You don't have permission to use this command.").color(ChatColor.RED).create());
			return;
		}

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
		LocalDateTime endTime = Warnings.parseDuration(args[1]);

		if (endTime == null) {
			sender.sendMessage(new ComponentBuilder("Invalid duration specified: " + args[1]).color(ChatColor.RED).create());
			return;
		}

		String message = "";
		if (args.length > 2)
			message += StringUtils.strJoin(args, " ", 2);


		boolean result = actions.tempBanPlayer(targetRaw, senderUuid, sender.getName(), endTime, message);

		if (!result) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not online!").color(ChatColor.RED).create());
		}
		
	}
	
}
