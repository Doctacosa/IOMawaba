package com.interordi.iomawaba.modules;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.interordi.iomawaba.utilities.BanData;
import com.interordi.iomawaba.utilities.Database;

public class Bans {

	Database db;
	static Bans instance;


	public Bans(Database db) {
		this.db = db;
		Bans.instance = this;
	}
	

	//Check if the given player is banned
	public BanData isBanned(UUID uuid, String ip) {
		return db.getBan(uuid, ip, false);
	}


	//Format a full ban message for the target
	public static String formatMessageTarget(BanData ban) {
		String message = "";
		if (ban.end != null)
			message += "You have been temporarily banned: ";
		else
			message += "You have been permanently banned: ";

		if (ban.reason != null && !ban.reason.isEmpty())
			message += ban.reason;
		else
			message += "No reason was specified";
		
		if (ban.end != null)
			message += "\n\nYour ban will end on " + ban.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "";

		return message;
	}

	
	//Format a full ban message for broadcast
	public static String formatMessageGlobal(BanData ban) {
		String target = "";
		if (ban.ip != null && !ban.ip.isEmpty())
			target = ban.ip;
		else
			target = ban.targetName;

		String message = "";
		if (ban.end != null)
			message += target + " has been banned until " + ban.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ": " + ban.reason;
		else
			message += target + " has been permanently banned: " + ban.reason;

		if (ban.reason.isEmpty())
			message += "No reason was specified";
		
		return message;
	}
	

	//Parse a ban duration in the format "2d5h"
	public static LocalDateTime parseDuration(String arg) {
		LocalDateTime end = LocalDateTime.now();

		//Suffixes: y for years, mo for months, w for week, d for day, h for hour, m for minute and s for second

		Pattern r = Pattern.compile("([0-9]*)([a-zA-Z]+)");
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
				case "year":
				case "years":
					end = end.plusYears(value);
					break;
				case "mo":
				case "mth":
				case "month":
				case "months":
					end = end.plusMonths(value);
					break;
				case "w":
				case "wk":
				case "wks":
				case "week":
				case "weeks":
					end = end.plusWeeks(value);
					break;
				case "d":
				case "day":
				case "days":
					end = end.plusDays(value);
					break;
				case "h":
				case "hr":
				case "hrs":
				case "hour":
				case "hours":
					end = end.plusHours(value);
					break;
				case "m":
				case "min":
				case "mins":
				case "minute":
				case "minutes":
					end = end.plusMinutes(value);
					break;
				case "s":
				case "sec":
				case "secs":
				case "second":
				case "seconds":
					end = end.plusSeconds(value);
					break;
				default:
					return null;
			}
		}

		return end;
	}


	public static Bans getInstance() {
		return instance;
	}

}
