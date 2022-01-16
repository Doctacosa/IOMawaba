package com.interordi.iomawaba.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interordi.iomawaba.utilities.ControlCode;

public interface PlayerActions {

	//Do the actions
	abstract ControlCode warnPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract ControlCode kickPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract ControlCode tempBanPlayer(String player, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message);
	abstract ControlCode tempBanIp(String ip, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message);
	abstract ControlCode banPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract ControlCode banIp(String ip, UUID sourceUuid, String sourceName, String message);
	
	//Undo the actions
	abstract ControlCode unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract ControlCode unbanPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract ControlCode unbanIp(String ip, UUID sourceUuid, String sourceName, String message);
	
	//Settings
	abstract void useBroadcast(boolean setting);
}
