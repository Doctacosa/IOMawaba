package com.interordi.iomawaba.modules;

import com.interordi.iomawaba.interfaces.PlayerActions;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerActionsBungee implements PlayerActions {

	@Override
	public boolean warnPlayer(String player, String message) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean kickPlayer(String player, String message) {
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(player);
		target.disconnect(new ComponentBuilder("Ayaaaah!").create());
		target.sendMessage(new ComponentBuilder(message).create());
		
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean banPlayer(String player, String message) {
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
