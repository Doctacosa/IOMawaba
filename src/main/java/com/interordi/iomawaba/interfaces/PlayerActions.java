package com.interordi.iomawaba.interfaces;

public interface PlayerActions {

	//Do the actions
	abstract boolean warnPlayer(String player, String message);
	abstract boolean kickPlayer(String player, String message);
	abstract boolean banPlayer(String player, String message);
	abstract boolean banIp(String ip, String message);
	
	//Undo the actions
	abstract boolean unwarnPlayer(String player, String message);
	abstract boolean unbanPlayer(String player, String message);
	abstract boolean unbanIp(String ip, String message);
	
}
