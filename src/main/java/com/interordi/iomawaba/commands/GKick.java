package com.interordi.iomawaba.commands;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GKick extends Command {

	PlayerActions pa;

	public GKick(PlayerActions pa) {
		super("GKick");
		this.pa = pa;
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {
		if ((sender instanceof ProxiedPlayer)) {
			//ProxiedPlayer pSender = (ProxiedPlayer)sender;
		}

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


		//Sends to lobby
		//p.connect(ProxyServer.getInstance().getServerInfo("lobby"));

		boolean result = pa.kickPlayer(targetRaw, message);

		if (!result) {
			sender.sendMessage(new ComponentBuilder(targetRaw + " is not online!").color(ChatColor.RED).create());
		}
		
	}
	
}
