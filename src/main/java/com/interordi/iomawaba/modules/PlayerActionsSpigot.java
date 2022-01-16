package com.interordi.iomawaba.modules;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.utilities.ControlCode;
import com.interordi.iomawaba.utilities.Database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerActionsSpigot implements PlayerActions {

	Database db;
	boolean useBroadcast = true;
	

	public PlayerActionsSpigot(Database db) {
		this.db = db;
	}


	@Override
	public ControlCode warnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		//NOTE: Called directly for now, only available at the server level
		//Warnings.giveWarning(player, message);

		Player target = Bukkit.getServer().getPlayer(player);
		if (target == null) {
			return ControlCode.ERROR;
		}

		db.logWarning(target.getUniqueId(), sourceUuid, sourceName, message);

		return ControlCode.SUCCESS;
	}


	@Override
	public ControlCode kickPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode banPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode tempBanPlayer(String player, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode tempBanIp(String ip, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode banIp(String ip, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public ControlCode unbanPlayer(String player, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}
	

	@Override
	public ControlCode unbanIp(String ip, UUID sourceUuid, String sourceName, String message) {
		// TODO Auto-generated method stub
		return ControlCode.ERROR;
	}


	@Override
	public void useBroadcast(boolean setting) {
		this.useBroadcast = setting;
	}
	
}
