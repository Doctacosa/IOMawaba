package com.interordi.iomawaba.commands;

import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GUnbanIp extends Command {

	PlayerActions actions;

	public GUnbanIp(PlayerActions actions) {
		super("GUnbanIp");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("iomawaba.admin")) {
			sender.sendMessage(new ComponentBuilder("You don't have permission to use this command.").color(ChatColor.RED).create());
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder("You must specify a target IP address.").color(ChatColor.RED).create());
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


		ControlCode result = actions.unbanIp(targetRaw, senderUuid, sender.getName(), message);

		if (result == ControlCode.SUCCESS) {
			sender.sendMessage(new ComponentBuilder("The IP address " + targetRaw + " has been unbanned.").color(ChatColor.GREEN).create());

		} else if (result == ControlCode.NOT_FOUND) {
			sender.sendMessage(new ComponentBuilder("The IP address " + targetRaw + " has not been found.").color(ChatColor.RED).create());
		}
		
	}
	
}
