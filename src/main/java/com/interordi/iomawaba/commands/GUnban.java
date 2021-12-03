package com.interordi.iomawaba.commands;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class GUnban extends Command {

	PlayerActions actions;

	public GUnban(PlayerActions actions) {
		super("GUnban");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder("You must specify a target name.").color(ChatColor.RED).create());
			return;
		}

		String targetRaw = args[0];

		String message = "Kicked: ";
		if (args.length > 1)
			message += StringUtils.strJoin(args, " ", 1);
		else
			message += "No reason was specified";


		boolean result = actions.unbanPlayer(targetRaw, message);

		if (!result) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not banned!").color(ChatColor.RED).create());
		}
		
	}
	
}
