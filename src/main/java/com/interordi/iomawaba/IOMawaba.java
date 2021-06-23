package com.interordi.iomawaba;

//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
import java.util.Arrays;
//import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.interordi.iomawaba.utilities.CommandTargets;
import com.interordi.iomawaba.utilities.Commands;

public class IOMawaba extends JavaPlugin {

	public static IOMawaba instance;

	
	public void onEnable() {
		instance = this;
		
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
			
			if (!sender.hasPermission("iocommands.warning")) {
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
			if (args.length > 1) {
				message += strJoin(Arrays.copyOfRange(args, 1, args.length), " ");
			} else
				message += "No griefing will be tolerated. Griefing is breaking or taking anything that belongs to someone else, or adding to a structure that isn't yours, without permission.";

			target.sendMessage(ChatColor.RED + "WARNING: " + ChatColor.WHITE + message);
			target.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "WARNING", ChatColor.GOLD + playerName + ", see the chat now.", 10, 100, 10);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 2), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 2), false);

			sender.sendMessage(ChatColor.GREEN + "Warning sent to " + playerName + ".");

			//Notify staff
			Bukkit.getServer().getLogger().info("|IOSTAFF|" + playerName + " was warned: " + message);

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
