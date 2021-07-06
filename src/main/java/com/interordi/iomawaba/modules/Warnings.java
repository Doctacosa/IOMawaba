package com.interordi.iomawaba.modules;

import com.interordi.iomawaba.IOMawaba;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warnings {

	private String defaultMessage = "No griefing will be tolerated. Griefing is breaking or taking anything that belongs to someone else, or adding to a structure that isn't yours, without permission.";

	private IOMawaba plugin;


	public Warnings(IOMawaba plugin) {
		this.plugin = plugin;
	}


	public void giveWarning(CommandSender sender, Player target, String message) {

		if (message == null || message.length() == 0)
			message = defaultMessage;

		final String finalMessage = message;

		//Get the number of warnings on this user
		Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				final int nbWarnings = plugin.db.getWarnings(target.getUniqueId()).size();

				Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						actOnWarning(sender, target, finalMessage, nbWarnings);
					}
				});
			}
		});
	}


	//Act based on the number of warnings received
	//nbWarnings represent the number of warnings already received
	public void actOnWarning(CommandSender sender, Player target, final String message, int nbWarnings) {

		String title = ChatColor.RED + "" + ChatColor.BOLD + "WARNING";
		String subtitle = ChatColor.GOLD + target.getDisplayName() + ", see the chat now.";
		boolean kick = false;

		if (nbWarnings >= 3) {
			//Ban for 30d
			//TODO: Send message to IOChatServer
			//TODO: Add a method to IOChatServer to actually receive commands
			//TODO: Switch to internal ban method eventually

		} else if (nbWarnings == 2) {
			//Stop then kick on delay, final warning
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 7), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30 * 20, 128), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 7), false);

			title = ChatColor.RED + "" + ChatColor.BOLD + "FINAL WARNING";
			subtitle = ChatColor.GOLD + "Continue and you will be banned.";
			kick = true;
		
		} else if (nbWarnings == 1) {
			//Stop then kick on delay
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 7), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30 * 20, 128), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 7), false);

			title = ChatColor.RED + "" + ChatColor.BOLD + "SECOND WARNING";
			subtitle = ChatColor.GOLD + "You will be kicked in a few seconds.";
			kick = true;

		} else {
			//Slowdown and warning
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 2), false);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 2), false);
		}

		//Send the warning to the player
		target.sendMessage(ChatColor.RED + "WARNING: " + ChatColor.WHITE + message);
		target.sendTitle(title, subtitle, 10, 100, 10);

		//Notify the sender
		sender.sendMessage(ChatColor.GREEN + "Warning sent to " + target.getDisplayName() + ".");

		//Notify staff
		Bukkit.getServer().getLogger().info("|IOSTAFF|" + target.getDisplayName() + " was warned (" + (nbWarnings + 1) + "): " + message);

		//Kick on delay if requested
		Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				target.kickPlayer("Official warning - review the rules now.");
			}
		}, 10 * 20L);

		//Log
		Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.db.logWarning(target.getUniqueId(), message);
			}
		});

	}
	
}
