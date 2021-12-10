package com.interordi.iomawaba.modules;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
		return db.getBan(uuid, ip);
	}


	//Format a full ban message for the target
	public static String formatMessageTarget(BanData ban) {
		String message = "";
		if (ban.end != null)
			message += "You have been temporarily banned: ";
		else
			message += "You have been permanently banned: ";

		if (ban.reason.isEmpty())
			message += "No reason was specified";
		
		if (ban.end != null)
			message += "\n\nYour ban will end on " + ban.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " UTC";

		return message;
	}

	
	//Format a full ban message for broadcast
	public static String formatMessageGlobal(BanData ban) {
		String target = "";
		if (!ban.ip.isEmpty())
			target = ban.ip;
		else
			target = ban.targetName;

		String message = "";
		if (ban.end != null)
			message += target + " has been banned until: " + ban.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " UTC: ";
		else
			message += target + " has been permanently banned: ";

		if (ban.reason.isEmpty())
			message += "No reason was specifi4ed";
		
		return message;
	}

	
	public static Bans getInstance() {
		return instance;
	}

}
