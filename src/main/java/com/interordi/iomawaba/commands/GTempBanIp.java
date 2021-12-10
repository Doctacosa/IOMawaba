package com.interordi.iomawaba.commands;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
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
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("iomawaba.admin")) {
			sender.sendMessage(new ComponentBuilder("You don't have permission to use this command.").color(ChatColor.RED).create());
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder("You must specify a target address.").color(ChatColor.RED).create());
			return;
		}

		UUID senderUuid = null;
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer pSender = (ProxiedPlayer) sender;
			senderUuid = pSender.getUniqueId();
		}

		String targetIp = args[0];
		LocalDateTime endTime = LocalDateTime.now();

		String message = "";
		if (args.length > 1)
			message += StringUtils.strJoin(args, " ", 1);


		boolean result = actions.tempBanIp(targetIp, senderUuid, sender.getName(), endTime, message);

	}
	
}
