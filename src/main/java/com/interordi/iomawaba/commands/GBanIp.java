package com.interordi.iomawaba.commands;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class GBanIp extends Command {

	PlayerActions actions;

	public GBanIp(PlayerActions actions) {
		super("GBanIp");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder("You must specify a target IP address.").color(ChatColor.RED).create());
			return;
		}

		String targetRaw = args[0];

		String message = "Kicked: ";
		if (args.length > 1)
			message += StringUtils.strJoin(args, " ", 1);
		else
			message += "No reason was specified";


		boolean result = actions.banIp(targetRaw, message);

		if (!result) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not online!").color(ChatColor.RED).create());
		}
		
	}
	
}
