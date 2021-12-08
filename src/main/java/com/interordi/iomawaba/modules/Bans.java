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


	//Format a full ban message for display
	public static String formatMessage(BanData ban) {
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

	
	public static Bans getInstance() {
		return instance;
	}

}
