package com.interordi.iomawaba;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.CommandTargets;
import com.interordi.iomawaba.utilities.Commands;
import com.interordi.iomawaba.utilities.Database;
import com.interordi.iomawaba.utilities.StringUtils;

public class IOMawabaSpigot extends JavaPlugin {

	public static IOMawabaSpigot instance;
	
	public Database db = null;

	public Warnings warnings;

	
	public void onEnable() {
		instance = this;

		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();
		
		String dbHost = this.getConfig().getString("database.host", null);
		int dbPort = this.getConfig().getInt("database.port", 3306);
		String dbUsername = this.getConfig().getString("database.username");
		String dbPassword = this.getConfig().getString("database.password");
		String dbBase = this.getConfig().getString("database.base");

		db = new Database(dbHost, dbPort, dbUsername, dbPassword, dbBase);
		if (!db.init()) {
			getLogger().severe("---------------------------------");
			getLogger().severe("Failed to initialize the database");
			getLogger().severe("Make sure to configure config.yml");
			getLogger().severe("---------------------------------");
			return;
		}
		
		warnings = new Warnings(this, db);

		getLogger().info("IOMawaba enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IOMawaba disabled");
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//Get the list of potential targets if a selector was used
		CommandTargets results = Commands.findTargets(Bukkit.getServer(), sender, cmd, label, args);
		
		boolean result = false;
		if (results.position != -1) {
			//Run the command for each target identified by the selector
			for (String target : results.targets) {
				args[results.position] = target;
				
				result = runCommand(sender, cmd, label, args);
			}
		} else {
			//Run the command as-is
			result = runCommand(sender, cmd, label, args);
		}
		
		return result;
	}
	
	
	//Actually run the entered command
	public boolean runCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("warning") || cmd.getName().equalsIgnoreCase("w")) {
			
			if (!sender.hasPermission("iomawaba.warning")) {
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
				return true;
			}

			Player target = null;
			String playerName = "";

			if (args.length >= 1)
				playerName = args[0];
			else {
				sender.sendMessage(ChatColor.RED + "Missing parameter: player name");
				return true;
			}
			target = getServer().getPlayer(playerName);

			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Target not found: " + playerName);
				return true;
			}

			String message = "";
			if (args.length > 1)
				message += StringUtils.strJoin(args, " ", 1);
	
			UUID senderUuid = null;
			if (sender instanceof Player) {
				Player pSender = (Player) sender;
				senderUuid = pSender.getUniqueId();
			}

			warnings.giveWarning(target, senderUuid, sender.getName(), message);

			return true;
		}
		
		return false;
	}
}
