package com.interordi.iomawaba;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.interordi.iomawaba.commands.GKick;
import com.interordi.iomawaba.interfaces.PlayerActions;
import com.interordi.iomawaba.listeners.bungee.PlayersListener;
import com.interordi.iomawaba.modules.PlayerActionsBungee;
import com.interordi.iomawaba.modules.Warnings;
import com.interordi.iomawaba.utilities.Database;

public class IOMawabaBungee extends Plugin {

	public static IOMawabaBungee instance;
	
	public Database db = null;

	public Warnings warnings;

	
	public void onEnable() {
		instance = this;

		//Create default configuration if not present
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Get the configuration data
		Configuration configuration;
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			getLogger().severe("Failed to read the configuration file, stopping.");
			return;
		}

		String dbHost = configuration.getString("database.host", null);
		int dbPort = configuration.getInt("database.port", 3306);
		String dbUsername = configuration.getString("database.username");
		String dbPassword = configuration.getString("database.password");
		String dbBase = configuration.getString("database.base");

		db = new Database(dbHost, dbPort, dbUsername, dbPassword, dbBase);


		PlayerActions pa = new PlayerActionsBungee();

		getProxy().getPluginManager().registerListener(this, new PlayersListener());

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new GKick(pa));

		getLogger().info("IOMawaba enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IOMawaba disabled");
	}
}