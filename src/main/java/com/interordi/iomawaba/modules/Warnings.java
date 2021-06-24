package com.interordi.iomawaba.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warnings {

	private static String defaultMessage = "No griefing will be tolerated. Griefing is breaking or taking anything that belongs to someone else, or adding to a structure that isn't yours, without permission.";


	public static void giveWarning(CommandSender sender, Player target, String message) {

		if (message == null || message.length() == 0)
			message = defaultMessage;

		target.sendMessage(ChatColor.RED + "WARNING: " + ChatColor.WHITE + message);
		target.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "WARNING", ChatColor.GOLD + target.getDisplayName() + ", see the chat now.", 10, 100, 10);
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 2), false);
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 2), false);

		sender.sendMessage(ChatColor.GREEN + "Warning sent to " + target.getDisplayName() + ".");

		//Notify staff
		Bukkit.getServer().getLogger().info("|IOSTAFF|" + target.getDisplayName() + " was warned: " + message);
	}
	
}
