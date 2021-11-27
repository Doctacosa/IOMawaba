package com.interordi.iomawaba;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.CommandTargets;
import com.interordi.iomawaba.utilities.Commands;
import com.interordi.iomawaba.utilities.Database;

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
				message += strJoin(Arrays.copyOfRange(args, 1, args.length), " ");
	

			warnings.giveWarning(sender, target, message);

			return true;
		}
		
		return false;
	}



	public static String strJoin(String[] aArr, String sSep) {
		return strJoin(aArr, sSep, 0);
	}
	
	
	public static String strJoin(String[] aArr, String sSep, int startPos) {
		if (aArr.length <= startPos)
			return "";
		
		StringBuilder sbStr = new StringBuilder();
		for (int i = startPos, il = aArr.length; i < il; i++) {
			if (i > startPos)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
}
