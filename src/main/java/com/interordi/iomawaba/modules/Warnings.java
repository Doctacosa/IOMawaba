package com.interordi.iomawaba.modules;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.interordi.iomawaba.IOMawabaSpigot;
import com.interordi.iomawaba.utilities.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warnings {

	private String defaultMessage = "No griefing will be tolerated. Griefing is breaking or taking anything that belongs to someone else, or adding to a structure that isn't yours, without permission.";

	private IOMawabaSpigot plugin;
	private Database db;


	public Warnings(IOMawabaSpigot plugin, Database db) {
		this.plugin = plugin;
		this.db = db;
	}


	public void giveWarning(Player target, CommandSender sender, String message) {

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
						actOnWarning(target, sender, finalMessage, nbWarnings);
					}
				});
			}
		});
	}


	//Act based on the number of warnings received
	//nbWarnings represent the number of warnings already received
	public void actOnWarning(Player target, CommandSender sender, final String message, int nbWarnings) {

		String title = ChatColor.RED + "" + ChatColor.BOLD + "WARNING";
		String subtitle = ChatColor.GOLD + target.getDisplayName() + ", see the chat now.";
		boolean kick = false;
		int ban = 0;

		UUID senderUuid = null;
		if (sender instanceof Player) {
			Player pSender = (Player) sender;
			senderUuid = pSender.getUniqueId();
		}
		final UUID finalSenderUuid = senderUuid;

		if (nbWarnings >= 3) {
			//Ban for 30d
			ban = 30;

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
		if (kick) {
			Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					target.kickPlayer("Official warning - review the rules now.");
				}
			}, 10 * 20L);
		}

		//Ban if requested
		if (ban > 0) {
			target.kickPlayer("You have been banned for " + ban + " days: " + message);
			Bukkit.getServer().getLogger().info("|IOBAN|" + target.getDisplayName() + " was banned (" + ban + "): " + message);
			
			LocalDateTime endTime = LocalDateTime.now().plusDays(ban);

			Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.db.banPlayer(target.getUniqueId(), finalSenderUuid, sender.getName(), null, endTime, message);
				}
			});
		}

		//Log
		Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.db.logWarning(target.getUniqueId(), finalSenderUuid, sender.getName(), message);
			}
		});

	}
	

	public static LocalDateTime parseDuration(String arg) {
		LocalDateTime end = LocalDateTime.now();

		//Suffixes: y for years, mo for months, w for week, d for day, h for hour, m for minute and s for second

		Pattern r = Pattern.compile("([0-9]*)([a-zA-Z])");
		Matcher m = r.matcher(arg);

		while (m.find()) {
			int value = 0;
			String valueString = m.group(1);
			String unit = m.group(2);

			try {
				value = Integer.parseInt(valueString);
			} catch (NumberFormatException e) {
				return null;
			}

			switch (unit) {
				case "y":
					end = end.plusYears(value);
					break;
				case "mo":
					end = end.plusMonths(value);
					break;
				case "w":
					end = end.plusWeeks(value);
					break;
				case "d":
					end = end.plusDays(value);
					break;
				case "h":
					end = end.plusHours(value);
					break;
				case "m":
					end = end.plusMinutes(value);
					break;
				case "s":
					end = end.plusSeconds(value);
					break;
				default:
					return null;
			}
		}

		return end;
	}
}
