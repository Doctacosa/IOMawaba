package com.interordi.iomawaba.modules;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.Database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerActionsSpigot implements PlayerActions {

	Database db;
	

	public PlayerActionsSpigot(Database db) {
		this.db = db;
	}


	@Override
	public boolean warnPlayer(String player, String message) {
		//NOTE: Called directly for now, only available at the server level
		//Warnings.giveWarning(player, message);

		Player target = Bukkit.getServer().getPlayer(player);
		if (target == null) {
			//Return to sender
			return false;
		}

		db.logWarning(target.getUniqueId(), message, null, null);

		return true;
	}


	@Override
	public boolean kickPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean banPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean tempBanPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean banIp(String ip, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean unwarnPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean unbanPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean unbanIp(String ip, String message) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
