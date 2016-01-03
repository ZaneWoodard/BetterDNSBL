package com.beastsmc.dnsbl;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterDNSBL extends JavaPlugin {
	public HashMap<String, Boolean> ipCache;
	public HashSet<String> bannedISPs;
	public HashSet<String> usernameBypass;
	public void onEnable() {
		ipCache = new HashMap<String, Boolean>();
		bannedISPs = new HashSet<String>();
		usernameBypass = new HashSet<String>();
		loadData();
		getServer().getPluginManager().registerEvents(new LoginListener(this), this);
		getCommand("dnsbl").setExecutor(this);
	}
	
	private void loadData() {
		saveDefaultConfig();
		for(String name : getConfig().getStringList("username-bypass")) {
			usernameBypass.add(name);
		}
		for(String isp : getConfig().getStringList("banned-isps")) {
			bannedISPs.add(isp);
		}
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String strCmd, String[] args) {
		if(args.length>0) {
			if(args[0].equalsIgnoreCase("reload")) {
				reloadConfig();
				loadData();
				sender.sendMessage("Config reloaded!");
				return true;
			}
		}
		return false;
	}
}
