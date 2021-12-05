package com.interordi.iomawaba.commands;

import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GKick extends Command {

	PlayerActions actions;

	public GKick(PlayerActions actions) {
		super("GKick");
		this.actions = actions;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {

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

		String message = "Kicked: ";
		if (args.length > 1)
			message += StringUtils.strJoin(args, " ", 1);
		else
			message += "No reason was specified";


		boolean result = actions.kickPlayer(targetRaw, senderUuid, sender.getName(), message);

		if (!result) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not online!").color(ChatColor.RED).create());
		}
		
	}
	

		/*
		if ((sender instanceof ProxiedPlayer)) {
			ProxiedPlayer pSender = (ProxiedPlayer)sender;
		}

		//Sends to lobby
		p.connect(ProxyServer.getInstance().getServerInfo("lobby"));
		*/
}
