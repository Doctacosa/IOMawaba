package com.interordi.iomawaba.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerActions {

	//Do the actions
	abstract boolean warnPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract boolean kickPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract boolean tempBanPlayer(String player, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message);
	abstract boolean tempBanIp(String ip, UUID sourceUuid, String sourceName, LocalDateTime endTime, String message);
	abstract boolean banPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract boolean banIp(String ip, UUID sourceUuid, String sourceName, String message);
	
	//Undo the actions
	abstract boolean unwarnPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract boolean unbanPlayer(String player, UUID sourceUuid, String sourceName, String message);
	abstract boolean unbanIp(String ip, UUID sourceUuid, String sourceName, String message);
	
	//Settings
	abstract void useBroadcast(boolean setting);
}
